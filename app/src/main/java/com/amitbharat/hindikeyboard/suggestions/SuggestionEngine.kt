package com.amitbharat.hindikeyboard.suggestions

import com.amitbharat.hindikeyboard.transliteration.HindiPhoneticEngine
import java.util.Locale

enum class TypingMode {
    ENGLISH,
    HINDI_TRANSLITERATION
}

object SuggestionEngine {

    private val englishTrie = TrieDictionary()
    private val englishVocabularyList: List<String>
    private val englishVocabularySet: Set<String>

    // Smart abbreviations mapping
    private val abbreviations = mapOf(
        "gm" to "Good Morning",
        "gn" to "Good Night",
        "ge" to "Good Evening",
        "ga" to "Good Afternoon",
        "brb" to "Be Right Back",
        "omw" to "On My Way",
        "idk" to "I Don't Know",
        "imo" to "In My Opinion",
        "tbh" to "To Be Honest",
        "hbd" to "Happy Birthday",
        "ty" to "Thank You",
        "tysm" to "Thank You So Much",
        "np" to "No Problem",
        "tc" to "Take Care",
        "asap" to "As Soon As Possible",
        "btw" to "By The Way",
        "fyi" to "For Your Information",
        "wru" to "Where are you?",
        "wyd" to "What are you doing?"
    )

    // Contextual Emoji Predictor
    private val emojiKeywords = mapOf(
        "happy" to listOf("😊", "😄", "😍", "🎉"),
        "smile" to listOf("😊", "😁", "🙂"),
        "love" to listOf("❤️", "😍", "💖", "🥰"),
        "heart" to listOf("❤️", "💕", "💘"),
        "sad" to listOf("😢", "😭", "😔", "💔"),
        "cry" to listOf("😭", "😢", "😿"),
        "angry" to listOf("😡", "😠", "🤬"),
        "fire" to listOf("🔥", "💥", "⚡"),
        "cool" to listOf("😎", "🤙", "🕶️"),
        "ok" to listOf("👍", "👌", "✅"),
        "yes" to listOf("👍", "✅", "🙌"),
        "no" to listOf("👎", "❌", "🚫"),
        "clap" to listOf("👏", "🙌", "🎉"),
        "party" to listOf("🥳", "🎉", "🍻"),
        "food" to listOf("🍕", "🍔", "🍜"),
        "chai" to listOf("☕", "🍵"),
        "tea" to listOf("☕", "🫖"),
        "namaste" to listOf("🙏"),
        "pranam" to listOf("🙏")
    )

    // Sentence Next-Word Prediction Map
    private val nextWordMap = mapOf(
        "good" to listOf("Morning", "Night", "Evening", "Afternoon", "Luck", "Job"),
        "how" to listOf("are you?", "is", "do you", "can I", "to"),
        "where" to listOf("are you?", "is", "do", "were", "have"),
        "what" to listOf("is", "are you", "do", "about", "happened?"),
        "thank" to listOf("you", "you so much", "a lot", "you for"),
        "thanks" to listOf("a lot", "so much", "for everything"),
        "i" to listOf("am", "have", "will", "would", "want", "think", "can", "know"),
        "you" to listOf("are", "can", "have", "will", "should", "want", "know"),
        "he" to listOf("is", "was", "has", "will", "said"),
        "she" to listOf("is", "was", "has", "will", "said"),
        "we" to listOf("are", "were", "can", "will", "have"),
        "they" to listOf("are", "were", "have", "will", "can"),
        "it" to listOf("is", "was", "will", "would", "looks"),
        "this" to listOf("is", "was", "will", "one"),
        "that" to listOf("is", "was", "will", "one"),
        "please" to listOf("help", "let me", "find", "check", "send"),
        "see" to listOf("you", "you soon", "later", "again"),
        "take" to listOf("care", "it easy", "your time"),
        "nice" to listOf("to meet you", "work", "day"),
        "happy" to listOf("birthday", "new year", "to help"),
        "call" to listOf("me", "you", "later", "back"),
        "send" to listOf("me", "him", "her", "details"),
        "let" to listOf("me", "us", "know"),
        "sentence" to listOf("completion", "suggestion", "is correct", "structure"),
        "correct" to listOf("sentence", "word", "spelling", "answer"),
        "increase" to listOf("size", "volume", "speed", "font"),
        "mera" to listOf("naam", "ghar", "phone", "dost"),
        "kya" to listOf("hai", "baat", "hua", "kar"),
        "namaste" to listOf("ji", "aapka", "kaise")
    )

    private val defaultSentenceStarters = listOf("Hello", "How", "Thank you", "What", "Good morning", "Please")

    init {
        val topEnglish = listOf(
            "the", "be", "to", "of", "and", "a", "in", "that", "have", "I",
            "it", "for", "not", "on", "with", "he", "as", "you", "do", "at",
            "this", "but", "his", "by", "from", "they", "we", "say", "her", "she",
            "or", "an", "will", "my", "one", "all", "would", "there", "their", "what",
            "so", "up", "out", "if", "about", "who", "get", "which", "go", "me",
            "when", "make", "can", "like", "time", "no", "just", "him", "know", "take",
            "people", "into", "year", "your", "good", "some", "could", "them", "see", "other",
            "than", "then", "now", "look", "only", "come", "its", "over", "think", "also",
            "back", "after", "use", "two", "how", "our", "work", "first", "well", "way",
            "even", "new", "want", "because", "any", "these", "give", "day", "most", "us",
            "great", "morning", "night", "evening", "welcome", "please", "thanks", "hello",
            "namaste", "india", "bharat", "amazing", "beautiful", "brother", "friend", "happy",
            "going", "doing", "where", "something", "nothing", "everything", "together", "tomorrow",
            "yesterday", "today", "family", "message", "call", "number", "office", "home",
            "busy", "available", "contact", "phone", "mobile", "address", "location", "send",
            "receive", "check", "please", "kindly", "update", "status", "complete", "finish",
            "ready", "start", "stop", "change", "system", "online", "meeting", "schedule",
            "confirm", "reply", "answer", "question", "problem", "solution", "support", "help",
            "important", "urgent", "need", "should", "must", "might", "maybe", "always",
            "never", "sometimes", "often", "again", "before", "after", "between", "under",
            "above", "right", "left", "front", "behind", "around", "through", "during",
            "increase", "increment", "cream", "sentence", "suggestion", "correct", "language",
            "keyboard", "typing", "transliteration", "application", "information", "service"
        )
        englishVocabularyList = topEnglish
        englishVocabularySet = topEnglish.toSet()

        topEnglish.forEachIndexed { index, word ->
            englishTrie.insert(word, 1000 - index)
        }
    }

    fun getSuggestions(rawInput: String, mode: TypingMode, contextText: String = ""): List<String> {
        val input = rawInput.trim()
        val results = mutableListOf<String>()

        // Case 1: When input is empty, return Next-Word / Sentence Prediction
        if (input.isEmpty()) {
            if (contextText.isNotBlank()) {
                val words = contextText.trim().split("\\s+".toRegex())
                val lastWord = words.lastOrNull()?.lowercase(Locale.ROOT)?.replace(Regex("[^a-z0-9]"), "") ?: ""
                if (nextWordMap.containsKey(lastWord)) {
                    return nextWordMap[lastWord]!!
                }
            }
            return defaultSentenceStarters
        }

        val lower = input.lowercase(Locale.ROOT)

        // 1. Check Abbreviations (e.g. gm -> Good Morning)
        if (abbreviations.containsKey(lower)) {
            results.add(abbreviations[lower]!!)
        }

        // 2. Check Contextual Emoji Prediction
        if (emojiKeywords.containsKey(lower)) {
            results.addAll(emojiKeywords[lower]!!)
        }

        if (mode == TypingMode.HINDI_TRANSLITERATION) {
            // Hindi Transliteration Mode (e.g. katiyar -> कटियार, कतियर)
            val hindiCandidates = HindiPhoneticEngine.transliterate(input)
            results.addAll(hindiCandidates)
            if (!results.contains(input)) {
                results.add(input)
            }
        } else {
            // English Mode:
            // A. Trie Prefix Matches
            val matches = if (rawInput.first().isUpperCase()) {
                englishTrie.findWordsWithPrefix(lower, 5).map { it.replaceFirstChar { c -> c.uppercase() } }
            } else {
                englishTrie.findWordsWithPrefix(lower, 5)
            }
            results.addAll(matches)

            // B. Compound Word Splitter (e.g. "incream" -> "in cream")
            val splitCandidate = getCompoundSplit(lower)
            if (splitCandidate != null) {
                results.add(splitCandidate)
            }

            // C. Fuzzy Edit-Distance Match for Typos (e.g. "incream" -> "increase", "increment", "cream")
            val fuzzyCandidates = getFuzzyMatches(lower)
            results.addAll(fuzzyCandidates)

            // Substring word match fallback (e.g. "incream" contains "cream")
            for (vocabWord in englishVocabularyList) {
                if (vocabWord.length >= 4 && lower.contains(vocabWord) && !results.contains(vocabWord)) {
                    results.add(vocabWord)
                }
            }

            // If raw input is not yet in results, add raw input as first option
            if (!results.contains(input)) {
                results.add(0, input)
            }
        }

        return results.distinct().take(6)
    }

    private fun getCompoundSplit(input: String): String? {
        if (input.length < 4) return null
        for (i in 2..(input.length - 2)) {
            val p1 = input.substring(0, i)
            val p2 = input.substring(i)
            if (englishVocabularySet.contains(p1) && englishVocabularySet.contains(p2)) {
                return "$p1 $p2"
            }
        }
        return null
    }

    private fun getFuzzyMatches(input: String): List<String> {
        if (input.length < 3) return emptyList()
        val candidates = mutableListOf<Pair<String, Int>>()
        for (word in englishVocabularyList) {
            val dist = computeLevenshtein(input, word)
            if (dist in 1..2 && word != input) {
                candidates.add(word to dist)
            }
        }
        return candidates.sortedBy { it.second }.map { it.first }
    }

    private fun computeLevenshtein(s1: String, s2: String): Int {
        val m = s1.length
        val n = s2.length
        val dp = Array(m + 1) { IntArray(n + 1) }
        for (i in 0..m) dp[i][0] = i
        for (j in 0..n) dp[0][j] = j
        for (i in 1..m) {
            for (j in 1..n) {
                val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,
                    dp[i][j - 1] + 1,
                    dp[i - 1][j - 1] + cost
                )
            }
        }
        return dp[m][n]
    }
}
