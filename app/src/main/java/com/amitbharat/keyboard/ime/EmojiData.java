package com.amitbharat.keyboard.ime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EmojiData {

    public static final String CAT_RECENT = "🕒";
    public static final String CAT_SMILEYS = "😊";
    public static final String CAT_HEARTS = "💖";
    public static final String CAT_PEOPLE = "👋";
    public static final String CAT_FESTIVALS = "🪔";
    public static final String CAT_ANIMALS = "🐶";
    public static final String CAT_FOOD = "🍔";
    public static final String CAT_ACTIVITIES = "⚽";
    public static final String CAT_TRAVEL = "✈️";
    public static final String CAT_TECH = "📱";
    public static final String CAT_OBJECTS = "💡";
    public static final String CAT_FLAGS = "🚩";

    public static final List<String> CATEGORY_ICONS = Arrays.asList(
            CAT_RECENT, CAT_SMILEYS, CAT_HEARTS, CAT_PEOPLE, CAT_FESTIVALS,
            CAT_ANIMALS, CAT_FOOD, CAT_ACTIVITIES, CAT_TRAVEL, CAT_TECH, CAT_OBJECTS, CAT_FLAGS
    );

    public static final Map<String, List<String>> CATEGORY_EMOJIS = new LinkedHashMap<>();
    private static final Map<String, List<String>> KEYWORD_INDEX = new HashMap<>();

    static {
        // 1. Smileys & Emotions (110+ emojis)
        CATEGORY_EMOJIS.put(CAT_SMILEYS, Arrays.asList(
                "😀", "😃", "😄", "😁", "😆", "😅", "😂", "🤣", "🥲", "🥹",
                "😊", "😇", "🙂", "🙃", "😉", "😌", "😍", "🥰", "😘", "😗",
                "😙", "😚", "😋", "😛", "😝", "😜", "🤪", "🤨", "🧐", "🤓",
                "😎", "🥸", "🤩", "🥳", "😏", "😒", "😞", "😔", "😟", "😕",
                "🙁", "☹️", "😣", "😖", "😫", "😩", "🥺", "😢", "😭", "😮‍💨",
                "😤", "😠", "😡", "🤬", "🤯", "😳", "🥵", "🥶", "😱", "😨",
                "😰", "😥", "😓", "🤗", "🤔", "🫣", "🤭", "🤫", "🤥", "😶",
                "😐", "😑", "😬", "🫨", "🙄", "😯", "😦", "😧", "😮", "😲",
                "🥱", "😴", "🤤", "😪", "😵", "😵‍💫", "🤐", "🥴", "🤢", "🤮",
                "🤧", "😷", "🤒", "🤕", "🤑", "🤠", "😈", "👿", "👹", "👺",
                "🤡", "💩", "👻", "💀", "☠️", "👽", "👾", "🤖", "🎃", "😺",
                "😸", "😹", "😻", "😼", "😽", "🙀", "😿", "😾"
        ));

        // 2. Hearts, Romance & Love (40+ emojis)
        CATEGORY_EMOJIS.put(CAT_HEARTS, Arrays.asList(
                "❤️", "🧡", "💛", "💚", "💙", "💜", "🖤", "🤍", "🤎", "💔",
                "❣️", "💕", "💞", "💓", "💗", "💖", "💘", "💝", "💟", "💌",
                "💋", "👄", "🫦", "🫂", "😻", "😽", "😍", "🥰", "😘", "🌹",
                "🥀", "💐", "🍫", "💍", "💎", "💒", "🏩", "❤️‍🔥", "❤️‍🩹", "🏩",
                "🧸", "🕯️", "🎀"
        ));

        // 3. People & Gestures (80+ emojis)
        CATEGORY_EMOJIS.put(CAT_PEOPLE, Arrays.asList(
                "👋", "🤚", "🖐️", "✋", "🖖", "🫱", "🫲", "🫳", "🫴", "👌",
                "🤌", "🤏", "✌️", "🤞", "🫰", "🤟", "🤘", "🤙", "👈", "👉",
                "👆", "🖕", "👇", "☝️", "🫵", "👍", "👎", "✊", "👊", "🤛",
                "🤜", "👏", "🙌", "🫶", "👐", "🤲", "🤝", "🙏", "✍️", "💅",
                "🤳", "💪", "🦾", "🦿", "🦵", "🦶", "👂", "🦻", "👃", "🫀",
                "🫁", "🧠", "👀", "👁️", "👅", "👶", "🧒", "👦", "👧", "🧑",
                "👱", "👨", "🧔", "👩", "🧓", "👴", "👵", "👮", "👷", "💂",
                "🕵️", "👩‍⚕️", "👨‍🌾", "👩‍🍳", "👩‍🎓", "👩‍🎤", "👩‍🏫", "🧑‍💻", "🧑‍🚀"
        ));

        // 4. Indian Festivals & Spiritual (45+ emojis)
        CATEGORY_EMOJIS.put(CAT_FESTIVALS, Arrays.asList(
                "🪔", "🕉️", "🇮🇳", "🛕", "🕌", "⛪", "🪷", "🌸", "🌺", "🌼",
                "💐", "🥥", "🚩", "🎆", "🎇", "✨", "🎊", "🎉", "🍬", "🍭",
                "🍯", "🪘", "🪗", "🥁", "🪕", "🔔", "📿", "☀️", "🌙", "⭐",
                "🌟", "🔥", "🌈", "🪁", "🎋", "🏮", "🕯️", "🎁", "🎈", "🌾"
        ));

        // 5. Animals & Nature (75+ emojis)
        CATEGORY_EMOJIS.put(CAT_ANIMALS, Arrays.asList(
                "🐶", "🐱", "🐭", "🐹", "🐰", "🦊", "🐻", "🐼", "🐻‍❄️", "🐨",
                "🐯", "🦁", "🐮", "🐷", "🐽", "🐸", "🐵", "🐒", "🦍", "🦧",
                "🐔", "🐧", "🐦", "🐤", "🐣", "🐥", "🦆", "🦅", "🦉", "🦇",
                "🐺", "🐗", "🐴", "🦄", "🐝", "🪱", "🐛", "🦋", "🐌", "🐞",
                "🐜", "🪰", "🪲", "🪳", "🦟", "🦗", "🕷️", "🦂", "🐢", "🐍",
                "🦎", "🐙", "🦑", "🦐", "🦞", "🦀", "🐡", "🐠", "🐟", "🐬",
                "🐳", "🐋", "🦈", "🦭", "🐊", "🐅", "🐆", "🦓", "🐘", "🦏",
                "🦛", "🐪", "🐫", "🦒", "🦘", "🦬", "🐃", "🐂", "🐄", "🐎"
        ));

        // 6. Food & Drink (60+ emojis)
        CATEGORY_EMOJIS.put(CAT_FOOD, Arrays.asList(
                "☕", "🍵", "🧃", "🥤", "🧋", "🥛", "🍼", "🍺", "🍻", "🥂",
                "🍷", "🥃", "🍸", "🍹", "🧉", "🍾", "🍕", "🍔", "🍟", "🌭",
                "🍿", "🧈", "🥞", "🧇", "🧀", "🍖", "🍗", "🥩", "🥓", "🥪",
                "🥙", "🧆", "🌮", "🌯", "🫔", "🥗", "🥘", "🫕", "🥫", "🍝",
                "🍜", "🍲", "🍛", "🍣", "🍱", "🥟", "🦪", "🍤", "🍙", "🍚",
                "🍎", "🍏", "🍐", "🍊", "🍋", "🍌", "🍉", "🍇", "🍓", "🫐",
                "🥭", "🍍", "🥥", "🥑", "🥦", "🌶️", "🫑", "🌽", "🥕", "🥔"
        ));

        // 7. Activities & Sports (40+ emojis)
        CATEGORY_EMOJIS.put(CAT_ACTIVITIES, Arrays.asList(
                "🏏", "⚽", "🏀", "🏈", "⚾", "🥎", "🎾", "🏐", "🥏", "🎱",
                "🪀", "🏓", "🏸", "🏒", "🏑", "🥍", "🏹", "🎣", "🤿", "🥊",
                "🥋", "🛹", "🛼", "🛷", "⛸️", "🏋️", "🤼", "🤸", "🤺", "🧘",
                "🚴", "🏆", "🥇", "🥈", "🥉", "🏅", "🎖️", "🎪", "🎭", "🎨",
                "🎤", "🎧", "🎬", "🎸", "🎹", "🥁", "🎲", "🎯", "🎮"
        ));

        // 8. Travel & Places (45+ emojis)
        CATEGORY_EMOJIS.put(CAT_TRAVEL, Arrays.asList(
                "🛺", "🚗", "🚕", "🚙", "🚌", "🚎", "🏎️", "🚓", "🚑", "🚒",
                "🚐", "🛻", "🚚", "🚛", "🚜", "🛴", "🚲", "🛵", "🏍️", "🚨",
                "🚡", "🚠", "🚟", "🚃", "🚋", "🚞", "🚄", "🚅", "🚈", "🚂",
                "🚆", "🚇", "✈️", "🛫", "🛬", "🚀", "🛸", "🚁", "🛶", "⛵",
                "🚤", "🛳️", "⛴️", "🚢", "⛽", "🚦", "🏖️", "⛰️", "🌋"
        ));

        // 9. Tech & Gadgets (35+ emojis)
        CATEGORY_EMOJIS.put(CAT_TECH, Arrays.asList(
                "📱", "📲", "💻", "⌨️", "🖥️", "🖨️", "🖱️", "📷", "📸", "📹",
                "🎥", "📽️", "📞", "☎️", "📟", "📺", "📻", "🎙️", "🎛️", "⏱️",
                "⏰", "🕰️", "⌛", "⏳", "📡", "🔋", "🪫", "🔌", "💡", "🔦",
                "🕹️", "🎮", "🖲️", "💾", "💿", "📀"
        ));

        // 10. Objects & Symbols (50+ emojis)
        CATEGORY_EMOJIS.put(CAT_OBJECTS, Arrays.asList(
                "✨", "⭐", "🌟", "💫", "💥", "🎉", "🎊", "🎈", "🎁", "🪄",
                "🪆", "🏮", "💸", "💵", "💴", "💶", "💷", "🪙", "💰", "💳",
                "💎", "⚖️", "🔑", "🗝️", "🔨", "🪓", "🔧", "🪛", "🧲", "🔔",
                "🔕", "📢", "📣", "🔒", "🔓", "🔍", "🔎", "📌", "📍", "📎",
                "🔗", "✂️", "✒️", "🖊️", "📝", "✏️", "📖", "📚", "🏷️", "📦"
        ));

        // 11. Flags (35+ emojis)
        CATEGORY_EMOJIS.put(CAT_FLAGS, Arrays.asList(
                "🇮🇳", "🏁", "🚩", "🎌", "🏴", "🏳️", "🏳️‍🌈", "🏳️‍⚧️", "🏴‍☠️", "🇦🇺",
                "🇧🇷", "🇨🇦", "🇨🇳", "🇩🇪", "🇪🇸", "🇫🇷", "🇬🇧", "🇮🇩", "🇮🇱", "🇮🇹",
                "🇯🇵", "🇰🇷", "🇲🇽", "🇲🇾", "🇳🇵", "🇳🇿", "🇵🇰", "🇷🇺", "🇸🇦", "🇸🇬",
                "🇹🇷", "🇺🇦", "🇦🇪", "🇺🇸", "🇻🇳", "🇿🇦"
        ));

        // Keyword search index
        index("love", "❤️", "😍", "🥰", "😘", "💖", "💕", "💓", "💗", "💘", "💝", "🌹");
        index("happy", "😀", "😃", "😄", "😁", "😊", "🙂", "🥳", "🤩", "✨", "🎉");
        index("laugh", "😂", "🤣", "😆", "😅", "😹");
        index("sad", "😢", "😭", "🥺", "😞", "😔", "😟", "🙁", "☹️");
        index("angry", "😠", "😡", "🤬", "😤", "👿");
        index("cry", "😢", "😭", "😿", "🥺");
        index("fire", "🔥", "💥", "✨", "❤️‍🔥");
        index("heart", "❤️", "🧡", "💛", "💚", "💙", "💜", "🖤", "🤍", "🤎", "💔", "💖");
        index("hand", "👋", "🤚", "✋", "👌", "🤌", "✌️", "🤞", "👍", "👎", "👏", "🙌", "🙏");
        index("india", "🇮🇳", "🕉️", "🪔", "🛕", "🛺", "🏏", "☕");
        index("diwali", "🪔", "✨", "🎆", "🎇", "🎉", "🍬");
        index("chai", "☕", "🍵");
        index("tea", "☕", "🍵");
        index("food", "🍕", "🍔", "🍟", "🌮", "🌯", "🍜", "🍛", "🥪");
        index("cricket", "🏏", "🏆", "🥇");
        index("dog", "🐶", "🐕", "🦮");
        index("cat", "🐱", "🐈", "😻", "😸");
        index("car", "🚗", "🏎️", "🚙", "🚕", "🛺");
        index("party", "🎉", "🎊", "🥳", "🎈", "🍾", "🍻");
        index("money", "💰", "💸", "💵", "🤑", "🪙");
        index("namaste", "🙏", "🕉️", "🪔");
        index("ok", "👌", "👍", "🙆");
        index("cool", "😎", "🤩", "🆒");
    }

    private static void index(String keyword, String... emojis) {
        KEYWORD_INDEX.put(keyword.toLowerCase(Locale.ROOT), Arrays.asList(emojis));
    }

    public static List<String> searchEmojis(String query) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String q = query.trim().toLowerCase(Locale.ROOT);
        List<String> results = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : KEYWORD_INDEX.entrySet()) {
            if (entry.getKey().contains(q) || q.contains(entry.getKey())) {
                for (String em : entry.getValue()) {
                    if (!results.contains(em)) results.add(em);
                }
            }
        }

        if (results.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : CATEGORY_EMOJIS.entrySet()) {
                for (String em : entry.getValue()) {
                    results.add(em);
                    if (results.size() >= 40) break;
                }
                if (results.size() >= 40) break;
            }
        }
        return results;
    }

    // Kaomoji
    public static final Map<String, List<String>> KAOMOJI_CATEGORIES = new LinkedHashMap<>();

    static {
        KAOMOJI_CATEGORIES.put("Happy", Arrays.asList(
                "(•‿•)", "(^_^)v", "(^o^)", "(◕‿◕)", "(*^▽^*)",
                "( ﾟヮﾟ)", "(✿◠‿◠)", "\\(★ω★)/", "(｡♥‿♥｡)", "(^◡^)"
        ));
        KAOMOJI_CATEGORIES.put("Love", Arrays.asList(
                "(♥_♥)", "(づ｡◕‿‿◕｡)づ", "(♡˙︶˙♡)", "(´∀｀)♡",
                "(づ￣ ³￣)づ", "(｡♥‿♥｡)", "(*¯ ³¯*)♡", "(´ε｀ )♡"
        ));
        KAOMOJI_CATEGORIES.put("Shrug", Arrays.asList(
                "¯\\_(ツ)_/¯", "¯\\(°_o)/¯", "╮(─▽─)╭", "(¬_¬)",
                "(・_・)", "(•ิ_•ิ)?", "┐(‘～` )┌", "ヽ(ヅ)ノ"
        ));
        KAOMOJI_CATEGORIES.put("Shock", Arrays.asList(
                "(⊙_⊙)", "(°o°)", "(•_•)", "(ʘᗩʘ')", "(O_O)",
                "Σ(°ロ°)", "Σ(ಠ_ಠ)", "(°ロ°) !"
        ));
        KAOMOJI_CATEGORIES.put("Fight", Arrays.asList(
                "(╯°□°)╯︵ ┻━┻", "(ง'̀-'́)ง", "(ಠ_ಠ)", "(ノಠ益ಠ)ノ",
                "ᕙ(⇀‸↼‶)ᕗ", "(ง︡'-'︠)ง", "( ˘▾˘)~゛"
        ));
    }

    // Video Clip Model & Library
    public static class VideoClip {
        public final String emoji;
        public final String title;
        public final String duration;
        public final String caption;
        public final String category;

        public VideoClip(String emoji, String title, String duration, String caption, String category) {
            this.emoji = emoji;
            this.title = title;
            this.duration = duration;
            this.caption = caption;
            this.category = category;
        }
    }

    public static List<VideoClip> getAllVideoClips() {
        List<VideoClip> list = new ArrayList<>();
        // Bollywood & Viral Movies
        list.add(new VideoClip("🔥", "Pushpa Fire Hai", "0:04", "🔥 Pushpa... Jhukega Nahi Saala! Main Jhukega Nahi!", "Bollywood"));
        list.add(new VideoClip("👓", "Babu Rao Swag", "0:05", "👓 Utha le re baba! Utha le mereko nahi isko!", "Memes"));
        list.add(new VideoClip("🕺", "Tauba Tauba", "0:06", "🕺 Tauba Tauba! Killer dance moves on repeat!", "Trending"));
        list.add(new VideoClip("🫂", "Munna Bhai Hug", "0:04", "🫂 Jadoo Ki Jhappi! Tension nahi lene ka bhai!", "Bollywood"));
        list.add(new VideoClip("🦁", "Singham Entry", "0:04", "🦁 Aata maajhi satakli! Sher ki entry!", "Bollywood"));
        list.add(new VideoClip("💃", "Jethalal Garba", "0:05", "💃 Hey maa, Mataji! Garba shuru karo tapu ke papa!", "Memes"));
        list.add(new VideoClip("😎", "Hero No. 1", "0:04", "😎 Sona kitna sona hai! Swag se karenge swagat!", "Bollywood"));

        // Memes & Viral Reactions
        list.add(new VideoClip("👏", "Kya Baat Hai", "0:03", "👏 Wah! Kya baat hai! Ekdum shaandar!", "Trending"));
        list.add(new VideoClip("🤯", "Khatarnaak", "0:03", "🤯 Khatarnaak! Aag laga di bhai, gazab!", "Trending"));
        list.add(new VideoClip("🐱", "Cat Vibing", "0:05", "🐱 Cat vibing to the beat! Pure joy!", "Memes"));
        list.add(new VideoClip("😂", "Haso Mat", "0:03", "😂 Hahaha control nahi ho raha! Pet phool gaya!", "Memes"));

        // Desi & Celebrations
        list.add(new VideoClip("☕", "Chai Peelo", "0:03", "☕ Hello friends! Garma garam Chai peelo!", "Desi"));
        list.add(new VideoClip("🇮🇳", "Jai Hind Salute", "0:04", "🇮🇳 Vande Mataram! Bharat Mata Ki Jai!", "Desi"));
        list.add(new VideoClip("🏆", "Victory Dance", "0:05", "🏆 Jeet gaye bhai jeet gaye! Chak de phatte!", "Celebration"));
        list.add(new VideoClip("🙌", "Balle Balle", "0:04", "🙌 Balle balle shava shava! Full Punjabi vibe!", "Desi"));
        list.add(new VideoClip("🎉", "Party Shuru", "0:06", "🎉 Abhi toh party shuru hui hai! DJ wale babu!", "Celebration"));
        list.add(new VideoClip("❤️", "Dil Se Love", "0:04", "❤️ Kuch kuch hota hai, tum nahi samjhoge!", "Love"));
        list.add(new VideoClip("🚀", "Full Speed", "0:03", "🚀 Ekdum bullet train speed! Mast!", "Trending"));
        return list;
    }

    // Visual GIF Category Model (As in Gboard / Tenor 2-column cards)
    public static class GifCategory {
        public final String id;
        public final String title;
        public final int[] gradientColors;
        public final String icon;

        public GifCategory(String id, String title, int[] gradientColors, String icon) {
            this.id = id;
            this.title = title;
            this.gradientColors = gradientColors;
            this.icon = icon;
        }
    }

    public static List<GifCategory> getGifVisualCategories() {
        List<GifCategory> categories = new ArrayList<>();
        categories.add(new GifCategory("Trending", "Trending", new int[]{0xFFFF512F, 0xFFDD2476}, "🔥"));
        categories.add(new GifCategory("Reactions", "Reactions", new int[]{0xFF4776E6, 0xFF8E54E9}, "🤩"));
        categories.add(new GifCategory("Greetings", "Greetings", new int[]{0xFF00B4DB, 0xFF0083B0}, "👋"));
        categories.add(new GifCategory("Perfect", "Perfect", new int[]{0xFFD31027, 0xFFEA384D}, "👌"));
        categories.add(new GifCategory("Welcome", "Welcome", new int[]{0xFFF7971E, 0xFFFFD200}, "🚪"));
        categories.add(new GifCategory("Congratulations", "Congratulations", new int[]{0xFF11998E, 0xFF38EF7D}, "🎉"));
        categories.add(new GifCategory("Thank you", "Thank you", new int[]{0xFF8A2387, 0xFFE94057}, "🙏"));
        categories.add(new GifCategory("Excited", "Excited", new int[]{0xFFF37335, 0xFFFDC830}, "⚡"));
        return categories;
    }

    // GIF Card Model & Library
    public static class GifCard {
        public final String emoji;
        public final String title;
        public final String message;
        public final String category;

        public GifCard(String emoji, String title, String message, String category) {
            this.emoji = emoji;
            this.title = title;
            this.message = message;
            this.category = category;
        }
    }

    public static List<GifCard> getAllGifCards() {
        List<GifCard> cards = new ArrayList<>();

        // 1. Trending
        cards.add(new GifCard("🔥", "Pushpa Fire Hai", "🔥 Jhukega nahi saala! Main jhukega nahi!", "Trending"));
        cards.add(new GifCard("🕺", "Tauba Tauba", "🕺 Killer dance moves on repeat! Tauba Tauba!", "Trending"));
        cards.add(new GifCard("🚀", "Full Speed", "🚀 100% Full Speed! Mast bullet train!", "Trending"));
        cards.add(new GifCard("😎", "Apna Swag", "😎 Apna swag alag hai boss! Superhit!", "Trending"));
        cards.add(new GifCard("⚡", "High Energy", "⚡ High voltage vibe! Sabse aage!", "Trending"));
        cards.add(new GifCard("✨", "Mindblowing", "✨ Absolutely mindblowing performance!", "Trending"));

        // 2. Reactions
        cards.add(new GifCard("😱", "Omg Shocking", "😱 Omg! Kya bola tune?! Sach mein?!", "Reactions"));
        cards.add(new GifCard("😂", "Hahaha Funny", "😂 Hahaha! Pet dard ho gaya haste haste!", "Reactions"));
        cards.add(new GifCard("🤔", "Thinking", "🤔 25 din mein paisa double? Sochna padega!", "Reactions"));
        cards.add(new GifCard("🤯", "Mind Blown", "🤯 Khatam! Bye bye! Tata! Goodbye gaya!", "Reactions"));
        cards.add(new GifCard("👀", "Dekh Raha Hai", "👀 Ohoo dekh raha hai na Vinod!", "Reactions"));
        cards.add(new GifCard("🥱", "Bore Ho Gaya", "🥱 Bas karo bhai, neend aa rahi hai!", "Reactions"));

        // 3. Greetings
        cards.add(new GifCard("☀️", "Good Morning", "☀️ Shubh Prabhat! Have a wonderful day ahead!", "Greetings"));
        cards.add(new GifCard("🌙", "Good Night", "🌙 Shubh Ratri! Sweet dreams! 😴", "Greetings"));
        cards.add(new GifCard("🙏", "Namaste", "🙏 Namaste ji! Pranam! Aap kaise hain?", "Greetings"));
        cards.add(new GifCard("👋", "Hello Dost", "👋 Hello friend! Kaise ho sab badhiya?", "Greetings"));
        cards.add(new GifCard("☕", "Chai Peelo", "☕ Hello friends! Garma garam Chai peelo!", "Greetings"));
        cards.add(new GifCard("✨", "Shubhkamnaye", "✨ Hardik shubhkamnayein aur ashirwad!", "Greetings"));

        // 4. Perfect
        cards.add(new GifCard("👌", "Ekdum Perfect", "👌 Chef's kiss! Ekdum lajawab aur perfect!", "Perfect"));
        cards.add(new GifCard("💯", "Super Solid", "💯 100% Ekdum sahi baat boli!", "Perfect"));
        cards.add(new GifCard("🎯", "Exact Target", "🎯 Nishana bilkul sahi laga! Spot on!", "Perfect"));
        cards.add(new GifCard("👏", "Kya Baat Hai", "👏 Wah! Kya baat hai! Lajawab!", "Perfect"));
        cards.add(new GifCard("⭐", "5-Star Rating", "⭐ Five star perfection! Kamaal kar diya!", "Perfect"));
        cards.add(new GifCard("👑", "Masterpiece", "👑 Ekdum shaandar aur zabardast!", "Perfect"));

        // 5. Welcome
        cards.add(new GifCard("🚪", "Swagatam", "🚪 Aapka swagat hai hamare dil mein!", "Welcome"));
        cards.add(new GifCard("💐", "Welcome Home", "💐 Ji aaya nu! Welcome home!", "Welcome"));
        cards.add(new GifCard("🤗", "Warm Welcome", "🤗 Dil khol ke tahe dil se swagat!", "Welcome"));
        cards.add(new GifCard("🌟", "Atithi Devo Bhava", "🌟 Welcome! Aapka aana shubh hua!", "Welcome"));
        cards.add(new GifCard("🎊", "Grand Entry", "🎊 Dhol nagade bajao! Grand welcome!", "Welcome"));

        // 6. Congratulations
        cards.add(new GifCard("🎉", "Bahut Badhai", "🎉 Bahut bahut badhai ho! Mubarak!", "Congratulations"));
        cards.add(new GifCard("🏆", "Victory Champ", "🏆 Chak de phatte! Jeet gaye champion!", "Congratulations"));
        cards.add(new GifCard("🎂", "Happy Birthday", "🎂 Janamdin Mubarak! Happy Birthday! 🥳", "Congratulations"));
        cards.add(new GifCard("💍", "Shaadi Mubarak", "💍 Happy Married Life! Mubarak ho!", "Congratulations"));
        cards.add(new GifCard("🪔", "Diwali Mubarak", "🪔 Happy Diwali! Deepawali mubarak!", "Congratulations"));
        cards.add(new GifCard("🌙", "Eid Mubarak", "🌙 Eid Mubarak aapko aur parivaar ko!", "Congratulations"));

        // 7. Thank you
        cards.add(new GifCard("🙏", "Dil Se Dhanyawad", "🙏 Dil se bahut bahut dhanyawad!", "Thank you"));
        cards.add(new GifCard("❤️", "Bohot Shukriya", "❤️ Bohot shukriya aapki madad ke liye!", "Thank you"));
        cards.add(new GifCard("🤝", "Thanks Dost", "🤝 Thanks dost! Yaari zindabad!", "Thank you"));
        cards.add(new GifCard("🌹", "Thank You So Much", "🌹 You are the best! Thank you so much!", "Thank you"));
        cards.add(new GifCard("🍫", "Sweet Thanks", "🍫 Mithaas bhara shukriya aapke liye!", "Thank you"));

        // 8. Excited
        cards.add(new GifCard("🤩", "Aag Laga Di", "🤩 Aag laga di bhai! Full on excitement!", "Excited"));
        cards.add(new GifCard("🕺", "Nacho Saare", "🕺 Nacho nacho! Full energy and fun!", "Excited"));
        cards.add(new GifCard("⚡", "Full Power", "⚡ 440 Volt current! Full on hype!", "Excited"));
        cards.add(new GifCard("🙌", "Balle Balle", "🙌 Balle balle shava shava! Mazaa aa gaya!", "Excited"));
        cards.add(new GifCard("🥳", "Party Shuru", "🥳 Abhi toh party shuru hui hai! Let's go!", "Excited"));
        cards.add(new GifCard("🔥", "Level Sabke Niklenge", "🔥 Josh ekdum high hai boss!", "Excited"));

        return cards;
    }
}
