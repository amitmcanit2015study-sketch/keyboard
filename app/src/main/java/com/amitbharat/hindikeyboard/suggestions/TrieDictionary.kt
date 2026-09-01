package com.amitbharat.hindikeyboard.suggestions

class TrieNode {
    val children = mutableMapOf<Char, TrieNode>()
    var isEndOfWord = false
    var word: String? = null
    var frequency: Int = 0
}

class TrieDictionary {
    private val root = TrieNode()

    fun insert(word: String, frequency: Int = 1) {
        if (word.isBlank()) return
        var current = root
        for (ch in word.lowercase()) {
            current = current.children.getOrPut(ch) { TrieNode() }
        }
        current.isEndOfWord = true
        current.word = word
        current.frequency = maxOf(current.frequency, frequency)
    }

    fun findWordsWithPrefix(prefix: String, limit: Int = 5): List<String> {
        if (prefix.isBlank()) return emptyList()
        var current = root
        for (ch in prefix.lowercase()) {
            current = current.children[ch] ?: return emptyList()
        }

        val results = mutableListOf<Pair<String, Int>>()
        collectAllWords(current, results)
        return results.sortedByDescending { it.second }.map { it.first }.take(limit)
    }

    private fun collectAllWords(node: TrieNode, results: MutableList<Pair<String, Int>>) {
        if (node.isEndOfWord && node.word != null) {
            results.add(Pair(node.word!!, node.frequency))
        }
        for (child in node.children.values) {
            collectAllWords(child, results)
        }
    }
}
