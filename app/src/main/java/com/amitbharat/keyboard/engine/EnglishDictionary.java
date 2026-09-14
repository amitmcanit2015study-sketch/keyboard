package com.amitbharat.keyboard.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * High-speed English dictionary and prefix suggestion engine
 * with auto-completion and spell suggestions.
 */
public class EnglishDictionary {

    private static final TrieNode root = new TrieNode();
    private static final java.util.Set<String> COMMON_WORDS_SET = new java.util.HashSet<>();
    private static final java.util.Map<String, List<String>> QUERY_CACHE =
            new java.util.LinkedHashMap<String, List<String>>(64, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(java.util.Map.Entry<String, List<String>> eldest) {
                    return size() > 128;
                }
            };
    private static final List<String> COMMON_WORDS = Arrays.asList(
            // Top everyday English words frequency ranked
            "the", "be", "to", "of", "and", "a", "in", "that", "have", "i",
            "it", "for", "not", "on", "with", "he", "as", "you", "do", "at",
            "this", "but", "his", "by", "from", "they", "we", "say", "her", "she",
            "or", "an", "will", "my", "one", "all", "would", "there", "their", "what",
            "so", "up", "out", "if", "about", "who", "get", "which", "go", "me",
            "when", "make", "can", "like", "time", "no", "just", "him", "know", "take",
            "people", "into", "year", "your", "good", "some", "could", "them", "see", "other",
            "than", "then", "now", "look", "only", "come", "its", "over", "think", "also",
            "back", "after", "use", "two", "how", "our", "work", "first", "well", "way",
            "even", "new", "want", "because", "any", "these", "give", "day", "most", "us",
            // Greetings & Common interactions
            "hello", "help", "held", "helping", "helpful", "here", "hear", "heart",
            "hi", "hey", "howdy", "good morning", "good afternoon", "good evening", "good night",
            "please", "thanks", "thank you", "welcome", "sorry", "excuse", "pardon",
            "yes", "yeah", "yep", "sure", "ok", "okay", "fine", "alright", "great", "awesome",
            "nice", "cool", "super", "wonderful", "perfect", "amazing", "beautiful", "love",
            // Frequent Verbs
            "am", "is", "are", "was", "were", "been", "being", "has", "had",
            "doing", "did", "done", "does", "going", "went", "gone", "goes",
            "seeing", "saw", "seen", "getting", "got", "gotten", "making", "made",
            "knowing", "knew", "known", "taking", "took", "taken", "coming", "came",
            "thinking", "thought", "looking", "looked", "giving", "gave", "given",
            "finding", "found", "telling", "told", "asking", "asked", "working", "worked",
            "calling", "called", "trying", "tried", "needing", "needed", "feeling", "felt",
            "becoming", "became", "leaving", "left", "putting", "put", "meaning", "meant",
            "keeping", "kept", "letting", "let", "beginning", "began", "begun",
            "showing", "showed", "shown", "hearing", "heard", "playing", "played",
            "running", "ran", "run", "moving", "moved", "living", "lived", "believing", "believed",
            "bringing", "brought", "happening", "happened", "writing", "wrote", "written",
            "providing", "provided", "sitting", "sat", "standing", "stood", "losing", "lost",
            "paying", "paid", "meeting", "met", "including", "included", "continuing", "continued",
            "setting", "learning", "learnt", "learned", "changing", "changed", "leading", "led",
            "understanding", "understood", "watching", "watched", "following", "followed",
            "stopping", "stopped", "creating", "created", "speaking", "spoke", "spoken",
            "reading", "read", "spending", "spent", "growing", "grew", "grown",
            "opening", "opened", "walking", "walked", "winning", "won", "teaching", "taught",
            "offering", "offered", "remembering", "remembered", "considering", "considered",
            "appearing", "appeared", "buying", "bought", "serving", "served", "sending", "sent",
            "expecting", "expected", "building", "built", "staying", "stayed", "falling", "fell",
            "cutting", "cut", "reaching", "reached", "killing", "killed", "remaining", "remained",
            // Common Nouns
            "world", "life", "hand", "part", "child", "eye", "woman", "place", "work", "week",
            "case", "point", "government", "company", "number", "group", "problem", "fact",
            "friend", "home", "night", "water", "room", "mother", "father", "brother", "sister",
            "area", "money", "story", "fact", "month", "lot", "right", "study", "book", "eye",
            "job", "word", "business", "issue", "side", "kind", "head", "house", "service",
            "friend", "father", "power", "hour", "game", "line", "end", "member", "law", "car",
            "city", "community", "name", "president", "team", "minute", "idea", "kid", "body",
            "information", "back", "parent", "face", "others", "level", "office", "door", "health",
            "person", "art", "war", "history", "party", "result", "change", "morning", "reason",
            "research", "girl", "guy", "moment", "air", "teacher", "force", "education",
            // Technology & Devices
            "phone", "keyboard", "android", "mobile", "app", "application", "gallery", "image",
            "photo", "video", "media", "camera", "device", "screen", "settings", "system",
            "language", "hindi", "english", "indic", "code", "update", "download", "share",
            "developer", "software", "technology", "smart", "fast", "internet", "message", "chat",
            // Qualities & Adjectives
            "important", "big", "high", "different", "small", "large", "next", "early", "young",
            "important", "few", "public", "bad", "same", "able", "free", "true", "clear", "easy",
            "full", "special", "easy", "hard", "strong", "happy", "real", "best", "better", "great"
    );

    static {
        COMMON_WORDS_SET.addAll(COMMON_WORDS);
        for (String word : COMMON_WORDS) {
            insert(word);
        }
    }

    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isWord = false;
        String fullWord = "";
    }

    private static void insert(String word) {
        if (word == null) return;
        TrieNode curr = root;
        for (char c : word.toLowerCase(Locale.ROOT).toCharArray()) {
            curr.children.putIfAbsent(c, new TrieNode());
            curr = curr.children.get(c);
        }
        curr.isWord = true;
        curr.fullWord = word;
    }

    /**
     * Get English suggestions based on current typing input.
     * Preserves capitalization (e.g. "Hel" -> "Hello", "Help").
     */
    public static List<String> getSuggestions(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }

        synchronized (QUERY_CACHE) {
            List<String> cached = QUERY_CACHE.get(query);
            if (cached != null) {
                return new ArrayList<>(cached);
            }
        }

        List<String> results = new ArrayList<>();
        boolean isCapitalized = Character.isUpperCase(query.charAt(0));
        boolean isAllUpper = query.length() > 1 && query.equals(query.toUpperCase(Locale.ROOT));

        String lowerQuery = query.toLowerCase(Locale.ROOT);

        // Find node matching prefix
        TrieNode curr = root;
        for (char c : lowerQuery.toCharArray()) {
            if (!curr.children.containsKey(c)) {
                curr = null;
                break;
            }
            curr = curr.children.get(c);
        }

        if (curr != null) {
            collectWords(curr, results, 6);
        }

        // If exact match is found, ensure it is first
        if (COMMON_WORDS_SET.contains(lowerQuery) && !results.contains(lowerQuery)) {
            results.add(0, lowerQuery);
        }

        // If no matches found in trie, provide original query as first candidate
        if (results.isEmpty()) {
            results.add(query);
        }

        // Format casing
        List<String> formatted = new ArrayList<>();
        for (String w : results) {
            String out = w;
            if (isAllUpper) {
                out = w.toUpperCase(Locale.ROOT);
            } else if (isCapitalized && w.length() > 0) {
                out = Character.toUpperCase(w.charAt(0)) + (w.length() > 1 ? w.substring(1) : "");
            }
            if (!formatted.contains(out)) {
                formatted.add(out);
            }
        }

        synchronized (QUERY_CACHE) {
            QUERY_CACHE.put(query, formatted);
        }

        return formatted;
    }

    private static void collectWords(TrieNode node, List<String> results, int max) {
        if (results.size() >= max) return;
        if (node.isWord) {
            results.add(node.fullWord);
        }
        for (TrieNode child : node.children.values()) {
            collectWords(child, results, max);
            if (results.size() >= max) return;
        }
    }
}
