package com.amitbharat.keyboard.engine;

import java.util.ArrayList;
import java.util.List;

public class LanguageItem {

    private final String code;
    private final String englishName;
    private final String nativeName;

    public LanguageItem(String code, String englishName, String nativeName) {
        this.code = code;
        this.englishName = englishName;
        this.nativeName = nativeName;
    }

    public String getCode() {
        return code;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getNativeName() {
        return nativeName;
    }

    public String getDisplayName() {
        if (code.equals("EN")) {
            return englishName;
        }
        return englishName + " (" + nativeName + ")";
    }

    public static List<LanguageItem> getAllLanguages() {
        List<LanguageItem> list = new ArrayList<>();
        // Indian Currency Banknote Languages & Primary Languages
        list.add(new LanguageItem("HN", "Hindi", "हिन्दी"));
        list.add(new LanguageItem("EN", "English", "English"));
        list.add(new LanguageItem("BHO", "Bhojpuri", "भोजपुरी")); // Widely spoken native
        list.add(new LanguageItem("MR", "Marathi", "मराठी"));
        list.add(new LanguageItem("BN", "Bengali", "বাংলা"));
        list.add(new LanguageItem("TE", "Telugu", "తెలుగు"));
        list.add(new LanguageItem("TA", "Tamil", "தமிழ்"));
        list.add(new LanguageItem("GU", "Gujarati", "ગુજરાતી"));
        list.add(new LanguageItem("KN", "Kannada", "ಕನ್ನಡ"));
        list.add(new LanguageItem("ML", "Malayalam", "മലയാളം"));
        list.add(new LanguageItem("PA", "Punjabi", "ਪੰਜਾਬੀ"));
        list.add(new LanguageItem("OR", "Odia", "ଓଡ଼ିଆ"));
        list.add(new LanguageItem("AS", "Assamese", "অসমীয়া"));
        list.add(new LanguageItem("SA", "Sanskrit", "संस्कृतम्"));
        list.add(new LanguageItem("NE", "Nepali", "नेपाली"));
        list.add(new LanguageItem("KOK", "Konkani", "कोंकणी"));
        list.add(new LanguageItem("UR", "Urdu", "اردو"));
        list.add(new LanguageItem("KS", "Kashmiri", "कॉशुर"));
        list.add(new LanguageItem("MAI", "Maithili", "मैथिली"));
        list.add(new LanguageItem("RAJ", "Rajasthani", "राजस्थानी"));
        list.add(new LanguageItem("SD", "Sindhi", "सिंधी"));
        list.add(new LanguageItem("DOI", "Dogri", "डोगरी"));
        list.add(new LanguageItem("SAT", "Santali", "संताली"));
        return list;
    }

    public static LanguageItem findByCode(String code) {
        for (LanguageItem item : getAllLanguages()) {
            if (item.getCode().equalsIgnoreCase(code)) {
                return item;
            }
        }
        return new LanguageItem(code, code, code);
    }
}
