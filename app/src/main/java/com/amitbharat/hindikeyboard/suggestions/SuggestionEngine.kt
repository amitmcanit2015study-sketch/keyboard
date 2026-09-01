package com.amitbharat.hindikeyboard.suggestions

import com.amitbharat.hindikeyboard.transliteration.HindiPhoneticEngine
import java.util.Locale

enum class TypingMode {
    ENGLISH,
    HINDI_TRANSLITERATION
}

object SuggestionEngine {

    private val englishTrie = TrieDictionary()

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
        "food" to listOf("🍕", "🍔", "🍛"),
        "chai" to listOf("☕", "🍵"),
        "tea" to listOf("☕", "🫖"),
        "namaste" to listOf("🙏"),
        "pranam" to listOf("🙏")
    )

    init {
        // Populate top English words into Trie
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
            "namaste", "india", "bharat", "amazing", "beautiful", "brother", "friend", "happy"
        )
        topEnglish.forEachIndexed { index, word ->
            englishTrie.insert(word, 1000 - index)
        }
    }

    fun getSuggestions(rawInput: String, mode: TypingMode): List<String> {
        val input = rawInput.trim()
        if (input.isEmpty()) return emptyList()

        val results = mutableListOf<String>()

        // 1. Check Abbreviations (e.g. gm -> Good Morning)
        val lower = input.lowercase(Locale.ROOT)
        if (abbreviations.containsKey(lower)) {
            results.add(abbreviations[lower]!!)
        }

        // 2. Check Contextual Emoji Prediction
        if (emojiKeywords.containsKey(lower)) {
            results.addAll(emojiKeywords[lower]!!)
        }

        if (mode == TypingMode.HINDI_TRANSLITERATION) {
            // Hindi Transliteration Mode
            val hindiCandidates = HindiPhoneticEngine.transliterate(input)
            results.addAll(hindiCandidates)
            // Add original English raw input as last choice
            if (!results.contains(input)) {
                results.add(input)
            }
        } else {
            // English Mode
            if (rawInput.first().isUpperCase()) {
                val matches = englishTrie.findWordsWithPrefix(lower, 4)
                results.addAll(matches.map { it.replaceFirstChar { c -> c.uppercase() } })
            } else {
                results.addAll(englishTrie.findWordsWithPrefix(input, 4))
            }

            if (!results.contains(input)) {
                results.add(0, input)
            }
        }

        return results.distinct().take(6)
    }
}
