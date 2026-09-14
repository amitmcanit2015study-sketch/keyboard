package com.amitbharat.keyboard.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * High-performance bilingual Hinglish-to-Hindi transliteration engine
 * modeled after Google Indic Keyboard transliteration mode.
 */
public class HinglishTransliterator {

    private static final Map<String, List<String>> HINGLISH_DICT = new HashMap<>();
    private static final Map<String, List<String>> SUGGESTION_CACHE =
            new java.util.LinkedHashMap<String, List<String>>(128, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, List<String>> eldest) {
                    return size() > 256;
                }
            };

    static {
        // High-frequency conversational greetings & pronouns
        add("namaste", "नमस्ते", "नमस्तें");
        add("namaskar", "नमस्कार", "नमस्कारम");
        add("pranam", "प्रणाम");
        add("suprabhat", "सुप्रभात");
        add("shubh", "शुभ");
        add("ratri", "रात्रि", "रात्री");
        add("alvida", "अलविदा");
        add("shukriya", "शुक्रिया");
        add("dhanyawad", "धन्यवाद", "धन्यवाद्");
        add("dhanyavad", "धन्यवाद");
        add("kripya", "कृपया", "कृपया!");
        add("swagat", "स्वागत", "स्वागतम");
        add("badhai", "बधाई", "बधाईयाँ");
        add("mubarak", "मुबारक", "मुबारकबाद");

        // Pronouns
        add("main", "मैं", "मैन");
        add("mai", "मैं", "मै");
        add("me", "मैं", "मुझे");
        add("hum", "हम", "हमें");
        add("ham", "हम", "हमारा");
        add("mera", "मेरा", "मेरी", "मेरे");
        add("meri", "मेरी", "मेरा");
        add("mere", "मेरे", "मेरा");
        add("hamara", "हमारा", "हमारी", "हमारे");
        add("hamari", "हमारी", "हमारा");
        add("hamare", "हमारे", "हमारा");
        add("mujhe", "मुझे", "मुझको");
        add("tum", "तुम", "तुम्हें");
        add("tumhara", "तुम्हारा", "तुम्हारी", "तुम्हारे");
        add("tumhari", "तुम्हारी", "तुम्हारा");
        add("tumhe", "तुम्हें", "तुमको");
        add("aap", "आप", "आपकी", "आपके");
        add("aapka", "आपका", "आपकी", "आपके");
        add("aapki", "आपकी", "आपका");
        add("aapke", "आपके", "आपका");
        add("aapko", "आपको");
        add("yeh", "यह", "ये");
        add("ye", "ये", "यह");
        add("voh", "वह", "वो");
        add("woh", "वह", "वो");
        add("wo", "वो", "वह");
        add("unka", "उनका", "उनकी", "उनके");
        add("unki", "उनकी", "उनका");
        add("unke", "उनके", "उनका");
        add("inka", "इनका", "इनकी", "इनके");
        add("sab", "सब", "सभी");
        add("sabko", "सबको");
        add("koi", "कोई", "किसी");
        add("kisi", "किसी", "किसको");
        add("kuch", "कुछ", "कुछ-कुछ");
        add("kuchh", "कुछ");

        // Question words
        add("kya", "क्या", "क्या?", "क्य");
        add("kyun", "क्यों", "क्यूँ");
        add("kyu", "क्यों", "क्यूँ");
        add("kab", "कब", "कब?");
        add("kahan", "कहाँ", "कहा");
        add("kaha", "कहा", "कहाँ");
        add("kidhar", "किधर");
        add("kaise", "कैसे", "कैसी", "कैसा");
        add("kaisa", "कैसा", "कैसे", "कैसी");
        add("kaisi", "कैसी", "कैसा");
        add("kaun", "कौन", "कौन?");
        add("kon", "कौन");
        add("kitna", "कितना", "कितने", "कितनी");
        add("kitni", "कितनी", "कितना");
        add("kitne", "कितने", "कितना");

        // Connectors / Particles
        add("aur", "और", "और भी");
        add("or", "और", "या");
        add("ya", "या", "या फिर");
        add("bhi", "भी");
        add("hi", "ही");
        add("to", "तो");
        add("toh", "तो");
        add("lekin", "लेकिन");
        add("magar", "मगर");
        add("par", "पर");
        add("kyunki", "क्योंकि");
        add("isliye", "इसलिए");
        add("agar", "अगर");
        add("yadi", "यदि");
        add("jab", "जब");
        add("tab", "तब");
        add("ab", "अब", "अभी");
        add("abhi", "अभी");
        add("yahan", "यहाँ", "यहा");
        add("yaha", "यहाँ", "यहा");
        add("wahan", "वहाँ", "वहा");
        add("vahan", "वहाँ", "वहा");
        add("waha", "वहाँ", "वहा");

        // Verbs & Inflections
        add("ho", "हो", "होगा", "होना");
        add("hai", "है", "हैं");
        add("hain", "हैं", "है");
        add("tha", "था", "थी", "थे");
        add("thi", "थी", "था");
        add("the", "थे", "था");
        add("hoga", "होगा", "होगी", "होंगे");
        add("hogi", "होगी", "होगा");
        add("honge", "होंगे", "होगा");
        add("kar", "कर", "करो", "करना");
        add("karo", "करो", "करिए");
        add("karna", "करना", "करने");
        add("kare", "करें", "करे");
        add("karen", "करें", "करेंगे");
        add("karte", "करते", "करती", "करता");
        add("karta", "करता", "करते");
        add("karti", "करती", "करता");
        add("kiya", "किया", "किए");
        add("kiye", "किए", "किया");
        add("raha", "रहा", "रहे", "रही");
        add("rahe", "रहे", "रहा");
        add("rahi", "रही", "रहा");
        add("gaya", "गया", "गए", "गई");
        add("gaye", "गए", "गया");
        add("gayi", "गई", "गया");
        add("aao", "आओ", "आइए");
        add("aana", "आना", "आने");
        add("aaya", "आया", "आए", "आई");
        add("jao", "जाओ", "जाइए");
        add("jana", "जाना", "जाने");
        add("bolo", "बोलो", "बोलिए");
        add("bol", "बोल", "बोलो");
        add("batao", "बताओ", "बताइए");
        add("batana", "बताना");
        add("dekho", "देखो", "देखिए");
        add("dekh", "देख", "देखो");
        add("suno", "सुनो", "सुनिए");
        add("sun", "सुन", "सुनो");
        add("samjho", "समझो", "समझिए");
        add("samajh", "समझ");
        add("lo", "लो", "लीजिए");
        add("do", "दो", "दीजिए");
        add("dena", "देना");
        add("lena", "लेना");
        add("rakho", "रखो", "रखिए");
        add("chalo", "चलो", "चलिए");
        add("chal", "चल", "चलो");
        add("ruko", "रुको", "रुकिए");

        // Common Nouns & Adjectives
        add("bharat", "भारत", "भारती", "भारतीय");
        add("desh", "देश", "देशवासी");
        add("dost", "दोस्त", "दोस्ती");
        add("mitr", "मित्र", "मित्रता");
        add("bhai", "भाई", "भैया");
        add("behen", "बहन", "बहना");
        add("didi", "दीदी");
        add("maa", "माँ", "मां");
        add("pitaji", "पिताजी", "पिता");
        add("ghar", "घर", "घरेलू");
        add("pani", "पानी", "जल");
        add("khana", "खाना", "खाने");
        add("chai", "चाय");
        add("roti", "रोटी");
        add("kam", "काम", "कम");
        add("kaam", "काम");
        add("naam", "नाम");
        add("nam", "नाम", "नम");
        add("samay", "समय");
        add("waqt", "वक़्त", "वक्त");
        add("aaj", "आज");
        add("kal", "कल");
        add("parso", "परसों");
        add("saal", "साल", "वर्ष");
        add("din", "दिन", "दिवस");
        add("raat", "रात");
        add("subah", "सुबह");
        add("shaam", "शाम");
        add("dopahar", "दोपहर");
        add("pyaar", "प्यार", "प्रेम");
        add("pyar", "प्यार");
        add("prem", "प्रेम");
        add("dil", "दिल");
        add("zindagi", "ज़िंदगी", "जिंदगी");
        add("jivan", "जीवन");
        add("khushi", "ख़ुशी", "खुशी");
        add("shanti", "शांति");
        add("shaan", "शान");
        add("shandar", "शानदार");
        add("accha", "अच्छा", "अच्छी", "अच्छे");
        add("achha", "अच्छा", "अच्छी", "अच्छे");
        add("achhi", "अच्छी", "अच्छा");
        add("achhe", "अच्छे", "अच्छा");
        add("bura", "बुरा", "बुरी", "बुरे");
        add("sahi", "सही");
        add("galat", "गलत", "ग़लत");
        add("sach", "सच", "सच्चा");
        add("jhooth", "झूठ", "झूठा");
        add("jhuth", "झूठ");
        add("bahut", "बहुत");
        add("thoda", "थोड़ा", "थोड़ी", "थोड़े");
        add("zyada", "ज़्यादा", "ज्यादा");
        add("jyada", "ज्यादा", "ज़्यादा");
        add("khoob", "खूब", "ख़ूब");
        add("khoobsurat", "खूबसूरत", "ख़ूबसूरत");
        add("sundar", "सुंदर", "सुन्दर");
        add("pyara", "प्यारा", "प्यारी", "प्यारे");
        add("bada", "बड़ा", "बड़ी", "बड़े");
        add("chhota", "छोटा", "छोटी", "छोटे");
        add("naya", "नया", "नयी", "नए");
        add("purana", "पुराना", "पुरानी", "पुराने");
        add("aasan", "आसान");
        add("mushkil", "मुश्किल");
        add("zaroori", "ज़रूरी", "जरूरी");
        add("zaruri", "ज़रूरी");
        add("madad", "मदद", "सहायता");
        add("sahayata", "सहायता");
        add("sawal", "सवाल", "प्रश्न");
        add("prashna", "प्रश्न");
        add("jawab", "जवाब", "उत्तर");
        add("uttar", "उत्तर");
        add("soch", "सोच");
        add("vichar", "विचार");
        add("yaad", "याद");
        add("bhool", "भूल");
        add("baat", "बात", "बातें");
        add("batein", "बातें");
        add("khabar", "ख़बर", "खबर");
        add("samachar", "समाचार");
        add("duniya", "दुनिया", "संसार");
        add("desh", "देश");
        add("videsh", "विदेश");
        add("shahar", "शहर", "नगर");
        add("gaon", "गाँव", "गांव");
        add("rasta", "रास्ता", "मार्ग");
        add("gadi", "गाड़ी");
        add("paisa", "पैसा", "पैसे", "रुपया");
        add("paise", "पैसे", "पैसा");
        add("rupya", "रुपया", "रुपये");
        add("rupaye", "रुपये", "रुपया");

        // Names & Common words
        add("amit", "अमित");
        add("bharata", "भारत", "भारता");
        add("rooys", "रूईस");
        add("tech", "टेक");
        add("gallery", "गैलरी");
        add("keyboard", "कीबोर्ड");
        add("indic", "इंडिक");
        add("hindi", "हिंदी", "हिन्दी");
        add("english", "अंग्रेज़ी", "इंग्लिश");
        add("hinglish", "हिंग्लिश");

        // Affirmative / Negative
        add("haan", "हाँ", "हां");
        add("ha", "हाँ", "हां");
        add("nahi", "नहीं", "नही");
        add("nahin", "नहीं");
        add("theek", "ठीक");
        add("thik", "ठीक", "ठीक है");
        add("matlab", "मतलब");
        add("pata", "पता");
        add("malum", "मालूम", "मासूम");
        add("zarur", "ज़रूर", "जरूर");
        add("zaroor", "ज़रूर");
        add("pakka", "पक्का");
        add("sachme", "सच में", "सचमुच");
        add("sachmuch", "सचमुच");

        // Numbers in words
        add("ek", "एक");
        add("do", "दो");
        add("teen", "तीन");
        add("char", "चार");
        add("paanch", "पांच", "पाँच");
        add("panch", "पांच");
        add("chhah", "छह", "छः");
        add("saat", "सात");
        add("aath", "आठ");
        add("nau", "नौ");
        add("das", "दस");
    }

    private static void add(String key, String... values) {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, values);
        HINGLISH_DICT.put(key.toLowerCase(Locale.ROOT), list);
    }

    /**
     * Given a raw Hinglish Roman query (e.g. "namaste", "kya", "aap", "dost"),
     * returns a list of candidate suggestions:
     * 1. Direct dictionary match or rule-transliterated Hindi word
     * 2. Additional Hindi inflections or variations
     * 3. Literal English input
     */
    public static List<String> getSuggestions(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }

        synchronized (SUGGESTION_CACHE) {
            List<String> cached = SUGGESTION_CACHE.get(query);
            if (cached != null) {
                return new ArrayList<>(cached);
            }
        }

        List<String> results = new ArrayList<>();
        String clean = query.trim().toLowerCase(Locale.ROOT);

        // 1. Direct match in curated dictionary
        List<String> dictMatches = HINGLISH_DICT.get(clean);
        if (dictMatches != null && !dictMatches.isEmpty()) {
            for (String m : dictMatches) {
                if (!results.contains(m)) {
                    results.add(m);
                }
            }
        }

        // 2. Algorithmic transliteration
        String algorithmic = transliterateRuleBased(clean);
        if (algorithmic != null && !algorithmic.isEmpty() && !results.contains(algorithmic)) {
            results.add(algorithmic);
        }

        // 3. Prefix matching from dictionary for autocomplete
        int addedPrefix = 0;
        for (Map.Entry<String, List<String>> entry : HINGLISH_DICT.entrySet()) {
            if (entry.getKey().startsWith(clean) && !entry.getKey().equals(clean)) {
                for (String word : entry.getValue()) {
                    if (!results.contains(word)) {
                        results.add(word);
                        addedPrefix++;
                        if (addedPrefix >= 2) break;
                    }
                }
            }
            if (addedPrefix >= 2) break;
        }

        // 4. Always provide the raw English word as an option
        if (!results.contains(query)) {
            results.add(query);
        }

        synchronized (SUGGESTION_CACHE) {
            SUGGESTION_CACHE.put(query, results);
        }

        return results;
    }

    /**
     * Rule-based Roman to Devanagari transliterator
     * Handles consonants, conjuncts, matras, independent vowels, and anusvara.
     */
    public static String transliterateRuleBased(String input) {
        if (input == null || input.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        int len = input.length();
        int i = 0;

        boolean previousWasConsonant = false;

        while (i < len) {
            // Check 3-char sequences first (e.g. chh, ksh, gy)
            if (i + 3 <= len) {
                String tri = input.substring(i, i + 3);
                if (tri.equals("chh")) {
                    appendConsonant(sb, "छ", previousWasConsonant);
                    previousWasConsonant = true;
                    i += 3;
                    continue;
                } else if (tri.equals("ksh")) {
                    appendConsonant(sb, "क्ष", previousWasConsonant);
                    previousWasConsonant = true;
                    i += 3;
                    continue;
                }
            }

            // Check 2-char sequences (kh, gh, ch, jh, th, dh, ph, bh, sh, zh, gy, tr, aa, ee, oo, ai, au)
            if (i + 2 <= len) {
                String bi = input.substring(i, i + 2);
                if (bi.equals("kh")) {
                    appendConsonant(sb, "ख", previousWasConsonant);
                    previousWasConsonant = true;
                    i += 2;
                    continue;
                } else if (bi.equals("gh")) {
                    appendConsonant(sb, "घ", previousWasConsonant);
                    previousWasConsonant = true;
                    i += 2;
                    continue;
                } else if (bi.equals("ch")) {
                    appendConsonant(sb, "च", previousWasConsonant);
                    previousWasConsonant = true;
                    i += 2;
                    continue;
                } else if (bi.equals("jh")) {
                    appendConsonant(sb, "झ", previousWasConsonant);
                    previousWasConsonant = true;
                    i += 2;
                    continue;
                } else if (bi.equals("th")) {
                    appendConsonant(sb, "थ", previousWasConsonant);
                    previousWasConsonant = true;
                    i += 2;
                    continue;
                } else if (bi.equals("dh")) {
                    appendConsonant(sb, "ध", previousWasConsonant);
                    previousWasConsonant = true;
                    i += 2;
                    continue;
                } else if (bi.equals("ph")) {
                    appendConsonant(sb, "फ", previousWasConsonant);
                    previousWasConsonant = true;
                    i += 2;
                    continue;
                } else if (bi.equals("bh")) {
                    appendConsonant(sb, "भ", previousWasConsonant);
                    previousWasConsonant = true;
                    i += 2;
                    continue;
                } else if (bi.equals("sh")) {
                    appendConsonant(sb, "श", previousWasConsonant);
                    previousWasConsonant = true;
                    i += 2;
                    continue;
                } else if (bi.equals("gy")) {
                    appendConsonant(sb, "ज्ञ", previousWasConsonant);
                    previousWasConsonant = true;
                    i += 2;
                    continue;
                } else if (bi.equals("tr")) {
                    appendConsonant(sb, "त्र", previousWasConsonant);
                    previousWasConsonant = true;
                    i += 2;
                    continue;
                } else if (bi.equals("aa")) {
                    appendVowel(sb, "आ", "ा", previousWasConsonant);
                    previousWasConsonant = false;
                    i += 2;
                    continue;
                } else if (bi.equals("ee") || bi.equals("ii")) {
                    appendVowel(sb, "ई", "ी", previousWasConsonant);
                    previousWasConsonant = false;
                    i += 2;
                    continue;
                } else if (bi.equals("oo") || bi.equals("uu")) {
                    appendVowel(sb, "ऊ", "ू", previousWasConsonant);
                    previousWasConsonant = false;
                    i += 2;
                    continue;
                } else if (bi.equals("ai")) {
                    appendVowel(sb, "ऐ", "ै", previousWasConsonant);
                    previousWasConsonant = false;
                    i += 2;
                    continue;
                } else if (bi.equals("au") || bi.equals("ou")) {
                    appendVowel(sb, "औ", "ौ", previousWasConsonant);
                    previousWasConsonant = false;
                    i += 2;
                    continue;
                }
            }

            char c = input.charAt(i);

            // Single characters
            switch (c) {
                // Vowels
                case 'a':
                    // If preceded by consonant, 'a' just satisfies the inherent vowel (no halant, no matra)
                    // unless it's independent
                    if (!previousWasConsonant) {
                        sb.append("अ");
                    }
                    previousWasConsonant = false;
                    break;
                case 'i':
                    appendVowel(sb, "इ", "ि", previousWasConsonant);
                    previousWasConsonant = false;
                    break;
                case 'u':
                    appendVowel(sb, "उ", "ु", previousWasConsonant);
                    previousWasConsonant = false;
                    break;
                case 'e':
                    appendVowel(sb, "ए", "े", previousWasConsonant);
                    previousWasConsonant = false;
                    break;
                case 'o':
                    appendVowel(sb, "ओ", "ो", previousWasConsonant);
                    previousWasConsonant = false;
                    break;

                // Consonants
                case 'k':
                    appendConsonant(sb, "क", previousWasConsonant);
                    previousWasConsonant = true;
                    break;
                case 'g':
                    appendConsonant(sb, "ग", previousWasConsonant);
                    previousWasConsonant = true;
                    break;
                case 'j':
                    appendConsonant(sb, "ज", previousWasConsonant);
                    previousWasConsonant = true;
                    break;
                case 't':
                    appendConsonant(sb, "त", previousWasConsonant);
                    previousWasConsonant = true;
                    break;
                case 'd':
                    appendConsonant(sb, "द", previousWasConsonant);
                    previousWasConsonant = true;
                    break;
                case 'n':
                    // Anusvara handling if at end of word preceded by vowel or before another consonant
                    if (i == len - 1 && !previousWasConsonant && sb.length() > 0) {
                        sb.append("ं");
                        previousWasConsonant = false;
                    } else {
                        appendConsonant(sb, "न", previousWasConsonant);
                        previousWasConsonant = true;
                    }
                    break;
                case 'p':
                    appendConsonant(sb, "प", previousWasConsonant);
                    previousWasConsonant = true;
                    break;
                case 'f':
                    appendConsonant(sb, "फ़", previousWasConsonant);
                    previousWasConsonant = true;
                    break;
                case 'b':
                    appendConsonant(sb, "ब", previousWasConsonant);
                    previousWasConsonant = true;
                    break;
                case 'm':
                    appendConsonant(sb, "म", previousWasConsonant);
                    previousWasConsonant = true;
                    break;
                case 'y':
                    appendConsonant(sb, "य", previousWasConsonant);
                    previousWasConsonant = true;
                    break;
                case 'r':
                    appendConsonant(sb, "र", previousWasConsonant);
                    previousWasConsonant = true;
                    break;
                case 'l':
                    appendConsonant(sb, "ल", previousWasConsonant);
                    previousWasConsonant = true;
                    break;
                case 'v':
                case 'w':
                    appendConsonant(sb, "व", previousWasConsonant);
                    previousWasConsonant = true;
                    break;
                case 's':
                    appendConsonant(sb, "स", previousWasConsonant);
                    previousWasConsonant = true;
                    break;
                case 'h':
                    appendConsonant(sb, "ह", previousWasConsonant);
                    previousWasConsonant = true;
                    break;
                case 'z':
                    appendConsonant(sb, "ज़", previousWasConsonant);
                    previousWasConsonant = true;
                    break;
                case 'q':
                    appendConsonant(sb, "क़", previousWasConsonant);
                    previousWasConsonant = true;
                    break;
                case 'x':
                    appendConsonant(sb, "क्स", previousWasConsonant);
                    previousWasConsonant = true;
                    break;
                case 'c':
                    appendConsonant(sb, "क", previousWasConsonant);
                    previousWasConsonant = true;
                    break;
                default:
                    sb.append(c);
                    previousWasConsonant = false;
                    break;
            }
            i++;
        }

        return sb.toString();
    }

    private static void appendConsonant(StringBuilder sb, String consonant, boolean previousWasConsonant) {
        if (previousWasConsonant) {
            // Conjunct creation: add virama/halant (्) between two consecutive consonants
            sb.append("्");
        }
        sb.append(consonant);
    }

    private static void appendVowel(StringBuilder sb, String independentVowel, String matra, boolean previousWasConsonant) {
        if (previousWasConsonant) {
            sb.append(matra);
        } else {
            sb.append(independentVowel);
        }
    }
}
