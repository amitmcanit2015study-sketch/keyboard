package com.amitbharat.hindikeyboard.utils

import android.content.Context
import android.content.SharedPreferences
import com.amitbharat.hindikeyboard.theme.ThemeType

class PreferencesManager private constructor(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences("indic_keyboard_prefs", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var instance: PreferencesManager? = null

        fun getInstance(context: Context): PreferencesManager {
            return instance ?: synchronized(this) {
                instance ?: PreferencesManager(context).also { instance = it }
            }
        }
    }

    // App Theme: 0 = System Default, 1 = Light, 2 = Dark
    fun getAppThemeMode(): Int = prefs.getInt("app_theme_mode", 0)
    fun setAppThemeMode(mode: Int) {
        prefs.edit().putInt("app_theme_mode", mode).apply()
    }

    // Keyboard Visual Theme
    fun getKeyboardTheme(): ThemeType {
        val themeName = prefs.getString("keyboard_theme", ThemeType.SYSTEM_DEFAULT.name)
        return try {
            ThemeType.valueOf(themeName ?: ThemeType.SYSTEM_DEFAULT.name)
        } catch (e: Exception) {
            ThemeType.SYSTEM_DEFAULT
        }
    }
    fun setKeyboardTheme(theme: ThemeType) {
        prefs.edit().putString("keyboard_theme", theme.name).apply()
    }

    // Feature Toggles
    fun isNumberRowEnabled(): Boolean = prefs.getBoolean("pref_number_row", true)
    fun setNumberRowEnabled(enabled: Boolean) = prefs.edit().putBoolean("pref_number_row", enabled).apply()

    fun isVibrationEnabled(): Boolean = prefs.getBoolean("pref_vibrate", true)
    fun setVibrationEnabled(enabled: Boolean) = prefs.edit().putBoolean("pref_vibrate", enabled).apply()

    fun isSoundEnabled(): Boolean = prefs.getBoolean("pref_sound", false)
    fun setSoundEnabled(enabled: Boolean) = prefs.edit().putBoolean("pref_sound", enabled).apply()

    fun isAutoCorrectEnabled(): Boolean = prefs.getBoolean("pref_auto_correct", true)
    fun setAutoCorrectEnabled(enabled: Boolean) = prefs.edit().putBoolean("pref_auto_correct", enabled).apply()

    fun isEmojiSuggestionsEnabled(): Boolean = prefs.getBoolean("pref_emoji_suggestions", true)
    fun setEmojiSuggestionsEnabled(enabled: Boolean) = prefs.edit().putBoolean("pref_emoji_suggestions", enabled).apply()

    fun isClipboardEnabled(): Boolean = prefs.getBoolean("pref_clipboard", true)
    fun setClipboardEnabled(enabled: Boolean) = prefs.edit().putBoolean("pref_clipboard", enabled).apply()
}
