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
        list.add(new LanguageItem("HN", "Hindi", "हिन्दी"));
        list.add(new LanguageItem("EN", "English", "English"));
        list.add(new LanguageItem("MR", "Marathi", "मराठी"));
        list.add(new LanguageItem("BN", "Bengali", "বাংলা"));
        list.add(new LanguageItem("TE", "Telugu", "తెలుగు"));
        list.add(new LanguageItem("TA", "Tamil", "தமிழ்"));
        list.add(new LanguageItem("GU", "Gujarati", "ગુજરાતી"));
        list.add(new LanguageItem("KN", "Kannada", "ಕನ್ನಡ"));
        list.add(new LanguageItem("ML", "Malayalam", "മലയാളം"));
        list.add(new LanguageItem("PA", "Punjabi", "ਪੰਜਾਬੀ"));
        list.add(new LanguageItem("OR", "Odia", "ଓଡ଼ିଆ"));
        list.add(new LanguageItem("UR", "Urdu", "اردو"));
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
