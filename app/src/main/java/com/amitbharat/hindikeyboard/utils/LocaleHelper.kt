package com.amitbharat.hindikeyboard.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import java.util.Locale

object LocaleHelper {

    private const val PREFS_NAME = "app_language_prefs"
    private const val KEY_LANGUAGE = "selected_language"

    fun isHindi(context: Context): Boolean {
        return getLanguage(context).startsWith("hi")
    }

    fun getLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANGUAGE, "en") ?: "en"
    }

    fun wrapContext(context: Context): Context {
        val lang = getLanguage(context)
        val locale = Locale(lang)
        Locale.setDefault(locale)

        val res = context.resources
        val config = Configuration(res.configuration)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            config.setLocales(localeList)
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
            @Suppress("DEPRECATION")
            res.updateConfiguration(config, res.displayMetrics)
            context
        }
    }

    fun applyAppLanguage(context: Context) {
        // wrapContext(Context) in attachBaseContext properly handles per-app locale
        // on Android N+ (API 24+) without triggering infinite activity recreation loops.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            val lang = getLanguage(context)
            val locale = Locale(lang)
            Locale.setDefault(locale)
            val res = context.resources
            val config = Configuration(res.configuration)
            @Suppress("DEPRECATION")
            config.locale = locale
            @Suppress("DEPRECATION")
            res.updateConfiguration(config, res.displayMetrics)
        }
    }

    fun setLocale(activity: Activity, languageTag: String) {
        val prefs = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, languageTag).apply()

        val locale = Locale(languageTag)
        Locale.setDefault(locale)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            val res = activity.resources
            val config = Configuration(res.configuration)
            @Suppress("DEPRECATION")
            config.locale = locale
            @Suppress("DEPRECATION")
            res.updateConfiguration(config, res.displayMetrics)
            @Suppress("DEPRECATION")
            activity.applicationContext.resources.updateConfiguration(config, activity.applicationContext.resources.displayMetrics)
        }

        try {
            androidx.appcompat.app.AppCompatDelegate.setApplicationLocales(
                androidx.core.os.LocaleListCompat.forLanguageTags(languageTag)
            )
        } catch (_: Exception) {
        }

        activity.recreate()
    }
}
