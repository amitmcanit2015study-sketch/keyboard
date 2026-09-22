package com.amitbharat.keyboard;

import com.amitbharat.keyboard.engine.EnglishDictionary;
import com.amitbharat.keyboard.engine.IndicTransliterator;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class SuggestionAndTypingTest {

    @Test
    public void testCapitalizedFirstLetterSuggestion() {
        // Requirement 1: First letter is capital, suggestions must preserve capital first letter
        List<String> suggestions = IndicTransliterator.getSuggestions("EN", "When");
        assertNotNull(suggestions);
        assertFalse("Suggestions should not be empty for 'When'", suggestions.isEmpty());
        assertEquals("Top suggestion for 'When' must have capital first letter", "When", suggestions.get(0));

        // Test lowercase
        List<String> lowerSuggestions = IndicTransliterator.getSuggestions("EN", "when");
        assertEquals("Top suggestion for 'when' must be lowercase", "when", lowerSuggestions.get(0));

        // Test ALL CAPS
        List<String> upperSuggestions = IndicTransliterator.getSuggestions("EN", "WHEN");
        assertEquals("Top suggestion for 'WHEN' must be uppercase", "WHEN", upperSuggestions.get(0));
    }

    @Test
    public void testSingleCharacterAndPrefixSpellingValidation() {
        // Requirement 2: Single character like 'h' should not be considered a complete correct word
        assertFalse("'h' should not be considered a valid complete English word", EnglishDictionary.isCorrectWord("h"));
        assertFalse("'wh' should not be considered a valid complete English word", EnglishDictionary.isCorrectWord("wh"));
        assertFalse("'prog' should not be considered a valid complete English word", EnglishDictionary.isCorrectWord("prog"));

        // Valid complete words
        assertTrue("'when' is a valid word", EnglishDictionary.isCorrectWord("when"));
        assertTrue("'When' is a valid word", EnglishDictionary.isCorrectWord("When"));
        assertTrue("'hello' is a valid word", EnglishDictionary.isCorrectWord("hello"));
        assertTrue("'have' is a valid word", EnglishDictionary.isCorrectWord("have"));

        // Suggestions for 'h' should still be available in the candidate strip
        List<String> hSuggestions = IndicTransliterator.getSuggestions("EN", "h");
        assertNotNull(hSuggestions);
        assertFalse("Candidate strip should still show suggestions for 'h'", hSuggestions.isEmpty());
    }

    @Test
    public void testHindiTransliteration() {
        // Requirement 5: Hindi suggestions
        List<String> namasteSuggestions = IndicTransliterator.getSuggestions("HN", "namaste");
        assertTrue("Hinglish 'namaste' should suggest 'नमस्ते'", namasteSuggestions.contains("नमस्ते"));

        List<String> kyaSuggestions = IndicTransliterator.getSuggestions("HN", "kya");
        assertTrue("Hinglish 'kya' should suggest 'क्या'", kyaSuggestions.contains("क्या"));
    }

    @Test
    public void testBeautifulFuzzyAutocorrect() {
        // Test user's exact case: typing "Beutifull" must suggest "Beautiful"
        List<String> suggestions = IndicTransliterator.getSuggestions("EN", "Beutifull");
        assertNotNull(suggestions);
        assertFalse("Suggestions must not be empty for 'Beutifull'", suggestions.isEmpty());
        assertEquals("Top autocorrect suggestion for 'Beutifull' must be 'Beautiful'", "Beautiful", suggestions.get(0));

        // Test lowercase "beutifull"
        List<String> lowerSuggestions = IndicTransliterator.getSuggestions("EN", "beutifull");
        assertEquals("Top autocorrect suggestion for 'beutifull' must be 'beautiful'", "beautiful", lowerSuggestions.get(0));

        // Test "definately" -> "definitely"
        List<String> defSuggestions = IndicTransliterator.getSuggestions("EN", "definately");
        assertEquals("Top autocorrect suggestion for 'definately' must be 'definitely'", "definitely", defSuggestions.get(0));
    }
}

