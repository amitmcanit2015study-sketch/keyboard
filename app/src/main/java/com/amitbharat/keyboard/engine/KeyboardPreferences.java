package com.amitbharat.keyboard.engine;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

public class KeyboardPreferences {

    private static final String PREF_CURRENT_LANG = "pref_current_lang";
    private static final String PREF_DEFAULT_LANG = "pref_default_lang";
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

    public String getCurrentLanguage() {
        return prefs.getString(PREF_CURRENT_LANG, LANG_HINDI);
    }

    public void setCurrentLanguage(String lang) {
        prefs.edit().putString(PREF_CURRENT_LANG, lang).apply();
    }

    public String getDefaultLanguage() {
        return prefs.getString(PREF_DEFAULT_LANG, LANG_HINDI);
    }

    public void setDefaultLanguage(String lang) {
        prefs.edit().putString(PREF_DEFAULT_LANG, lang).apply();
    }

    public String getTheme() {
        return prefs.getString(PREF_THEME, THEME_SYSTEM);
    }

    public void setTheme(String theme) {
        prefs.edit().putString(PREF_THEME, theme).apply();
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
