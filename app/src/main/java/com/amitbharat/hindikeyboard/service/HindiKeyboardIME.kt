package com.amitbharat.hindikeyboard.service

import android.content.Intent
import android.inputmethodservice.InputMethodService
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.amitbharat.hindikeyboard.keyboard.*
import com.amitbharat.hindikeyboard.settings.SettingsActivity
import com.amitbharat.hindikeyboard.suggestions.SuggestionEngine
import com.amitbharat.hindikeyboard.suggestions.TypingMode
import com.amitbharat.hindikeyboard.theme.ThemeType
import com.amitbharat.hindikeyboard.utils.PreferencesManager
import com.amitbharat.hindikeyboard.utils.SoundHapticHelper

class HindiKeyboardIME : InputMethodService(), LifecycleOwner, ViewModelStoreOwner, SavedStateRegistryOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)
    private val store = ViewModelStore()
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    override val lifecycle: Lifecycle get() = lifecycleRegistry
    override val viewModelStore: ViewModelStore get() = store
    override val savedStateRegistry: SavedStateRegistry get() = savedStateRegistryController.savedStateRegistry

    private var composeView: ComposeView? = null
    private var keyboardState by mutableStateOf(KeyboardUiState())
    private var composingWord = StringBuilder()
    private var speechRecognizer: SpeechRecognizer? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        loadPreferences()
    }

    private fun loadPreferences() {
        val prefs = PreferencesManager.getInstance(this)
        keyboardState = keyboardState.copy(
            activeTheme = prefs.getKeyboardTheme(),
            showNumberRow = prefs.isNumberRowEnabled(),
            enableVibration = prefs.isVibrationEnabled(),
            enableSound = prefs.isSoundEnabled()
        )
    }

    // Disable full screen extract mode to avoid flickering / window resizing blinking on devices
    override fun onEvaluateFullscreenMode(): Boolean = false
    override fun onEvaluateInputViewShown(): Boolean = true

    override fun onCreateInputView(): View {
        window?.window?.decorView?.let { decor ->
            decor.setViewTreeLifecycleOwner(this)
            decor.setViewTreeViewModelStoreOwner(this)
            decor.setViewTreeSavedStateRegistryOwner(this)
        }

        if (composeView == null) {
            composeView = ComposeView(this).apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(this@HindiKeyboardIME.lifecycle))
                setViewTreeLifecycleOwner(this@HindiKeyboardIME)
                setViewTreeViewModelStoreOwner(this@HindiKeyboardIME)
                setViewTreeSavedStateRegistryOwner(this@HindiKeyboardIME)

                setContent {
                    KeyboardScreen(
                        state = keyboardState,
                        onKeyPress = { text -> handleText(text) },
                        onBackspace = { handleBackspace() },
                        onSpace = { handleSpace() },
                        onEnter = { handleEnter() },
                        onLanguageToggle = { handleLanguageToggle() },
                        onShiftToggle = { handleShiftToggle() },
                        onLayoutChange = { layout -> keyboardState = keyboardState.copy(layoutType = layout) },
                        onSuggestionClick = { word -> handleSuggestionClick(word) },
                        onCursorMove = { offset -> handleCursorMove(offset) },
                        onVoiceClick = { toggleVoiceTyping() },
                        onSettingsClick = { launchSettings() },
                        imeAction = currentInputEditorInfo?.imeOptions?.and(EditorInfo.IME_MASK_ACTION) ?: EditorInfo.IME_ACTION_DONE
                    )
                }
            }
        }
        return composeView!!
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        loadPreferences()
        if (lifecycleRegistry.currentState.isAtLeast(Lifecycle.State.CREATED)) {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        }
        composingWord.clear()
        updateSuggestions()
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
        stopVoiceTyping()
        if (lifecycleRegistry.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        }
        composingWord.clear()
    }

    private fun handleText(text: String) {
        SoundHapticHelper.performHapticFeedback(this, isEnabled = keyboardState.enableVibration)
        SoundHapticHelper.playClickSound(this, isEnabled = keyboardState.enableSound)

        val ic = currentInputConnection ?: return

        if (text.length == 1 && text.first().isLetter()) {
            composingWord.append(text)
            updateSuggestions()
            ic.commitText(text, 1)
        } else {
            ic.commitText(text, 1)
            composingWord.clear()
            updateSuggestions()
        }

        if (keyboardState.shiftState == ShiftState.ON) {
            keyboardState = keyboardState.copy(shiftState = ShiftState.OFF)
        }
    }

    private fun handleBackspace() {
        SoundHapticHelper.performHapticFeedback(this, isEnabled = keyboardState.enableVibration)
        SoundHapticHelper.playClickSound(this, isEnabled = keyboardState.enableSound)

        val ic = currentInputConnection ?: return

        if (composingWord.isNotEmpty()) {
            composingWord.deleteCharAt(composingWord.length - 1)
            updateSuggestions()
        }
        ic.deleteSurroundingText(1, 0)
    }

    private fun handleSpace() {
        SoundHapticHelper.performHapticFeedback(this, isEnabled = keyboardState.enableVibration)
        SoundHapticHelper.playClickSound(this, isEnabled = keyboardState.enableSound)

        val ic = currentInputConnection ?: return

        if (keyboardState.typingMode == TypingMode.HINDI_TRANSLITERATION && composingWord.isNotEmpty()) {
            val top = keyboardState.suggestions.firstOrNull()
            if (top != null) {
                val len = composingWord.length
                ic.deleteSurroundingText(len, 0)
                ic.commitText(top + " ", 1)
                composingWord.clear()
                updateSuggestions()
                return
            }
        }

        ic.commitText(" ", 1)
        composingWord.clear()
        updateSuggestions()
    }

    private fun handleEnter() {
        SoundHapticHelper.performHapticFeedback(this, isEnabled = keyboardState.enableVibration)
        SoundHapticHelper.playClickSound(this, isEnabled = keyboardState.enableSound)

        val ic = currentInputConnection ?: return
        val action = currentInputEditorInfo?.imeOptions?.and(EditorInfo.IME_MASK_ACTION) ?: EditorInfo.IME_ACTION_NONE

        if (action != EditorInfo.IME_ACTION_NONE && action != EditorInfo.IME_ACTION_UNSPECIFIED) {
            ic.performEditorAction(action)
        } else {
            ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
            ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER))
        }
        composingWord.clear()
        updateSuggestions()
    }

    private fun handleLanguageToggle() {
        SoundHapticHelper.performHapticFeedback(this, isEnabled = true)

        val newMode = if (keyboardState.typingMode == TypingMode.ENGLISH) {
            TypingMode.HINDI_TRANSLITERATION
        } else {
            TypingMode.ENGLISH
        }
        keyboardState = keyboardState.copy(typingMode = newMode)
        updateSuggestions()
    }

    private fun handleShiftToggle() {
        SoundHapticHelper.performHapticFeedback(this, isEnabled = keyboardState.enableVibration)

        val newShift = when (keyboardState.shiftState) {
            ShiftState.OFF -> ShiftState.ON
            ShiftState.ON -> ShiftState.CAPS_LOCK
            ShiftState.CAPS_LOCK -> ShiftState.OFF
        }
        keyboardState = keyboardState.copy(shiftState = newShift)
    }

    private fun handleSuggestionClick(selectedText: String) {
        SoundHapticHelper.performHapticFeedback(this, isEnabled = keyboardState.enableVibration)
        val ic = currentInputConnection ?: return

        val wordLen = composingWord.length
        if (wordLen > 0) {
            ic.deleteSurroundingText(wordLen, 0)
        }
        ic.commitText(selectedText + " ", 1)
        composingWord.clear()
        updateSuggestions()
    }

    private fun handleCursorMove(offset: Int) {
        val ic = currentInputConnection ?: return
        if (offset > 0) {
            ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT))
            ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_RIGHT))
        } else if (offset < 0) {
            ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT))
            ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_LEFT))
        }
    }

    private fun updateSuggestions() {
        val query = composingWord.toString()
        val suggestions = SuggestionEngine.getSuggestions(query, keyboardState.typingMode)
        keyboardState = keyboardState.copy(currentWord = query, suggestions = suggestions)
    }

    private fun toggleVoiceTyping() {
        if (keyboardState.isListeningVoice) {
            stopVoiceTyping()
            return
        }

        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(this, "Voice recognition service unavailable", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            speechRecognizer?.destroy()
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        mainHandler.post { keyboardState = keyboardState.copy(isListeningVoice = true) }
                    }
                    override fun onBeginningOfSpeech() {}
                    override fun onRmsChanged(rmsdB: Float) {}
                    override fun onBufferReceived(buffer: ByteArray?) {}
                    override fun onEndOfSpeech() {
                        mainHandler.post { keyboardState = keyboardState.copy(isListeningVoice = false) }
                    }
                    override fun onError(error: Int) {
                        mainHandler.post { keyboardState = keyboardState.copy(isListeningVoice = false) }
                    }
                    override fun onResults(results: Bundle?) {
                        mainHandler.post {
                            keyboardState = keyboardState.copy(isListeningVoice = false)
                            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                            if (!matches.isNullOrEmpty()) {
                                val spoken = matches[0]
                                val ic = currentInputConnection
                                ic?.commitText(spoken + " ", 1)
                                SoundHapticHelper.performHapticFeedback(this@HindiKeyboardIME, isEnabled = true)
                            }
                        }
                    }
                    override fun onPartialResults(partialResults: Bundle?) {}
                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }

            val lang = if (keyboardState.typingMode == TypingMode.HINDI_TRANSLITERATION) "hi-IN" else "en-US"
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, lang)
                putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
            }

            speechRecognizer?.startListening(intent)
            keyboardState = keyboardState.copy(isListeningVoice = true)
            SoundHapticHelper.performHapticFeedback(this, isEnabled = true)
        } catch (e: Exception) {
            keyboardState = keyboardState.copy(isListeningVoice = false)
            Toast.makeText(this, "Microphone error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopVoiceTyping() {
        try {
            speechRecognizer?.stopListening()
            speechRecognizer?.destroy()
            speechRecognizer = null
        } catch (ignored: Exception) {}
        keyboardState = keyboardState.copy(isListeningVoice = false)
    }

    private fun launchSettings() {
        val intent = Intent(this, SettingsActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        stopVoiceTyping()
        if (lifecycleRegistry.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        }
        super.onDestroy()
    }
}
