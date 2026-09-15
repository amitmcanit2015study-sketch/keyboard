package com.amitbharat.keyboard.engine;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeyboardPreferences {

    public static final String PREF_CURRENT_LANG = "pref_current_lang";
    public static final String PREF_SELECTED_LANGS = "pref_selected_langs";
    public static final String PREF_THEME = "pref_theme";
    public static final String PREF_SOUND = "pref_sound";
    public static final String PREF_VIBRATE = "pref_vibrate";
    public static final String PREF_POPUP = "pref_popup";
    public static final String PREF_AUTOCAP = "pref_autocap";

    // Keys & Layout
    public static final String PREF_NUMBER_ROW = "pref_number_row";
    public static final String PREF_EMOJI_KEY = "pref_emoji_key";
    public static final String PREF_LANG_KEY = "pref_lang_key";
    public static final String PREF_COMMA_KEY = "pref_comma_key";
    public static final String PREF_FULLSTOP_KEY = "pref_fullstop_key";
    public static final String PREF_SUGGESTION_STRIP = "pref_suggestion_strip";

    // Shortcuts & Corrections
    public static final String PREF_DOUBLE_SPACE_PERIOD = "pref_double_space_period";
    public static final String PREF_AUTO_CORRECT = "pref_auto_correct";
    public static final String PREF_WORD_SUGGESTIONS = "pref_word_suggestions";
    public static final String PREF_NEXT_WORD_SUGGESTIONS = "pref_next_word_suggestions";
    public static final String PREF_EMOJI_FAST_ROW = "pref_emoji_fast_row";

    public static final String LANG_HINDI = "HN";
    public static final String LANG_ENGLISH = "EN";

    public static final String THEME_SYSTEM = "system";

    private final SharedPreferences prefs;

    public KeyboardPreferences(Context context) {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public List<String> getSelectedLanguages() {
        String saved = prefs.getString(PREF_SELECTED_LANGS, "HN,EN");
        List<String> list = new ArrayList<>();
        if (saved != null && !saved.trim().isEmpty()) {
            String[] parts = saved.split(",");
            for (String p : parts) {
                String clean = p.trim();
                if (!clean.isEmpty() && !list.contains(clean)) {
                    list.add(clean);
                }
            }
        }
        if (list.isEmpty()) {
            list.add(LANG_HINDI);
            list.add(LANG_ENGLISH);
        }
        if (list.size() > 2) {
            list = list.subList(0, 2);
        }
        return list;
    }

    public void setSelectedLanguages(List<String> langs) {
        if (langs == null || langs.isEmpty()) {
            langs = Arrays.asList(LANG_HINDI, LANG_ENGLISH);
        } else if (langs.size() > 2) {
            langs = langs.subList(0, 2);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < langs.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(langs.get(i));
        }
        prefs.edit().putString(PREF_SELECTED_LANGS, sb.toString()).apply();

        String current = getCurrentLanguage();
        if (!langs.contains(current)) {
            setCurrentLanguage(langs.get(0));
        }
    }

    public String getCurrentLanguage() {
        String curr = prefs.getString(PREF_CURRENT_LANG, LANG_HINDI);
        List<String> selected = getSelectedLanguages();
        if (!selected.contains(curr)) {
            curr = selected.get(0);
            prefs.edit().putString(PREF_CURRENT_LANG, curr).apply();
        }
        return curr;
    }

    public void setCurrentLanguage(String lang) {
        prefs.edit().putString(PREF_CURRENT_LANG, lang).apply();
    }

    public String getAltLanguage() {
        List<String> selected = getSelectedLanguages();
        if (selected.size() < 2) {
            return null;
        }
        String current = getCurrentLanguage();
        if (selected.get(0).equalsIgnoreCase(current)) {
            return selected.get(1);
        } else {
            return selected.get(0);
        }
    }

    public String toggleLanguage() {
        List<String> selected = getSelectedLanguages();
        if (selected.size() == 1) {
            setCurrentLanguage(selected.get(0));
            return selected.get(0);
        }
        String current = getCurrentLanguage();
        String next;
        if (selected.get(0).equalsIgnoreCase(current)) {
            next = selected.get(1);
        } else {
            next = selected.get(0);
        }
        setCurrentLanguage(next);
        return next;
    }

    public String getTheme() {
        return THEME_SYSTEM;
    }

    public void setTheme(String theme) {
        prefs.edit().putString(PREF_THEME, THEME_SYSTEM).apply();
    }

    public boolean isSoundEnabled() {
        return prefs.getBoolean(PREF_SOUND, true);
    }

    public void setSoundEnabled(boolean enabled) {
        prefs.edit().putBoolean(PREF_SOUND, enabled).apply();
    }

    public boolean isVibrateEnabled() {
        return prefs.getBoolean(PREF_VIBRATE, true);
    }

    public void setVibrateEnabled(boolean enabled) {
        prefs.edit().putBoolean(PREF_VIBRATE, enabled).apply();
    }

    public boolean isPopupEnabled() {
        return prefs.getBoolean(PREF_POPUP, true);
    }

    public void setPopupEnabled(boolean enabled) {
        prefs.edit().putBoolean(PREF_POPUP, enabled).apply();
    }

    public boolean isAutoCapEnabled() {
        return prefs.getBoolean(PREF_AUTOCAP, true);
    }

    public void setAutoCapEnabled(boolean enabled) {
        prefs.edit().putBoolean(PREF_AUTOCAP, enabled).apply();
    }

    // Number row
    public boolean isNumberRowEnabled() {
        return prefs.getBoolean(PREF_NUMBER_ROW, true);
    }

    public void setNumberRowEnabled(boolean enabled) {
        prefs.edit().putBoolean(PREF_NUMBER_ROW, enabled).apply();
    }

    // Emoji key vs Language switch key
    public boolean isEmojiKeyEnabled() {
        return prefs.getBoolean(PREF_EMOJI_KEY, true);
    }

    public void setEmojiKeyEnabled(boolean enabled) {
        prefs.edit().putBoolean(PREF_EMOJI_KEY, enabled).apply();
    }

    public boolean isLanguageKeyEnabled() {
        return prefs.getBoolean(PREF_LANG_KEY, false);
    }

    public void setLanguageKeyEnabled(boolean enabled) {
        prefs.edit().putBoolean(PREF_LANG_KEY, enabled).apply();
    }

    // Comma key
    public boolean isCommaKeyEnabled() {
        return prefs.getBoolean(PREF_COMMA_KEY, true);
    }

    public void setCommaKeyEnabled(boolean enabled) {
        prefs.edit().putBoolean(PREF_COMMA_KEY, enabled).apply();
    }

    // Full stop key
    public boolean isFullStopKeyEnabled() {
        return prefs.getBoolean(PREF_FULLSTOP_KEY, true);
    }

    public void setFullStopKeyEnabled(boolean enabled) {
        prefs.edit().putBoolean(PREF_FULLSTOP_KEY, enabled).apply();
    }

    // Suggestion strip
    public boolean isSuggestionStripEnabled() {
        return prefs.getBoolean(PREF_SUGGESTION_STRIP, true);
    }

    public void setSuggestionStripEnabled(boolean enabled) {
        prefs.edit().putBoolean(PREF_SUGGESTION_STRIP, enabled).apply();
    }

    // Double-space period
    public boolean isDoubleSpacePeriodEnabled() {
        return prefs.getBoolean(PREF_DOUBLE_SPACE_PERIOD, true);
    }

    public void setDoubleSpacePeriodEnabled(boolean enabled) {
        prefs.edit().putBoolean(PREF_DOUBLE_SPACE_PERIOD, enabled).apply();
    }

    // Auto-correct
    public boolean isAutoCorrectEnabled() {
        return prefs.getBoolean(PREF_AUTO_CORRECT, true);
    }

    public void setAutoCorrectEnabled(boolean enabled) {
        prefs.edit().putBoolean(PREF_AUTO_CORRECT, enabled).apply();
    }

    // Word suggestions
    public boolean isWordSuggestionsEnabled() {
        return prefs.getBoolean(PREF_WORD_SUGGESTIONS, true);
    }

    public void setWordSuggestionsEnabled(boolean enabled) {
        prefs.edit().putBoolean(PREF_WORD_SUGGESTIONS, enabled).apply();
    }

    // Next-word suggestions
    public boolean isNextWordSuggestionsEnabled() {
        return prefs.getBoolean(PREF_NEXT_WORD_SUGGESTIONS, true);
    }

    public void setNextWordSuggestionsEnabled(boolean enabled) {
        prefs.edit().putBoolean(PREF_NEXT_WORD_SUGGESTIONS, enabled).apply();
    }

    // Emoji fast-access row
    public boolean isEmojiFastRowEnabled() {
        return prefs.getBoolean(PREF_EMOJI_FAST_ROW, false);
    }

    public void setEmojiFastRowEnabled(boolean enabled) {
        prefs.edit().putBoolean(PREF_EMOJI_FAST_ROW, enabled).apply();
    }
}
