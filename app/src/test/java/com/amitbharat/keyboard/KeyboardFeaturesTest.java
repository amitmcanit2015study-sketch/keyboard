package com.amitbharat.keyboard;

import com.amitbharat.keyboard.engine.EnglishDictionary;
import com.amitbharat.keyboard.engine.IndicTransliterator;
import com.amitbharat.keyboard.engine.LanguageItem;
import com.amitbharat.keyboard.ime.EmojiData;

import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class KeyboardFeaturesTest {

    // =========================================================================
    // FEATURE 1: Word Suggestions, Typo Autocorrect, and Fuzzy Matching
    // =========================================================================

    @Test
    public void testTypoAutocorrectCommonWords() {
        // Test key frequent typos and their corrections
        String[][] typoPairs = {
            {"beutifull", "beautiful"},
            {"Beutifull", "Beautiful"},
            {"BEUTIFULL", "BEAUTIFUL"},
            {"definately", "definitely"},
            {"Definately", "Definitely"},
            {"tommorow", "tomorrow"},
            {"tommorrow", "tomorrow"},
            {"recieved", "received"},
            {"seperate", "separate"},
            {"untill", "until"},
            {"wierd", "weird"},
            {"goverment", "government"},
            {"truely", "truly"},
            {"occured", "occurred"},
            {"adress", "address"},
            {"beleive", "believe"},
            {"thier", "their"}
        };

        for (String[] pair : typoPairs) {
            String typo = pair[0];
            String expected = pair[1];
            List<String> suggestions = IndicTransliterator.getSuggestions("EN", typo);
            assertNotNull("Suggestions should not be null for " + typo, suggestions);
            assertFalse("Suggestions should not be empty for " + typo, suggestions.isEmpty());
            assertEquals("Autocorrect for '" + typo + "' must be '" + expected + "'", expected, suggestions.get(0));
            assertTrue("EnglishDictionary.hasAutocorrect should be true for typo '" + typo + "'",
                    EnglishDictionary.hasAutocorrect(typo));
        }
    }

    @Test
    public void testFuzzyMatchingWithinEditDistance() {
        // Words with single character transposition, substitution, or deletion
        String[] fuzzyInputs = {"beautful", "beautfiul", "definitly", "helo", "computr", "langauge"};
        for (String input : fuzzyInputs) {
            List<String> suggestions = IndicTransliterator.getSuggestions("EN", input);
            assertNotNull("Suggestions should not be null for " + input, suggestions);
            assertFalse("Suggestions should not be empty for fuzzy input: " + input, suggestions.isEmpty());
        }
    }

    @Test
    public void testPrefixSuggestions() {
        // Typing prefixes should suggest completions
        List<String> progSuggestions = IndicTransliterator.getSuggestions("EN", "progr");
        assertNotNull(progSuggestions);
        assertTrue("Prefix 'progr' should suggest 'program'", progSuggestions.contains("program"));

        List<String> beautSuggestions = IndicTransliterator.getSuggestions("EN", "beauti");
        assertNotNull(beautSuggestions);
        assertTrue("Prefix 'beauti' should suggest 'beautiful'", beautSuggestions.contains("beautiful"));
    }

    @Test
    public void testBigramNextWordPredictions() {
        // Next word predictions for common words
        List<String> thankNext = IndicTransliterator.getSuggestions("EN", "thank ");
        assertNotNull(thankNext);
        assertTrue("After 'thank ', should suggest 'you'", thankNext.contains("you"));

        List<String> goodNext = IndicTransliterator.getSuggestions("EN", "good ");
        assertNotNull(goodNext);
        assertTrue("After 'good ', should suggest 'morning'", goodNext.contains("morning"));

        List<String> howNext = IndicTransliterator.getSuggestions("EN", "how ");
        assertNotNull(howNext);
        assertTrue("After 'how ', should suggest 'are'", howNext.contains("are"));
    }

    @Test
    public void testCandidateStripBalance() {
        // Suggestions should provide up to 3 candidates
        List<String> suggestions = IndicTransliterator.getSuggestions("EN", "beutifull");
        assertNotNull(suggestions);
        assertTrue("Should provide at least 2 candidates", suggestions.size() >= 2);
        assertTrue("Primary suggestion must be 'beautiful'", suggestions.contains("beautiful"));
        assertTrue("Raw typed word should be available as fallback", suggestions.contains("beutifull"));
    }

    // =========================================================================
    // FEATURE 2: Indic Transliteration Engines (Hindi, Marathi, Gujarati, etc.)
    // =========================================================================

    @Test
    public void testHindiTransliterationCoverage() {
        String[][] hindiPairs = {
            {"namaste", "नमस्ते"},
            {"bharat", "भारत"},
            {"kya", "क्या"},
            {"kaise", "कैसे"},
            {"shukriya", "शुक्रिया"},
            {"dhanyawad", "धन्यवाद"}
        };

        for (String[] pair : hindiPairs) {
            String roman = pair[0];
            String expected = pair[1];
            List<String> suggestions = IndicTransliterator.getSuggestions("HN", roman);
            assertNotNull("Suggestions should not be null for Hindi " + roman, suggestions);
            assertTrue("Hindi transliteration for '" + roman + "' should contain '" + expected + "'",
                    suggestions.contains(expected));
        }
    }

    @Test
    public void testRegionalIndicLanguages() {
        // Marathi
        List<String> mr = IndicTransliterator.getSuggestions("MR", "namaskar");
        assertTrue("Marathi transliteration for 'namaskar' should contain 'नमस्कार'", mr.contains("नमस्कार"));

        // Gujarati
        List<String> gu = IndicTransliterator.getSuggestions("GU", "namaste");
        assertTrue("Gujarati transliteration for 'namaste' should contain 'નમસ્તે'", gu.contains("નમસ્તે"));

        // Bengali
        List<String> bn = IndicTransliterator.getSuggestions("BN", "nomoshkar");
        assertTrue("Bengali transliteration for 'nomoshkar' should contain 'নমস্কার'", bn.contains("নমস্কার"));

        // Punjabi
        List<String> pa = IndicTransliterator.getSuggestions("PA", "satshriakal");
        assertTrue("Punjabi transliteration should contain 'ਸਤਿ ਸ਼੍ਰੀ ਅਕਾਲ'", pa.contains("ਸਤਿ ਸ਼੍ਰੀ ਅਕਾਲ"));
    }

    // =========================================================================
    // FEATURE 3: Language Registry
    // =========================================================================

    @Test
    public void testLanguageRegistry() {
        List<LanguageItem> languages = LanguageItem.getAllLanguages();
        assertNotNull(languages);
        assertTrue("Language list should contain at least 20 languages", languages.size() >= 20);

        boolean hasHindi = false;
        boolean hasEnglish = false;
        for (LanguageItem item : languages) {
            if ("HN".equals(item.getCode())) hasHindi = true;
            if ("EN".equals(item.getCode())) hasEnglish = true;
            assertNotNull(item.getEnglishName());
            assertNotNull(item.getNativeName());
            assertNotNull(item.getDisplayName());
        }
        assertTrue("Language registry must include Hindi", hasHindi);
        assertTrue("Language registry must include English", hasEnglish);
    }

    // =========================================================================
    // FEATURE 4: Emojis, Kaomoji, and Real Animated GIFs
    // =========================================================================

    @Test
    public void testEmojiAndKaomojiData() {
        // Categories
        List<String> icons = EmojiData.CATEGORY_ICONS;
        assertNotNull(icons);
        assertTrue("Should have multiple emoji category icons", icons.size() >= 8);

        // Emoji list
        List<String> smileys = EmojiData.CATEGORY_EMOJIS.get(EmojiData.CAT_SMILEYS);
        assertNotNull(smileys);
        assertTrue("Smileys category should contain emojis", smileys.size() > 0);

        // Kaomoji
        assertNotNull("Kaomoji categories should exist", EmojiData.KAOMOJI_CATEGORIES);
        assertFalse("Kaomoji categories should not be empty", EmojiData.KAOMOJI_CATEGORIES.isEmpty());
        assertTrue("Should have classic kaomojis", EmojiData.KAOMOJI_CATEGORIES.containsKey("Happy"));
    }

    @Test
    public void testGifCardsAndAssetIntegrity() {
        List<EmojiData.GifCard> gifs = EmojiData.getAllGifCards();
        assertNotNull("Real GIFs list must not be null", gifs);
        assertTrue("Should have at least 16 curated animated GIFs", gifs.size() >= 16);

        File assetsDir = new File("src/main/assets/gifs");
        for (EmojiData.GifCard card : gifs) {
            assertNotNull("GIF card category cannot be null", card.category);
            assertNotNull("GIF card title cannot be null", card.title);
            assertNotNull("GIF card assetFile cannot be null", card.assetFile);
            assertTrue("GIF card assetFile must end with .gif", card.assetFile.endsWith(".gif"));

            // Verify file exists in assets folder
            File gifFile = new File(assetsDir, card.assetFile);
            assertTrue("GIF asset file must exist on disk: " + card.assetFile, gifFile.exists());
            assertTrue("GIF file size must be > 0 bytes: " + card.assetFile, gifFile.length() > 0);
        }
    }
}
