package com.amitbharat.keyboard.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Universal Indic transliteration engine supporting major Indian languages:
 * Hindi, Marathi, Bengali, Telugu, Tamil, Gujarati, Kannada, Malayalam, Punjabi, Odia, and Urdu.
 */
public class IndicTransliterator {

    private static final Map<String, Map<String, List<String>>> LANG_DICTS = new HashMap<>();

    static {
        initMarathiDict();
        initBengaliDict();
        initTeluguDict();
        initTamilDict();
        initGujaratiDict();
        initKannadaDict();
        initMalayalamDict();
        initPunjabiDict();
        initOdiaDict();
        initUrduDict();
    }

    private static void addWord(String lang, String roman, String... candidates) {
        Map<String, List<String>> dict = LANG_DICTS.computeIfAbsent(lang, k -> new HashMap<>());
        List<String> list = new ArrayList<>();
        Collections.addAll(list, candidates);
        dict.put(roman.toLowerCase(Locale.ROOT), list);
    }

    private static void initMarathiDict() {
        String L = "MR";
        addWord(L, "namaskar", "नमस्कार", "नमस्कारम");
        addWord(L, "kasa", "कसा", "कसा काय");
        addWord(L, "kashi", "कशी");
        addWord(L, "kase", "कसे");
        addWord(L, "aahes", "आहेस");
        addWord(L, "aahe", "आहे", "आहेत");
        addWord(L, "aahet", "आहेत");
        addWord(L, "dhanyawad", "धन्यवाद");
        addWord(L, "dhanyavad", "धन्यवाद");
        addWord(L, "aabhar", "आभार");
        addWord(L, "mi", "मी");
        addWord(L, "tu", "तू");
        addWord(L, "tumhi", "तुम्ही");
        addWord(L, "amhi", "आम्ही");
        addWord(L, "aapn", "आपण");
        addWord(L, "aapan", "आपण");
        addWord(L, "kaay", "काय");
        addWord(L, "kay", "काय");
        addWord(L, "kuthe", "कुठे");
        addWord(L, "kadhi", "कधी");
        addWord(L, "kon", "कोण");
        addWord(L, "kashala", "कशाला");
        addWord(L, "ho", "हो", "होय");
        addWord(L, "nahi", "नाही");
        addWord(L, "chhan", "छान");
        addWord(L, "khoop", "खूप");
        addWord(L, "khup", "खूप");
        addWord(L, "shubh", "शुभ");
        addWord(L, "ratri", "रात्री");
        addWord(L, "prabhat", "प्रभात");
        addWord(L, "sakhar", "सकाळ");
        addWord(L, "sakal", "सकाळ");
        addWord(L, "jevlas", "जेवलास", "जेवलात");
        addWord(L, "jevan", "जेवण");
        addWord(L, "bar", "बरं");
        addWord(L, "bara", "बरा", "बरे");
    }

    private static void initBengaliDict() {
        String L = "BN";
        addWord(L, "namaskar", "নমস্কার");
        addWord(L, "nomoshkar", "নমস্কার");
        addWord(L, "kemon", "কেমন");
        addWord(L, "acho", "আছো");
        addWord(L, "achen", "আছেন");
        addWord(L, "dhonnobad", "ধন্যবাদ");
        addWord(L, "dhanyabad", "ধন্যবাদ");
        addWord(L, "bhalo", "ভালো");
        addWord(L, "ami", "আমি");
        addWord(L, "tumi", "তুমি");
        addWord(L, "aapni", "আপনি");
        addWord(L, "apni", "আপনি");
        addWord(L, "ki", "কি", "কী");
        addWord(L, "kothay", "কোথায়");
        addWord(L, "kokhon", "কখন");
        addWord(L, "ke", "কে");
        addWord(L, "keno", "কেন");
        addWord(L, "haan", "হ্যাঁ");
        addWord(L, "ha", "হ্যাঁ");
        addWord(L, "na", "না");
        addWord(L, "shuvo", "শুভ");
        addWord(L, "sokal", "সকাল");
        addWord(L, "ratri", "রাত্রি");
        addWord(L, "khub", "খুব");
        addWord(L, "khabar", "খাবার");
        addWord(L, "thik", "ঠিক");
        addWord(L, "shob", "সব");
        addWord(L, "bhalobashi", "ভালোবাসি");
    }

    private static void initTeluguDict() {
        String L = "TE";
        addWord(L, "namaskaram", "నమస్కారం");
        addWord(L, "namasthe", "నమస్తే");
        addWord(L, "ela", "ఎలా");
        addWord(L, "unnaru", "ఉన్నారు");
        addWord(L, "unnavu", "ఉన్నావు");
        addWord(L, "dhanyavadalu", "ధన్యవాదాలు");
        addWord(L, "nenu", "నేను");
        addWord(L, "meeru", "మీరు");
        addWord(L, "nuvvu", "నువ్వు");
        addWord(L, "manamu", "మనము");
        addWord(L, "enti", "ఏంటి");
        addWord(L, "emiti", "ఏమిటి");
        addWord(L, "ekkada", "ఎక్కడ");
        addWord(L, "eppudu", "ఎప్పుడు");
        addWord(L, "evaru", "ఎవరు");
        addWord(L, "enduku", "ఎందుకు");
        addWord(L, "avunu", "అవును");
        addWord(L, "kaadu", "కాదు");
        addWord(L, "ledu", "లేదు");
        addWord(L, "baagundi", "బాగుంది");
        addWord(L, "chala", "చాలా");
        addWord(L, "shubhodhayam", "శుభోదయం");
        addWord(L, "shubharathri", "శుభరాత్రి");
        addWord(L, "thinnara", "తిన్నారా");
        addWord(L, "annam", "అన్నం");
    }

    private static void initTamilDict() {
        String L = "TA";
        addWord(L, "vanakkam", "வணக்கம்");
        addWord(L, "eppadi", "எப்படி");
        addWord(L, "irukkinga", "இருக்கிறீர்கள்");
        addWord(L, "irukka", "இருக்கிறாயா");
        addWord(L, "nandri", "நன்றி");
        addWord(L, "naan", "நான்");
        addWord(L, "neengal", "நீங்கள்");
        addWord(L, "nee", "நீ");
        addWord(L, "naangal", "நாங்கள்");
        addWord(L, "enna", "என்ன");
        addWord(L, "enge", "எங்கே");
        addWord(L, "yeppo", "எப்போது");
        addWord(L, "yaaru", "யார்");
        addWord(L, "yean", "ஏன்");
        addWord(L, "aam", "ஆம்");
        addWord(L, "illai", "இல்லை");
        addWord(L, "nalla", "நல்ல");
        addWord(L, "romba", "ரொம்ப");
        addWord(L, "kaalai", "காலை");
        addWord(L, "iravu", "இரவு");
        addWord(L, "saapttacha", "சாப்பிட்டாச்சா");
        addWord(L, "unavu", "உணவு");
    }

    private static void initGujaratiDict() {
        String L = "GU";
        addWord(L, "namaste", "નમસ્તે");
        addWord(L, "namaskar", "નમસ્કાર");
        addWord(L, "kem", "કેમ");
        addWord(L, "cho", "છો");
        addWord(L, "aabhar", "આભાર");
        addWord(L, "dhanyavad", "ધન્યવાદ");
        addWord(L, "hun", "હું");
        addWord(L, "tame", "તમે");
        addWord(L, "tu", "તું");
        addWord(L, "ame", "અમે");
        addWord(L, "shu", "શું");
        addWord(L, "kyan", "ક્યાં");
        addWord(L, "kyare", "ક્યારે");
        addWord(L, "kon", "કોણ");
        addWord(L, "kemne", "કેમ");
        addWord(L, "haan", "હા");
        addWord(L, "na", "ના");
        addWord(L, "saras", "સરસ");
        addWord(L, "bahu", "બહુ");
        addWord(L, "shubh", "શુભ");
        addWord(L, "ratri", "રાત્રિ");
        addWord(L, "prabhat", "પ્રભાત");
        addWord(L, "jamya", "જમ્યા");
        addWord(L, "maza", "મજા");
    }

    private static void initKannadaDict() {
        String L = "KN";
        addWord(L, "namaskara", "ನಮಸ್ಕಾರ");
        addWord(L, "hegiddira", "ಹೇಗಿದ್ದೀರಾ");
        addWord(L, "hegidya", "ಹೇಗಿದ್ದೀಯಾ");
        addWord(L, "dhanyavada", "ಧನ್ಯವಾದ");
        addWord(L, "naanu", "ನಾನು");
        addWord(L, "neevu", "ನೀವು");
        addWord(L, "neenu", "ನೀನು");
        addWord(L, "naavu", "ನಾವು");
        addWord(L, "yenu", "ಏನು");
        addWord(L, "yelli", "ಎಲ್ಲಿ");
        addWord(L, "yaavaga", "ಯಾವಾಗ");
        addWord(L, "yaaru", "ಯಾರು");
        addWord(L, "yaake", "ಯಾಕೆ");
        addWord(L, "haudu", "ಹೌದು");
        addWord(L, "illa", "ಇಲ್ಲ");
        addWord(L, "chennagide", "ಚೆನ್ನಾಗಿದೆ");
        addWord(L, "tumba", "ತುಂಬಾ");
        addWord(L, "shubhadina", "ಶುಭದಿನ");
        addWord(L, "oota", "ಊಟ");
    }

    private static void initMalayalamDict() {
        String L = "ML";
        addWord(L, "namaskaram", "നമസ്കാരം");
        addWord(L, "sukhamano", "സുഖമാണോ");
        addWord(L, "nanni", "നന്ദി");
        addWord(L, "njan", "ഞാൻ");
        addWord(L, "ningal", "നിങ്ങൾ");
        addWord(L, "nee", "നീ");
        addWord(L, "nammal", "നമ്മൾ");
        addWord(L, "enthu", "എന്ത്");
        addWord(L, "evide", "എവിടെ");
        addWord(L, "eppol", "എപ്പോൾ");
        addWord(L, "aaru", "ആര്");
        addWord(L, "enthukondu", "എന്തുകൊണ്ട്");
        addWord(L, "athe", "അതെ");
        addWord(L, "alla", "അല്ല");
        addWord(L, "nannayi", "നന്നായി");
        addWord(L, "valare", "വളരെ");
        addWord(L, "shubhadinam", "ശുഭദിനം");
        addWord(L, "kazhicho", "കഴിച്ചോ");
    }

    private static void initPunjabiDict() {
        String L = "PA";
        addWord(L, "satshriakal", "ਸਤਿ ਸ਼੍ਰੀ ਅਕਾਲ");
        addWord(L, "sat", "ਸਤਿ");
        addWord(L, "shri", "ਸ਼੍ਰੀ");
        addWord(L, "akal", "ਅਕਾਲ");
        addWord(L, "ki", "ਕੀ");
        addWord(L, "haal", "ਹਾਲ");
        addWord(L, "hai", "ਹੈ");
        addWord(L, "dhannwad", "ਧੰਨਵਾਦ");
        addWord(L, "main", "ਮੈਂ");
        addWord(L, "tussi", "ਤੁਸੀਂ");
        addWord(L, "tu", "ਤੂੰ");
        addWord(L, "asi", "ਅਸੀਂ");
        addWord(L, "kithe", "ਕਿੱਥੇ");
        addWord(L, "kadon", "ਕਦੋਂ");
        addWord(L, "kaun", "ਕੌਣ");
        addWord(L, "kyun", "ਕਿਉਂ");
        addWord(L, "haan", "ਹਾਂ");
        addWord(L, "nahi", "ਨਹੀਂ");
        addWord(L, "vadiya", "ਵਧੀਆ");
        addWord(L, "bohat", "ਬਹੁਤ");
        addWord(L, "roti", "ਰੋਟੀ");
        addWord(L, "khao", "ਖਾਓ");
    }

    private static void initOdiaDict() {
        String L = "OR";
        addWord(L, "namaskar", "ନମସ୍କାର");
        addWord(L, "kemiti", "କେମିତି");
        addWord(L, "achanti", "ଅଛନ୍ତି");
        addWord(L, "achu", "ଅଛୁ");
        addWord(L, "dhanyabad", "ଧନ୍ୟବାଦ");
        addWord(L, "mu", "ମୁଁ");
        addWord(L, "tame", "ତମେ");
        addWord(L, "aapan", "ଆପଣ");
        addWord(L, "ame", "ଆମେ");
        addWord(L, "kana", "କଣ");
        addWord(L, "kouthi", "କେଉଁଠି");
        addWord(L, "ketebele", "କେତେବେଳେ");
        addWord(L, "kie", "କିଏ");
        addWord(L, "kahinki", "କାହିଁକି");
        addWord(L, "han", "ହଁ");
        addWord(L, "na", "ନା");
        addWord(L, "bhala", "ଭଲ");
        addWord(L, "bahut", "ବହୁତ");
        addWord(L, "khaiba", "ଖାଇବା");
    }

    private static void initUrduDict() {
        String L = "UR";
        addWord(L, "salam", "سلام", "السلام علیکم");
        addWord(L, "assalam", "السلام علیکم");
        addWord(L, "kaise", "کیسے");
        addWord(L, "kese", "کیسے");
        addWord(L, "hain", "ہیں");
        addWord(L, "shukriya", "شکریہ");
        addWord(L, "main", "میں");
        addWord(L, "aap", "آپ");
        addWord(L, "tum", "تم");
        addWord(L, "hum", "ہم");
        addWord(L, "kya", "کیا");
        addWord(L, "kahan", "کہاں");
        addWord(L, "kab", "کب");
        addWord(L, "kaun", "کون");
        addWord(L, "kyun", "کیوں");
        addWord(L, "haan", "ہاں");
        addWord(L, "nahi", "نہیں");
        addWord(L, "theek", "ٹھیک");
        addWord(L, "bahut", "بہت");
        addWord(L, "khana", "کھانا");
    }

    public static List<String> getSuggestions(String lang, String query) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String q = query.trim().toLowerCase(Locale.ROOT);

        // Hindi uses HinglishTransliterator
        if ("HN".equalsIgnoreCase(lang)) {
            return HinglishTransliterator.getSuggestions(q);
        }

        // English uses EnglishDictionary
        if ("EN".equalsIgnoreCase(lang)) {
            return EnglishDictionary.getSuggestions(q);
        }

        List<String> results = new ArrayList<>();

        // Check language dictionary
        Map<String, List<String>> dict = LANG_DICTS.get(lang.toUpperCase(Locale.ROOT));
        if (dict != null) {
            List<String> matched = dict.get(q);
            if (matched != null && !matched.isEmpty()) {
                results.addAll(matched);
            }
        }

        // For Marathi, check Hindi dict as well since it shares Devanagari
        if ("MR".equalsIgnoreCase(lang)) {
            List<String> devanagariSuggestions = HinglishTransliterator.getSuggestions(q);
            for (String s : devanagariSuggestions) {
                if (!results.contains(s)) {
                    results.add(s);
                }
            }
        } else if (!"UR".equalsIgnoreCase(lang)) {
            // General Brahmic script transliteration via Devanagari offset conversion
            List<String> hindiBase = HinglishTransliterator.getSuggestions(q);
            for (String hWord : hindiBase) {
                String converted = convertDevanagariToScript(hWord, lang);
                if (converted != null && !converted.isEmpty() && !results.contains(converted)) {
                    results.add(converted);
                }
            }
        }

        // Add original input as fallback candidate
        if (!results.contains(q)) {
            results.add(q);
        }

        return results;
    }

    private static String convertDevanagariToScript(String devanagariText, String targetLang) {
        int offset = 0;
        switch (targetLang.toUpperCase(Locale.ROOT)) {
            case "BN": // Bengali: 0x0980 - 0x0900 = 0x80
                offset = 0x80;
                break;
            case "PA": // Gurmukhi: 0x0A00 - 0x0900 = 0x100
                offset = 0x100;
                break;
            case "GU": // Gujarati: 0x0A80 - 0x0900 = 0x180
                offset = 0x180;
                break;
            case "OR": // Odia: 0x0B00 - 0x0900 = 0x200
                offset = 0x200;
                break;
            case "TA": // Tamil: 0x0B80 - 0x0900 = 0x280
                offset = 0x280;
                break;
            case "TE": // Telugu: 0x0C00 - 0x0900 = 0x300
                offset = 0x300;
                break;
            case "KN": // Kannada: 0x0C80 - 0x0900 = 0x380
                offset = 0x380;
                break;
            case "ML": // Malayalam: 0x0D00 - 0x0900 = 0x400
                offset = 0x400;
                break;
            default:
                return devanagariText;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < devanagariText.length(); i++) {
            char c = devanagariText.charAt(i);
            // Devanagari Unicode range: 0x0901 to 0x0970
            if (c >= 0x0901 && c <= 0x0970) {
                char target = (char) (c + offset);
                sb.append(target);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
