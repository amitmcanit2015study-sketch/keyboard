package com.amitbharat.keyboard.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import androidx.preference.PreferenceManager;
import java.util.Locale;

public class LocaleHelper {

    private static final String PREF_LOCALE = "pref_app_locale";

    public static Context wrapContext(Context context) {
        String lang = getLanguage(context);
        return updateResources(context, lang);
    }

    public static String getLanguage(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(PREF_LOCALE, Locale.getDefault().getLanguage());
    }

    public static boolean isHindi(Context context) {
        return "hi".equalsIgnoreCase(getLanguage(context));
    }

    public static void setLocale(Context context, String language) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(PREF_LOCALE, language).commit();

        updateResources(context, language);

        androidx.appcompat.app.AppCompatDelegate.setApplicationLocales(
                androidx.core.os.LocaleListCompat.forLanguageTags(language)
        );

        if (context instanceof android.app.Activity) {
            ((android.app.Activity) context).recreate();
        }
    }

    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration configuration = new Configuration(resources.getConfiguration());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocales(new LocaleList(locale));
            return context.createConfigurationContext(configuration);
        } else {
            configuration.locale = locale;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            return context;
        }
    }
}
