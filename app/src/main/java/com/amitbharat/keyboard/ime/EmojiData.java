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
        // Celebrations & Greetings
        cards.add(new GifCard("🎉", "Badhai Ho", "🎉 Bahut Bahut Badhai Ho! Mubarak!", "Celebration"));
        cards.add(new GifCard("🙏", "Namaste", "🙏 Namaste! Pranam!", "Greeting"));
        cards.add(new GifCard("🎂", "Happy Birthday", "🎂 Janamdin Mubarak! Happy Birthday! 🥳", "Celebration"));
        cards.add(new GifCard("🪔", "Happy Diwali", "🪔 Deepawali ki hardik shubhkamnayein! Sukh aur samriddhi!", "Celebration"));
        cards.add(new GifCard("🌙", "Eid Mubarak", "🌙 Eid Mubarak aapko aur aapke parivaar ko!", "Celebration"));
        cards.add(new GifCard("✨", "Shubhkamnaye", "✨ Hardik Shubhkamnayein aur Aashirwad!", "Greeting"));
        cards.add(new GifCard("☀️", "Good Morning", "☀️ Shubh Prabhat! Have a wonderful day!", "Greeting"));
        cards.add(new GifCard("🌙", "Good Night", "🌙 Shubh Ratri! Sweet Dreams! 😴", "Greeting"));

        // Desi & Memes
        cards.add(new GifCard("☕", "Chai Peelo", "☕ Garma garam Chai peelo doston!", "Desi"));
        cards.add(new GifCard("🇮🇳", "Jai Hind", "🇮🇳 Jai Hind! Bharat Mata Ki Jai!", "Desi"));
        cards.add(new GifCard("💃", "Nacho Party", "💃 Nacho nacho! Party time! 🕺🎉", "Celebration"));
        cards.add(new GifCard("🙌", "Balle Balle", "🙌 Balle Balle! Chak De Phatte!", "Desi"));
        cards.add(new GifCard("🔥", "Dhamaka", "🔥 Ekdum Dhamaka! Kya baat hai!", "Trending"));
        cards.add(new GifCard("😎", "Apna Swag", "😎 Apna swag alag hai boss!", "Trending"));
        cards.add(new GifCard("😂", "Hahaha", "😂 Hahaha! Very funny! Mazaa aa gaya!", "Memes"));
        cards.add(new GifCard("👍", "Zabardast", "👍 Ekdum Zabardast! Superhit!", "Trending"));
        cards.add(new GifCard("❤️", "Dil Se", "❤️ Dil se bahut bahut shukriya!", "Love"));
        cards.add(new GifCard("🤝", "Dosti Forever", "🤝 Dosti zindabad! Yaari forever!", "Love"));
        cards.add(new GifCard("🤩", "Mindblowing", "🤩 Mindblowing performance!", "Trending"));
        cards.add(new GifCard("🚀", "Ekdum Mast", "🚀 Ekdum Mast! Full speed!", "Trending"));
        cards.add(new GifCard("👑", "King Vibe", "👑 Raja beta! Shaandaar entry!", "Memes"));
        cards.add(new GifCard("💯", "Super Solid", "💯 100% Ekdum sahi baat boli!", "Trending"));
        return cards;
    }
}
