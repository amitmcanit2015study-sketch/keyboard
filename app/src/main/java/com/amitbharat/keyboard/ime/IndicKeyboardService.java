package com.amitbharat.keyboard.ime;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.FrameLayout;
import androidx.core.content.ContextCompat;
import com.amitbharat.keyboard.R;
import com.amitbharat.keyboard.engine.EnglishDictionary;
import com.amitbharat.keyboard.engine.HinglishTransliterator;
import com.amitbharat.keyboard.engine.KeyboardPreferences;
import com.amitbharat.keyboard.ui.MainActivity;
import com.amitbharat.keyboard.ui.VoicePermissionActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IndicKeyboardService extends InputMethodService implements
        SoftKeyboardView.OnKeyboardActionListener,
        CandidateStripView.OnCandidateClickListener,
        EmojiKeyboardView.OnEmojiSelectedListener {

    private KeyboardPreferences preferences;
    private View rootView;
    private CandidateStripView candidateStripView;
    private SoftKeyboardView softKeyboardView;
    private EmojiKeyboardView emojiKeyboardView;
    private FrameLayout keyboardContainer;

    private String currentLanguage = KeyboardPreferences.LANG_HINDI;
    private final StringBuilder composingText = new StringBuilder();
    private List<String> currentSuggestions = Collections.emptyList();

    private long lastSpaceTime = 0;
    private SpeechRecognizer speechRecognizer;
    private boolean isVoiceListening = false;
    private boolean isPasswordField = false;

    private android.content.SharedPreferences sharedPreferences;
    private final android.content.SharedPreferences.OnSharedPreferenceChangeListener prefListener =
            (sp, key) -> {
                if ("pref_theme".equals(key)) {
                    if (softKeyboardView != null) {
                        softKeyboardView.post(() -> softKeyboardView.resolveThemeColors());
                    }
                    if (candidateStripView != null) {
                        candidateStripView.post(() -> candidateStripView.updateTheme(preferences.getTheme()));
                    }
                } else if (KeyboardPreferences.PREF_SELECTED_LANGS.equals(key) || KeyboardPreferences.PREF_CURRENT_LANG.equals(key)) {
                    currentLanguage = preferences.getCurrentLanguage();
                    if (candidateStripView != null) {
                        candidateStripView.post(this::updateStripLanguages);
                    }
                } else if (KeyboardPreferences.PREF_NUMBER_ROW.equals(key)
                        || KeyboardPreferences.PREF_EMOJI_KEY.equals(key)
                        || KeyboardPreferences.PREF_LANG_KEY.equals(key)
                        || KeyboardPreferences.PREF_COMMA_KEY.equals(key)
                        || KeyboardPreferences.PREF_FULLSTOP_KEY.equals(key)) {
                    if (softKeyboardView != null) {
                        softKeyboardView.post(() -> softKeyboardView.rebuildLayout());
                    }
                } else if (KeyboardPreferences.PREF_SUGGESTION_STRIP.equals(key)) {
                    if (candidateStripView != null) {
                        candidateStripView.post(() -> candidateStripView.setVisibility(
                                preferences.isSuggestionStripEnabled() ? View.VISIBLE : View.GONE));
                    }
                }
            };

    @Override
    public void onCreate() {
        super.onCreate();
        android.util.Log.d("IndicKeyboard", "IndicKeyboardService onCreate called");
        preferences = new KeyboardPreferences(this);
        currentLanguage = preferences.getCurrentLanguage();
        sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(prefListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopVoiceInput();
        if (sharedPreferences != null) {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(prefListener);
        }
    }

    @Override
    public void onFinishInputView(boolean finishingInput) {
        super.onFinishInputView(finishingInput);
        stopVoiceInput();
    }

    @Override
    public boolean onEvaluateInputViewShown() {
        return true;
    }

    @Override
    public View onCreateInputView() {
        android.util.Log.d("IndicKeyboard", "onCreateInputView called");
        android.content.Context themedContext =
                new androidx.appcompat.view.ContextThemeWrapper(this, R.style.Theme_IndicKeyboard);
        themedContext = com.google.android.material.color.DynamicColors.wrapContextIfAvailable(themedContext);
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(themedContext);
        rootView = inflater.inflate(R.layout.keyboard_root_view, null);

        candidateStripView = rootView.findViewById(R.id.candidateStripView);
        softKeyboardView = rootView.findViewById(R.id.softKeyboardView);
        emojiKeyboardView = rootView.findViewById(R.id.emojiKeyboardView);
        keyboardContainer = rootView.findViewById(R.id.keyboardContainer);

        candidateStripView.setVisibility(preferences.isSuggestionStripEnabled() ? View.VISIBLE : View.GONE);
        updateStripLanguages();
        candidateStripView.updateTheme(preferences.getTheme());
        candidateStripView.setOnCandidateClickListener(this);

        // 1. Top-left language toggle listener
        candidateStripView.setOnLanguageToggleListener(() -> {
            toggleLanguage();
        });

        // Quick settings shortcut
        candidateStripView.setOnSettingsClickListener(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        // Voice input mic shortcut
        candidateStripView.setOnVoiceClickListener(this::handleVoiceInput);

        softKeyboardView.setOnKeyboardActionListener(this);
        emojiKeyboardView.setOnEmojiSelectedListener(this);

        return rootView;
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);

        composingText.setLength(0);
        currentSuggestions = Collections.emptyList();

        isPasswordField = false;
        if (info != null) {
            int variation = info.inputType & InputType.TYPE_MASK_VARIATION;
            if (variation == InputType.TYPE_TEXT_VARIATION_PASSWORD
                    || variation == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    || variation == InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD
                    || variation == InputType.TYPE_NUMBER_VARIATION_PASSWORD) {
                isPasswordField = true;
            }
        }

        if (candidateStripView != null) {
            candidateStripView.setVisibility((!isPasswordField && preferences.isSuggestionStripEnabled()) ? View.VISIBLE : View.GONE);
            candidateStripView.setSuggestions(Collections.emptyList());
            updateStripLanguages();
            candidateStripView.updateTheme(preferences.getTheme());
        }

        if (softKeyboardView != null) {
            softKeyboardView.resolveThemeColors();

            boolean isNumericInput = false;
            if (info != null) {
                int inputClass = info.inputType & InputType.TYPE_MASK_CLASS;
                if (inputClass == InputType.TYPE_CLASS_NUMBER
                        || inputClass == InputType.TYPE_CLASS_PHONE
                        || inputClass == InputType.TYPE_CLASS_DATETIME) {
                    isNumericInput = true;
                }
                int variation = info.inputType & InputType.TYPE_MASK_VARIATION;
                if (variation == InputType.TYPE_NUMBER_VARIATION_PASSWORD) {
                    isNumericInput = true;
                }
            }

            if (isNumericInput) {
                softKeyboardView.setMode(KeyboardLayoutHelper.MODE_NUMPAD);
            } else {
                softKeyboardView.setMode(KeyboardLayoutHelper.MODE_ALPHA);
            }

            // Check auto-capitalization if enabled
            if (!isNumericInput && preferences.isAutoCapEnabled() && info != null) {
                int caps = info.inputType & InputType.TYPE_MASK_FLAGS;
                boolean shouldCap = (caps & InputType.TYPE_TEXT_FLAG_CAP_SENTENCES) != 0
                        || (caps & InputType.TYPE_TEXT_FLAG_CAP_WORDS) != 0
                        || (caps & InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS) != 0;
                softKeyboardView.setShifted(shouldCap);
            } else {
                softKeyboardView.setShifted(false);
            }
        }

        showSoftKeyboard();
    }

    private void updateStripLanguages() {
        if (candidateStripView != null) {
            String active = preferences.getCurrentLanguage();
            String alt = preferences.getAltLanguage();
            candidateStripView.setLanguages(active, alt);
        }
    }

    private void toggleLanguage() {
        currentLanguage = preferences.toggleLanguage();
        updateStripLanguages();
        updateSuggestions();
    }

    @Override
    public void onKey(int primaryCode, KeyboardKey key) {
        if (isVoiceListening) {
            stopVoiceInput();
        }
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;

        switch (primaryCode) {
            case KeyboardKey.CODE_BACKSPACE:
                handleBackspace(ic);
                break;

            case KeyboardKey.CODE_ENTER:
                handleEnter(ic);
                break;

            case KeyboardKey.CODE_SPACE:
                handleSpace(ic);
                break;

            case KeyboardKey.CODE_EMOJI:
                showEmojiKeyboard();
                break;

            case KeyboardKey.CODE_LANG_SWITCH:
                toggleLanguage();
                break;

            default:
                handleCharacter((char) primaryCode, ic);
                break;
        }
    }

    private void handleCharacter(char ch, InputConnection ic) {
        if (isPasswordField) {
            ic.commitText(String.valueOf(ch), 1);
            return;
        }

        // If it's an alphabetical letter, compose for transliteration or spell suggestion
        if (Character.isLetter(ch)) {
            composingText.append(ch);
            ic.setComposingText(composingText.toString(), 1);
            updateSuggestions();
        } else {
            // Punctuation, symbols, numbers commit current composing word first
            if (composingText.length() > 0) {
                commitComposingText(ic, true);
            }
            ic.commitText(String.valueOf(ch), 1);
            if (candidateStripView != null) {
                candidateStripView.setSuggestions(Collections.emptyList());
            }
        }
    }

    private void handleBackspace(InputConnection ic) {
        if (composingText.length() > 0) {
            composingText.deleteCharAt(composingText.length() - 1);
            if (composingText.length() > 0) {
                ic.setComposingText(composingText.toString(), 1);
                updateSuggestions();
            } else {
                ic.setComposingText("", 0);
                currentSuggestions = Collections.emptyList();
                if (candidateStripView != null) {
                    candidateStripView.setSuggestions(Collections.emptyList());
                }
            }
        } else {
            ic.deleteSurroundingText(1, 0);
        }
    }

    private void handleSpace(InputConnection ic) {
        long now = System.currentTimeMillis();
        if (composingText.length() > 0) {
            String typed = composingText.toString();
            String toCommit = typed;

            // Suggested word should take ONLY when it completes with correct spelling
            // Single character like 'h' must NEVER auto-complete to a suggested word on space
            if (typed.length() > 1 && preferences.isAutoCorrectEnabled() && !currentSuggestions.isEmpty()) {
                String topChoice = currentSuggestions.get(0);
                if ("EN".equalsIgnoreCase(currentLanguage)) {
                    if (EnglishDictionary.isCorrectWord(typed) || topChoice.equalsIgnoreCase(typed)) {
                        toCommit = topChoice;
                    }
                } else {
                    toCommit = topChoice;
                }
            }

            ic.commitText(toCommit + " ", 1);
            composingText.setLength(0);
            currentSuggestions = Collections.emptyList();
            if (candidateStripView != null) {
                candidateStripView.setSuggestions(Collections.emptyList());
            }
            lastSpaceTime = now;
        } else {
            if (preferences.isDoubleSpacePeriodEnabled() && (now - lastSpaceTime < 500)) {
                CharSequence before = ic.getTextBeforeCursor(1, 0);
                if (before != null && before.length() > 0 && before.charAt(before.length() - 1) == ' ') {
                    ic.deleteSurroundingText(1, 0);
                    ic.commitText(". ", 1);
                    lastSpaceTime = 0;
                    return;
                }
            }
            ic.commitText(" ", 1);
            lastSpaceTime = now;
        }
    }

    private void handleEnter(InputConnection ic) {
        if (composingText.length() > 0) {
            commitComposingText(ic, false);
        }
        EditorInfo info = getCurrentInputEditorInfo();
        if (info != null && (info.imeOptions & EditorInfo.IME_FLAG_NO_ENTER_ACTION) == 0) {
            int action = info.imeOptions & EditorInfo.IME_MASK_ACTION;
            if (action != EditorInfo.IME_ACTION_NONE && action != EditorInfo.IME_ACTION_UNSPECIFIED) {
                ic.performEditorAction(action);
                return;
            }
        }
        sendKeyChar('\n');
    }

    private void commitComposingText(InputConnection ic, boolean appendSpace) {
        if (composingText.length() == 0) return;

        String typed = composingText.toString();
        String toCommit = typed;
        if (typed.length() > 1 && !currentSuggestions.isEmpty()) {
            String topChoice = currentSuggestions.get(0);
            if ("EN".equalsIgnoreCase(currentLanguage)) {
                if (EnglishDictionary.isCorrectWord(typed) || topChoice.equalsIgnoreCase(typed)) {
                    toCommit = topChoice;
                }
            } else {
                toCommit = topChoice;
            }
        }

        ic.commitText(appendSpace ? (toCommit + " ") : toCommit, 1);
        composingText.setLength(0);
        currentSuggestions = Collections.emptyList();
        if (candidateStripView != null) {
            candidateStripView.setSuggestions(Collections.emptyList());
        }
    }

    private void updateSuggestions() {
        if (isPasswordField || !preferences.isWordSuggestionsEnabled() || composingText.length() == 0) {
            currentSuggestions = Collections.emptyList();
            if (candidateStripView != null) {
                candidateStripView.setSuggestions(Collections.emptyList());
            }
            return;
        }

        String query = composingText.toString();
        currentSuggestions = com.amitbharat.keyboard.engine.IndicTransliterator.getSuggestions(currentLanguage, query);

        if (candidateStripView != null) {
            candidateStripView.setSuggestions(currentSuggestions);
        }
    }

    @Override
    public void onCandidateClicked(String candidate) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;

        ic.commitText(candidate + " ", 1);
        composingText.setLength(0);
        currentSuggestions = Collections.emptyList();
        if (candidateStripView != null) {
            candidateStripView.setSuggestions(Collections.emptyList());
        }
    }

    @Override
    public void onText(CharSequence text) {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null && text != null) {
            if (composingText.length() > 0) {
                commitComposingText(ic, false);
            }
            ic.commitText(text, 1);
        }
    }

    @Override
    public void onEmojiSelected(String emoji) {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            if (composingText.length() > 0) {
                commitComposingText(ic, false);
            }
            ic.commitText(emoji, 1);
        }
    }

    @Override
    public void onGifSelected(String title, String content) {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            if (composingText.length() > 0) {
                commitComposingText(ic, false);
            }
            ic.commitText(content + " ", 1);
        }
    }

    @Override
    public void onBackToAlpha() {
        showSoftKeyboard();
    }

    @Override
    public void onBackspace() {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            ic.deleteSurroundingText(1, 0);
        }
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }

    private void showSoftKeyboard() {
        if (emojiKeyboardView != null) emojiKeyboardView.setVisibility(View.GONE);
        boolean showStrip = preferences.isSuggestionStripEnabled();
        if (candidateStripView != null) {
            candidateStripView.setVisibility(showStrip ? View.VISIBLE : View.GONE);
        }
        if (keyboardContainer != null) {
            ViewGroup.LayoutParams lp = keyboardContainer.getLayoutParams();
            if (lp != null) {
                lp.height = dpToPx(showStrip ? 276 : 320);
                keyboardContainer.setLayoutParams(lp);
            }
        }
        if (softKeyboardView != null) softKeyboardView.setVisibility(View.VISIBLE);
    }

    private void showEmojiKeyboard() {
        if (candidateStripView != null) candidateStripView.setVisibility(View.GONE);
        if (softKeyboardView != null) softKeyboardView.setVisibility(View.GONE);
        if (keyboardContainer != null) {
            ViewGroup.LayoutParams lp = keyboardContainer.getLayoutParams();
            if (lp != null) {
                lp.height = dpToPx(320);
                keyboardContainer.setLayoutParams(lp);
            }
        }
        if (emojiKeyboardView != null) {
            emojiKeyboardView.updateTheme();
            emojiKeyboardView.setVisibility(View.VISIBLE);
        }
    }

    private void handleVoiceInput() {
        if (isPasswordField) {
            android.widget.Toast.makeText(this, "Voice typing is disabled in password fields", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(this, VoicePermissionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        if (isVoiceListening) {
            stopVoiceInput();
            return;
        }

        startVoiceInput();
    }

    private void startVoiceInput() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            android.widget.Toast.makeText(this, "Speech recognition is not available", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        stopVoiceInput();

        try {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    isVoiceListening = true;
                    if (candidateStripView != null) {
                        candidateStripView.setVoiceListening(true);
                        String langPrompt = "EN".equalsIgnoreCase(currentLanguage)
                                ? "Listening... Speak in English"
                                : "सुन रहे हैं... हिन्दी में बोलें";
                        candidateStripView.showVoiceStatus(langPrompt);
                    }
                }

                @Override
                public void onBeginningOfSpeech() {
                }

                @Override
                public void onRmsChanged(float rmsdB) {
                }

                @Override
                public void onBufferReceived(byte[] buffer) {
                }

                @Override
                public void onEndOfSpeech() {
                    if (candidateStripView != null) {
                        candidateStripView.showVoiceStatus("Processing...");
                    }
                }

                @Override
                public void onError(int error) {
                    android.util.Log.w("IndicKeyboard", "SpeechRecognizer error code: " + error);
                    stopVoiceListeningState();
                }

                @Override
                public void onResults(Bundle results) {
                    if (results != null) {
                        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                        if (matches != null && !matches.isEmpty()) {
                            commitSpokenSentence(matches.get(0));
                        }
                    }
                    stopVoiceListeningState();
                }

                @Override
                public void onPartialResults(Bundle partialResults) {
                    if (partialResults != null) {
                        ArrayList<String> partial = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                        if (partial != null && !partial.isEmpty() && candidateStripView != null) {
                            candidateStripView.showVoiceStatus(partial.get(0));
                        }
                    }
                }

                @Override
                public void onEvent(int eventType, Bundle params) {
                }
            });

            String speechLocale = getSpeechLocale(currentLanguage);
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, speechLocale);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, speechLocale);
            intent.putExtra("android.speech.extra.EXTRA_ADDITIONAL_LANGUAGES", new String[]{speechLocale});
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

            isVoiceListening = true;
            if (candidateStripView != null) {
                candidateStripView.setVoiceListening(true);
                String initPrompt = "EN".equalsIgnoreCase(currentLanguage)
                        ? "Starting English Voice..."
                        : "हिन्दी आवाज़ शुरू हो रही है...";
                candidateStripView.showVoiceStatus(initPrompt);
            }

            speechRecognizer.startListening(intent);
        } catch (Exception e) {
            android.util.Log.e("IndicKeyboard", "Failed to start speech recognition", e);
            stopVoiceListeningState();
        }
    }

    private String getSpeechLocale(String lang) {
        if ("EN".equalsIgnoreCase(lang)) {
            return "en-IN"; // English
        } else if ("HN".equalsIgnoreCase(lang)) {
            return "hi-IN"; // Hindi
        } else if ("MR".equalsIgnoreCase(lang)) {
            return "mr-IN";
        } else if ("BN".equalsIgnoreCase(lang)) {
            return "bn-IN";
        } else if ("TE".equalsIgnoreCase(lang)) {
            return "te-IN";
        } else if ("TA".equalsIgnoreCase(lang)) {
            return "ta-IN";
        } else if ("GU".equalsIgnoreCase(lang)) {
            return "gu-IN";
        } else if ("KN".equalsIgnoreCase(lang)) {
            return "kn-IN";
        } else if ("ML".equalsIgnoreCase(lang)) {
            return "ml-IN";
        } else if ("PA".equalsIgnoreCase(lang)) {
            return "pa-IN";
        } else if ("UR".equalsIgnoreCase(lang)) {
            return "ur-IN";
        } else {
            return "hi-IN";
        }
    }

    private void commitSpokenSentence(String sentence) {
        if (sentence == null || sentence.trim().isEmpty()) return;
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;

        if (composingText.length() > 0) {
            ic.commitText(composingText.toString() + " ", 1);
            composingText.setLength(0);
        }

        CharSequence before = ic.getTextBeforeCursor(1, 0);
        StringBuilder sb = new StringBuilder();
        if (before != null && before.length() > 0 && !Character.isWhitespace(before.charAt(before.length() - 1))) {
            sb.append(" ");
        }
        sb.append(sentence.trim()).append(" ");
        ic.commitText(sb.toString(), 1);
    }

    private void stopVoiceInput() {
        if (speechRecognizer != null) {
            try {
                speechRecognizer.stopListening();
                speechRecognizer.cancel();
                speechRecognizer.destroy();
            } catch (Exception ignored) {
            }
            speechRecognizer = null;
        }
        stopVoiceListeningState();
    }

    private void stopVoiceListeningState() {
        isVoiceListening = false;
        if (candidateStripView != null) {
            candidateStripView.setVoiceListening(false);
            candidateStripView.setSuggestions(Collections.emptyList());
        }
    }
}
