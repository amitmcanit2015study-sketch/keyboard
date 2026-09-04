package com.amitbharat.hindikeyboard.transliteration

import java.util.Locale

/**
 * Fast, pure offline phonetic transliteration engine (Hinglish -> Devanagari Hindi)
 * High accuracy rule-based syllabic mapper with comprehensive consonant, vowel, conjunct,
 * matra, anusvara, and common word dictionary lookup.
 */
object HindiPhoneticEngine {

    // Common High-Frequency Hinglish Words Dictionary for Instant 100% Accurate Transliteration
    private val commonWords = mapOf(
        "katiyar" to listOf("कटियार", "कतियर", "कटियर"),
        "katyar" to listOf("कटियार", "कतियर"),
        "hindi" to listOf("हिंदी", "हिन्दी", "हिन्दी।"),
        "and" to listOf("और", "तथा", "एंड"),
        "end" to listOf("एंड", "अंत", "खत्म"),
        "english" to listOf("इंग्लिश", "अंग्रेजी", "इंगलिश"),
        "angrezi" to listOf("अंग्रेजी", "अंग्रेज़ी", "इंग्लिश"),
        "antar" to listOf("अंतर", "अन्तर", "फर्क"),
        "fark" to listOf("फर्क", "अंतर", "भेद"),
        "namaste" to listOf("नमस्ते", "नमस्ते।", "नमस्कार"),
        "namaskar" to listOf("नमस्कार", "नमस्ते", "नमस्कार।"),
        "bharat" to listOf("भारत", "भरत", "भारती"),
        "india" to listOf("इंडिया", "भारत", "इण्डिया"),
        "mera" to listOf("मेरा", "मेरी", "मेरे"),
        "meri" to listOf("मेरी", "मेरा", "मेरे"),
        "mere" to listOf("मेरे", "मेरा", "मेरी"),
        "naam" to listOf("नाम", "नम", "नामा"),
        "amit" to listOf("अमित", "अमीत", "अमिताभ"),
        "hai" to listOf("है", "हैं", "हो"),
        "hain" to listOf("हैं", "है", "हां"),
        "ho" to listOf("हो", "हों", "हूं"),
        "hoon" to listOf("हूं", "हूं", "हो"),
        "hun" to listOf("हूं", "हूं", "हो"),
        "kaise" to listOf("कैसे", "कैसा", "कैसी"),
        "kaisa" to listOf("कैसा", "कैसे", "कैसी"),
        "kaisi" to listOf("कैसी", "कैसे", "कैसा"),
        "aap" to listOf("आप", "आपका", "आपकी"),
        "aapka" to listOf("आपका", "आपकी", "आपके"),
        "aapki" to listOf("आपकी", "आपका", "आपके"),
        "aapke" to listOf("आपके", "आपका", "आपकी"),
        "tum" to listOf("तुम", "तुम्हें", "तुम्हारा"),
        "tumhara" to listOf("तुम्हारा", "तुम्हारी", "तुम्हारे"),
        "tumhe" to listOf("तुम्हें", "तुझे", "तुमको"),
        "hum" to listOf("हम", "हमें", "हमारा"),
        "hamara" to listOf("हमारा", "हमारी", "हमारे"),
        "humein" to listOf("हमें", "हमको", "हम"),
        "mujhe" to listOf("मुझे", "मुझको", "मेरे"),
        "tujhe" to listOf("तुझे", "तुझको", "तेरे"),
        "tera" to listOf("तेरा", "तेरी", "तेरे"),
        "teri" to listOf("तेरी", "तेरा", "तेरे"),
        "tere" to listOf("तेरे", "तेरा", "तेरी"),
        "main" to listOf("मैं", "में", "मेन"),
        "mai" to listOf("मैं", "माई", "में"),
        "mein" to listOf("में", "मैं", "अंदर"),
        "me" to listOf("में", "मुझे", "मुझको"),
        "kya" to listOf("क्या", "क्या?", "क्या!"),
        "kyun" to listOf("क्यों", "क्यों?", "क्युं"),
        "kyu" to listOf("क्यों", "क्यों?", "क्युं"),
        "kab" to listOf("कब", "कभी", "कब?"),
        "kahan" to listOf("कहां", "कहा", "कहां"),
        "kaha" to listOf("कहा", "कहां", "कहां"),
        "kaun" to listOf("कौन", "कौन?", "कोन"),
        "kon" to listOf("कौन", "कौन?", "कोन"),
        "kuch" to listOf("कुछ", "कुछ भी", "थोड़ा"),
        "kuchh" to listOf("कुछ", "कुछ भी", "थोड़ा"),
        "koi" to listOf("कोई", "कोई भी", "किसी"),
        "kisi" to listOf("किसी", "किस", "किसे"),
        "batao" to listOf("बताओ", "बताएं", "बताइए"),
        "bataiye" to listOf("बताइए", "बताओ", "बताएं"),
        "bolo" to listOf("बोलो", "बोलिए", "कहिए"),
        "boliye" to listOf("बोलिए", "बोलो", "कहिए"),
        "accha" to listOf("अच्छा", "अच्छी", "अच्छे"),
        "achha" to listOf("अच्छा", "अच्छी", "अच्छे"),
        "theek" to listOf("ठीक", "सही", "ठिक"),
        "thik" to listOf("ठीक", "ठिक", "सही"),
        "sahi" to listOf("सही", "सत्य", "ठीक"),
        "galat" to listOf("गलत", "ग़लत", "अशुद्ध"),
        "dhanyawad" to listOf("धन्यवाद", "धन्यवाद।", "शुक्रिया"),
        "dhanyavad" to listOf("धन्यवाद", "धन्यवाद।", "शुक्रिया"),
        "shukriya" to listOf("शुक्रिया", "धन्यवाद", "शुक्रिया।"),
        "dost" to listOf("दोस्त", "मित्र", "दोस्ती"),
        "yaar" to listOf("यार", "मित्र", "दोस्त"),
        "bhai" to listOf("भाई", "भैया", "भाईजान"),
        "behan" to listOf("बहन", "बहना", "दीदी"),
        "baat" to listOf("बात", "बातें", "बत"),
        "ghar" to listOf("घर", "घरेलू", "मकान"),
        "khana" to listOf("खाना", "खाओ", "खाने"),
        "pani" to listOf("पानी", "जल", "नीर"),
        "chai" to listOf("चाय", "चाय-पानी", "चाय?"),
        "samay" to listOf("समय", "वक्त", "काल"),
        "aaj" to listOf("आज", "आजकल", "आजका"),
        "kal" to listOf("कल", "काल", "कला"),
        "parso" to listOf("परसों", "परसो", "बीता कल"),
        "abhi" to listOf("अभी", "इसी वक्त", "तुरंत"),
        "baad" to listOf("बाद", "बाद में", "उपरांत"),
        "pehle" to listOf("पहले", "पूर्व", "शुरुआत"),
        "hamesha" to listOf("हमेशा", "सदा", "हरदम"),
        "kabhi" to listOf("कभी", "कभी-कभी", "कभी नहीं"),
        "zaruri" to listOf("ज़रूरी", "जरूरी", "आवश्यक"),
        "jaruri" to listOf("जरूरी", "ज़रूरी", "आवश्यक"),
        "bahut" to listOf("बहुत", "बड़ा", "अत्यधिक"),
        "bada" to listOf("बड़ा", "बड़े", "बड़ी"),
        "chota" to listOf("छोटा", "छोटे", "छोटी"),
        "zyada" to listOf("ज्यादा", "ज़्यादा", "अधिक"),
        "jyada" to listOf("ज्यादा", "ज़्यादा", "अधिक"),
        "pyar" to listOf("प्यार", "प्रेम", "मोहब्बत"),
        "pyaar" to listOf("प्यार", "प्रेम", "मोहब्बत"),
        "dil" to listOf("दिल", "हृदय", "मन"),
        "khushi" to listOf("खुशी", "प्रसन्नता", "आनंद"),
        "shanti" to listOf("शांति", "अमन", "चैन"),
        "desh" to listOf("देश", "राष्ट्र", "वतन"),
        "kam" to listOf("काम", "कम", "कर्म"),
        "kaam" to listOf("काम", "कार्य", "कामकाज"),
        "bhi" to listOf("भी", "भी तो", "भी नहीं"),
        "to" to listOf("तो", "तब", "फिर"),
        "toh" to listOf("तो", "तब", "फिर"),
        "aur" to listOf("और", "तथा", "एवं"),
        "lekin" to listOf("लेकिन", "परंतु", "मगर"),
        "magar" to listOf("मगर", "लेकिन", "परंतु"),
        "par" to listOf("पर", "परंतु", "ऊपर"),
        "se" to listOf("से", "द्वारा", "साथ"),
        "ko" to listOf("को", "के लिए", "प्रति"),
        "ka" to listOf("का", "के", "की"),
        "ki" to listOf("की", "कि", "के"),
        "ke" to listOf("के", "का", "की"),
        "pe" to listOf("पे", "पर", "ऊपर"),
        "raha" to listOf("रहा", "रही", "रहे"),
        "rahi" to listOf("रही", "रहा", "रहे"),
        "rahe" to listOf("रहे", "रहा", "रही"),
        "tha" to listOf("था", "थी", "थे"),
        "thi" to listOf("थी", "था", "थे"),
        "the" to listOf("थे", "था", "थी"),
        "chahiye" to listOf("चाहिए", "चाहती", "चाहता"),
        "sakta" to listOf("सकता", "सकती", "सकते"),
        "sakti" to listOf("सकती", "सकता", "सकते"),
        "sakte" to listOf("सकते", "सकता", "सकती"),
        "phone" to listOf("फोन", "मोबाइल", "फ़ोन"),
        "mobile" to listOf("मोबाइल", "फोन", "सेलफोन"),
        "message" to listOf("मैसेज", "संदेश", "मैसेज करो"),
        "chat" to listOf("चैट", "बातचीत", "चैटिंग"),
        "call" to listOf("कॉल", "फोन", "कॉल करो"),
        "ha" to listOf("हां", "हांजी"),
        "haan" to listOf("हां", "हांजी"),
        "han" to listOf("हां", "हांजी"),
        "nahi" to listOf("नहीं", "ना", "नही"),
        "nahin" to listOf("नहीं", "ना", "नही"),
        "na" to listOf("ना", "नहीं", "नाहीं"),
        "bye" to listOf("बाय", "अलविदा", "बाय!"),
        "hello" to listOf("हेलो", "नमस्ते", "नमस्कार"),
        "hi" to listOf("हाय", "नमस्ते", "हेलो"),
        "ok" to listOf("ओके", "ठीक है", "सही है"),
        "yes" to listOf("हां", "यस"),
        "no" to listOf("नहीं", "ना", "नो")
    )

    // Independent Initial Vowels
    private val initialVowels = mapOf(
        "aa" to "आ", "a" to "अ",
        "ee" to "ई", "ii" to "ई", "i" to "इ",
        "oo" to "ऊ", "uu" to "ऊ", "u" to "उ",
        "ai" to "ऐ", "ae" to "ऐ", "e" to "ए",
        "au" to "औ", "ou" to "औ", "o" to "ओ",
        "ri" to "ऋ", "ree" to "ॠ"
    )

    // Matras (Vowel signs attached to consonants)
    private val matras = mapOf(
        "aa" to "ा", "a" to "",
        "ee" to "ी", "ii" to "ी", "i" to "ि",
        "oo" to "ू", "uu" to "ू", "u" to "ु",
        "ai" to "ै", "ae" to "ै", "e" to "े",
        "au" to "ौ", "ou" to "ौ", "o" to "ो",
        "ri" to "ृ"
    )

    // Consonants (mapped to consonant + halant base)
    private val consonants = mapOf(
        "kh" to "ख्", "k" to "क्",
        "gh" to "घ्", "g" to "ग्",
        "ng" to "ंग्",
        "chh" to "छ्", "ch" to "च्",
        "jh" to "झ्", "j" to "ज्",
        "ny" to "ञ्",
        "thh" to "ठ्", "th" to "थ्",
        "t" to "त्", "tt" to "ट्",
        "dhh" to "ढ्", "dh" to "ध्",
        "d" to "द्", "dd" to "ड्",
        "n" to "न्", "nn" to "ण्",
        "ph" to "फ्", "p" to "प्", "f" to "फ़्",
        "bh" to "भ्", "b" to "ब्",
        "m" to "म्",
        "y" to "य्",
        "r" to "र्",
        "l" to "ल्",
        "v" to "व्", "w" to "व्",
        "shh" to "ष्", "sh" to "श्",
        "s" to "स्",
        "h" to "ह्",
        "ksh" to "क्ष्", "x" to "क्ष्",
        "tr" to "त्र्",
        "gy" to "ज्ञ्", "jny" to "ज्ञ्",
        "shr" to "श्र्",
        "q" to "क़्", "z" to "ज़्"
    )

    fun transliterate(input: String): List<String> {
        if (input.isBlank()) return emptyList()

        val lower = input.trim().lowercase(Locale.ROOT)

        // 1. Direct dictionary match
        if (commonWords.containsKey(lower)) {
            val list = commonWords[lower]!!
            return list
        }

        // 2. Multi-word phrase splitting (e.g. "mera naam amit hai")
        if (lower.contains(" ")) {
            val words = lower.split("\\s+".toRegex())
            val transliteratedWords = words.map { word ->
                transliterateSingleWord(word)
            }
            return listOf(transliteratedWords.joinToString(" "))
        }

        // 3. Rule-based single word transliteration
        val primary = transliterateSingleWord(lower)
        val variations = generateVariations(lower, primary)

        return (listOf(primary) + variations).distinct()
    }

    fun transliterateSingleWord(input: String): String {
        if (input.isEmpty()) return ""
        val s = input.lowercase(Locale.ROOT)

        if (commonWords.containsKey(s)) {
            return commonWords[s]!!.first()
        }

        val sb = StringBuilder()
        var i = 0
        var isStart = true

        while (i < s.length) {
            // Check 4-char down to 1-char consonants
            var matchedConsonant: String? = null
            var matchedConsonantKey = ""

            for (len in 4 downTo 1) {
                if (i + len <= s.length) {
                    val sub = s.substring(i, i + len)
                    if (consonants.containsKey(sub)) {
                        matchedConsonant = consonants[sub]
                        matchedConsonantKey = sub
                        break
                    }
                }
            }

            if (matchedConsonant != null) {
                i += matchedConsonantKey.length
                isStart = false

                // Check following vowel/matra
                var matchedMatraKey = ""
                for (vLen in 3 downTo 1) {
                    if (i + vLen <= s.length) {
                        val sub = s.substring(i, i + vLen)
                        if (matras.containsKey(sub)) {
                            matchedMatraKey = sub
                            break
                        }
                    }
                }

                if (matchedMatraKey.isNotEmpty()) {
                    val baseConsonant = matchedConsonant.removeSuffix("्")
                    val matraSign = matras[matchedMatraKey] ?: ""
                    val finalMatra = if (matchedMatraKey == "i" && i + 1 >= s.length) "ी" else matraSign
                    sb.append(baseConsonant).append(finalMatra)
                    i += matchedMatraKey.length
                } else {
                    val baseConsonant = matchedConsonant.removeSuffix("्")
                    if (i >= s.length) {
                        sb.append(baseConsonant)
                    } else {
                        sb.append(matchedConsonant)
                    }
                }
            } else {
                var matchedVowelKey = ""
                for (vLen in 3 downTo 1) {
                    if (i + vLen <= s.length) {
                        val sub = s.substring(i, i + vLen)
                        if (if (isStart) initialVowels.containsKey(sub) else matras.containsKey(sub)) {
                            matchedVowelKey = sub
                            break
                        }
                    }
                }

                if (matchedVowelKey.isNotEmpty()) {
                    if (isStart) {
                        sb.append(initialVowels[matchedVowelKey] ?: "")
                    } else {
                        sb.append(matras[matchedVowelKey] ?: "")
                    }
                    i += matchedVowelKey.length
                    isStart = false
                } else {
                    sb.append(s[i])
                    i++
                    isStart = false
                }
            }
        }

        return sb.toString()
    }

    private fun generateVariations(raw: String, primary: String): List<String> {
        val list = mutableListOf<String>()

        // 1. Anusvara variations (e.g. अन्तर -> अंतर, हिन्दी -> हिंदी)
        if (primary.contains("न्")) {
            list.add(primary.replace("न्", "ं"))
        }
        if (primary.contains("म्")) {
            list.add(primary.replace("म्", "ं"))
        }

        // 2. Ending vowel variations (ी vs ि)
        if (primary.endsWith("ी")) {
            list.add(primary.dropLast(1) + "ि")
        } else if (primary.endsWith("ि")) {
            list.add(primary.dropLast(1) + "ी")
        }

        // 3. Nuqta variations
        if (primary.contains("ज़")) {
            list.add(primary.replace("ज़", "ज"))
        }
        if (primary.contains("फ़")) {
            list.add(primary.replace("फ़", "फ"))
        }

        // 4. Soft 'त' vs Hard 'ट' variation for 't' (e.g. katiyar -> कटियार & कतियर)
        if (primary.contains("त")) {
            list.add(primary.replace("त", "ट"))
        }
        if (primary.contains("ट")) {
            list.add(primary.replace("ट", "त"))
        }
        if (primary.contains("थ")) {
            list.add(primary.replace("थ", "ठ"))
        }
        if (primary.contains("ठ")) {
            list.add(primary.replace("ठ", "थ"))
        }
        if (primary.contains("द")) {
            list.add(primary.replace("द", "ड"))
        }
        if (primary.contains("ड")) {
            list.add(primary.replace("ड", "द"))
        }

        return list.filter { it != primary }
    }
}
