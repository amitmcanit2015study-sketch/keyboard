package com.amitbharat.keyboard.ime;

import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.FrameLayout;
import com.amitbharat.keyboard.R;
import com.amitbharat.keyboard.engine.EnglishDictionary;
import com.amitbharat.keyboard.engine.HinglishTransliterator;
import com.amitbharat.keyboard.engine.KeyboardPreferences;
import com.amitbharat.keyboard.ui.MainActivity;
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

    private android.content.SharedPreferences sharedPreferences;
    private final android.content.SharedPreferences.OnSharedPreferenceChangeListener prefListener =
            (sp, key) -> {
                if ("pref_theme".equals(key)) {
                    if (softKeyboardView != null) {
                        softKeyboardView.post(() -> softKeyboardView.resolveThemeColors());
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
        if (sharedPreferences != null) {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(prefListener);
        }
    }

    @Override
    public boolean onEvaluateInputViewShown() {
        return true;
    }

    @Override
    public View onCreateInputView() {
        android.util.Log.d("IndicKeyboard", "onCreateInputView called");
        androidx.appcompat.view.ContextThemeWrapper themedContext =
                new androidx.appcompat.view.ContextThemeWrapper(this, R.style.Theme_IndicKeyboard);
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(themedContext);
        rootView = inflater.inflate(R.layout.keyboard_root_view, null);

        candidateStripView = rootView.findViewById(R.id.candidateStripView);
        softKeyboardView = rootView.findViewById(R.id.softKeyboardView);
        emojiKeyboardView = rootView.findViewById(R.id.emojiKeyboardView);
        keyboardContainer = rootView.findViewById(R.id.keyboardContainer);

        candidateStripView.setLanguage(currentLanguage);
        candidateStripView.setOnCandidateClickListener(this);

        // 1. Top-left language toggle listener (HN / EN)
        candidateStripView.setOnLanguageToggleListener(() -> {
            toggleLanguage();
        });

        // Quick settings shortcut
        candidateStripView.setOnSettingsClickListener(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        softKeyboardView.setOnKeyboardActionListener(this);
        emojiKeyboardView.setOnEmojiSelectedListener(this);

        return rootView;
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);

        composingText.setLength(0);
        currentSuggestions = Collections.emptyList();

        if (candidateStripView != null) {
            candidateStripView.setSuggestions(Collections.emptyList());
            candidateStripView.setLanguage(currentLanguage);
        }

        if (softKeyboardView != null) {
            softKeyboardView.resolveThemeColors();
            softKeyboardView.setMode(KeyboardLayoutHelper.MODE_ALPHA);

            // Check auto-capitalization if enabled
            if (preferences.isAutoCapEnabled() && info != null) {
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

    private void toggleLanguage() {
        if (KeyboardPreferences.LANG_HINDI.equals(currentLanguage)) {
            currentLanguage = KeyboardPreferences.LANG_ENGLISH;
        } else {
            currentLanguage = KeyboardPreferences.LANG_HINDI;
        }
        preferences.setCurrentLanguage(currentLanguage);
        if (candidateStripView != null) {
            candidateStripView.setLanguage(currentLanguage);
        }
        // Update suggestions if text is being composed
        updateSuggestions();
    }

    @Override
    public void onKey(int primaryCode, KeyboardKey key) {
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
        if (composingText.length() > 0) {
            // Commit top candidate or composing text
            if (!currentSuggestions.isEmpty()) {
                String topChoice = currentSuggestions.get(0);
                ic.commitText(topChoice + " ", 1);
            } else {
                ic.commitText(composingText.toString() + " ", 1);
            }
            composingText.setLength(0);
            currentSuggestions = Collections.emptyList();
            if (candidateStripView != null) {
                candidateStripView.setSuggestions(Collections.emptyList());
            }
        } else {
            ic.commitText(" ", 1);
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

        String toCommit;
        if (!currentSuggestions.isEmpty()) {
            toCommit = currentSuggestions.get(0);
        } else {
            toCommit = composingText.toString();
        }

        ic.commitText(appendSpace ? (toCommit + " ") : toCommit, 1);
        composingText.setLength(0);
        currentSuggestions = Collections.emptyList();
        if (candidateStripView != null) {
            candidateStripView.setSuggestions(Collections.emptyList());
        }
    }

    private void updateSuggestions() {
        if (composingText.length() == 0) {
            currentSuggestions = Collections.emptyList();
            if (candidateStripView != null) {
                candidateStripView.setSuggestions(Collections.emptyList());
            }
            return;
        }

        String query = composingText.toString();
        if (KeyboardPreferences.LANG_HINDI.equals(currentLanguage)) {
            // Hinglish -> Hindi Transliteration suggestions
            currentSuggestions = HinglishTransliterator.getSuggestions(query);
        } else {
            // English word autocomplete & spell suggestions
            currentSuggestions = EnglishDictionary.getSuggestions(query);
        }

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

    private void showSoftKeyboard() {
        if (emojiKeyboardView != null) emojiKeyboardView.setVisibility(View.GONE);
        if (softKeyboardView != null) softKeyboardView.setVisibility(View.VISIBLE);
    }

    private void showEmojiKeyboard() {
        if (softKeyboardView != null) softKeyboardView.setVisibility(View.GONE);
        if (emojiKeyboardView != null) emojiKeyboardView.setVisibility(View.VISIBLE);
    }
}
