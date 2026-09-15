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
    private static final String PREF_THEME = "pref_theme";
    private static final String PREF_SOUND = "pref_sound";
    private static final String PREF_VIBRATE = "pref_vibrate";
    private static final String PREF_POPUP = "pref_popup";
    private static final String PREF_AUTOCAP = "pref_autocap";

    public static final String LANG_HINDI = "HN";
    public static final String LANG_ENGLISH = "EN";

    public static final String THEME_SYSTEM = "system";
    public static final String THEME_LIGHT = "light";
    public static final String THEME_DARK = "dark";
    public static final String THEME_BLUE = "blue";
    public static final String THEME_PURPLE = "purple";
    public static final String THEME_GREEN = "green";
    public static final String THEME_AMOLED = "amoled";

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
        // Enforce maximum 2
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

        // If current language is no longer in selected languages, set to first selected
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
        // Keyboard theme is permanently by default system
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
}
