package com.amitbharat.hindikeyboard.transliteration

import java.util.Locale

/**
 * Fast, pure offline phonetic transliteration engine (Hinglish -> Devanagari Hindi)
 * High accuracy rule-based syllabic mapper with comprehensive consonant, vowel, conjunct,
 * matra, anusvara, and common word dictionary lookup.
 */
object HindiPhoneticEngine {

    // Common High-Frequency Words Dictionary for Instant 100% Accurate Transliteration
    private val commonWords = mapOf(
        "namaste" to listOf("नमस्ते", "नमस्ते।", "नमस्कार"),
        "namaskar" to listOf("नमस्कार", "नमस्ते", "नमस्कार।"),
        "bharat" to listOf("भारत", "भरत", "भारती"),
        "india" to listOf("इंडिया", "भारत", "इण्डिया"),
        "mera" to listOf("मेरा", "मेरी", "मेरे"),
        "meri" to listOf("मेरी", "मेरा", "मेरे"),
        "mere" to listOf("मेरे", "मेरा", "मेरी"),
        "naam" to listOf("नाम", "नम", "नामा"),
        "amit" to listOf("अमित", "अमीत", "अमिताभ"),
        "bharat" to listOf("भारत", "भरत", "भारती"),
        "hai" to listOf("है", "हैं", "हो"),
        "hain" to listOf("हैं", "है", "हाँ"),
        "ho" to listOf("हो", "हों", "हू"),
        "hoon" to listOf("हूँ", "हूं", "हो"),
        "hun" to listOf("हूँ", "हूं", "हो"),
        "kaise" to listOf("कैसे", "कैसा", "कैसी"),
        "kaisa" to listOf("कैसा", "कैसे", "कैसी"),
        "kaisi" to listOf("कैसी", "कैसे", "कैसा"),
        "aap" to listOf("आप", "आपका", "आपकी"),
        "tum" to listOf("तुम", "तुम्हें", "तुम्हारा"),
        "main" to listOf("मैं", "मेन", "मई"),
        "mai" to listOf("मैं", "माई", "मा"),
        "hum" to listOf("हम", "हमें", "हमारा"),
        "kya" to listOf("क्या", "कया", "क्या?"),
        "kyun" to listOf("क्यों", "क्यों?", "क्युं"),
        "kyu" to listOf("क्यों", "क्यों?", "क्युं"),
        "kab" to listOf("कब", "कभी", "कबा"),
        "kahan" to listOf("कहाँ", "कहां", "कहा"),
        "kaha" to listOf("कहा", "कहाँ", "कहां"),
        "kaun" to listOf("कौन", "कौन?", "कौनसा"),
        "kon" to listOf("कौन", "कौन?", "कोन"),
        "accha" to listOf("अच्छा", "अच्छी", "अच्छे"),
        "achha" to listOf("अच्छा", "अच्छी", "अच्छे"),
        "theek" to listOf("ठीक", "सही", "ठिक"),
        "thik" to listOf("ठीक", "ठिक", "सही"),
        "dhanyawad" to listOf("धन्यवाद", "धन्यवाद।", "शुक्रिया"),
        "dhanyavad" to listOf("धन्यवाद", "धन्यवाद।", "शुक्रिया"),
        "shukriya" to listOf("शुक्रिया", "धन्यवाद", "शुक्रिया।"),
        "shubh" to listOf("शुभ", "शुभ प्रभात", "शुभ रात्रि"),
        "prabhat" to listOf("प्रभात", "शुभ प्रभात", "सवेरा"),
        "ratri" to listOf("रात्रि", "शुभ रात्रि", "रात"),
        "dost" to listOf("दोस्त", "मित्र", "दोस्ती"),
        "bhai" to listOf("भाई", "भैया", "भाईजान"),
        "behan" to listOf("बहन", "बहना", "दीदी"),
        "baat" to listOf("बात", "बातें", "बत"),
        "ghar" to listOf("घर", "घरेलू", "मकान"),
        "khana" to listOf("खाना", "खाओ", "खाने"),
        "pani" to listOf("पानी", "जल", "नीर"),
        "samay" to listOf("समय", "वक्त", "काल"),
        "aaj" to listOf("आज", "आजकल", "आजका"),
        "kal" to listOf("कल", "काल", "कला"),
        "parso" to listOf("परसों", "परसो", "बीता कल"),
        "bahut" to listOf("बहुत", "बड़ा", "अत्यधिक"),
        "bada" to listOf("बड़ा", "बड़े", "बड़ी"),
        "chota" to listOf("छोटा", "छोटे", "छोटी"),
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
        "aur" to listOf("और", "तथा", "एवं"),
        "lekin" to listOf("लेकिन", "परंतु", "मगर"),
        "magar" to listOf("मगर", "लेकिन", "परंतु"),
        "par" to listOf("पर", "परंतु", "ऊपर"),
        "se" to listOf("से", "द्वारा", "साथ"),
        "ko" to listOf("को", "के लिए", "प्रति"),
        "ka" to listOf("का", "के", "की"),
        "ki" to listOf("की", "कि", "के"),
        "ke" to listOf("के", "का", "की"),
        "mein" to listOf("में", "अंदर", "भीतर"),
        "me" to listOf("में", "मुझे", "अंदर"),
        "pe" to listOf("पे", "पर", "ऊपर"),
        "jana" to listOf("जाना", "जाओ", "गया"),
        "aana" to listOf("आना", "आओ", "आया"),
        "karna" to listOf("करना", "करो", "किया"),
        "dekhna" to listOf("देखना", "देखो", "देखा"),
        "sunna" to listOf("सुनना", "सुनो", "सुना"),
        "bolna" to listOf("बोलना", "बोलो", "कहो"),
        "likhna" to listOf("लिखना", "लिखो", "लिखा"),
        "padhna" to listOf("पढ़ना", "पढ़ो", "पढ़ा")
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
        "ng" to "ङ्",
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
                transliterateSingleWord(word).firstOrNull() ?: word
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

        val sb = StringBuilder()
        var i = 0
        var isStart = true

        while (i < s.length) {
            // Check 3-char, 2-char, then 1-char consonants
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
                    // Consonant + Matra
                    val baseConsonant = matchedConsonant.removeSuffix("्")
                    val matraSign = matras[matchedMatraKey] ?: ""
                    sb.append(baseConsonant).append(matraSign)
                    i += matchedMatraKey.length
                } else {
                    // In Hindi, consonant at end of word or before another consonant:
                    // If at the end of word and no explicit vowel, it is full consonant without halant (e.g. "nam" -> "नाम" / "नम")
                    val baseConsonant = matchedConsonant.removeSuffix("्")
                    if (i >= s.length) {
                        sb.append(baseConsonant)
                    } else {
                        // Consonant cluster / conjunct: keep halant
                        sb.append(matchedConsonant)
                    }
                }
            } else {
                // Initial or standalone vowel
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
                    // Check special modifiers: anusvara (n/m), chandrabindu, visarga (h)
                    val c = s[i]
                    if (c == 'n' || c == 'm') {
                        if (i == s.length - 1 && sb.isNotEmpty()) {
                            sb.append("ं")
                        } else {
                            sb.append(c)
                        }
                    } else {
                        sb.append(c)
                    }
                    i++
                    isStart = false
                }
            }
        }

        return sb.toString()
    }

    private fun generateVariations(input: String, primary: String): List<String> {
        val list = mutableListOf<String>()

        // Add alternate common forms
        if (input.endsWith("a") && primary.endsWith("ा")) {
            list.add(primary.removeSuffix("ा"))
        } else if (!input.endsWith("a") && !primary.endsWith("ा")) {
            list.add(primary + "ा")
        }

        if (primary.contains("ि")) {
            list.add(primary.replace("ि", "ी"))
        }
        if (primary.contains("ी")) {
            list.add(primary.replace("ी", "ि"))
        }
        if (primary.contains("ु")) {
            list.add(primary.replace("ु", "ू"))
        }
        if (primary.contains("ू")) {
            list.add(primary.replace("ू", "ु"))
        }

        return list.filter { it.isNotEmpty() && it != primary }
    }
}
