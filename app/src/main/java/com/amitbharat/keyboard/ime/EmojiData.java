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
        list.add(new VideoClip("😎", "Hero No. 1", "0:04", "😎 Sona kitna sona hai! Swag se karenge swagat!", "Bollywood"));
        return list;
    }

    // GIF Card Model & Library
    public static class GifCard {
        public final String emoji;

        public final String title;
        public final String message;
        public final String category;
        public final String assetFile;

        public GifCard(String emoji, String title, String message, String category, String assetFile) {
            this.emoji = emoji;
            this.title = title;
            this.message = message;
            this.category = category;
            this.assetFile = assetFile;
        }

        public GifCard(String emoji, String title, String message, String category) {
            this(emoji, title, message, category, "awesome_kitty.gif");
        }
    }

    public static List<GifCard> getAllGifCards() {
        List<GifCard> cards = new ArrayList<>();

        // 1. Trending
        cards.add(new GifCard("😻", "Awesome Kitty", "AWESOME! Hello Kitty", "Trending", "awesome_kitty.gif"));
        cards.add(new GifCard("🔥", "Pushpa Fire Hai", "Jhukega nahi saala! Fire hai!", "Trending", "pushpa_fire.gif"));
        cards.add(new GifCard("🕺", "Tauba Tauba", "Tauba Tauba killer dance moves!", "Trending", "tauba_tauba.gif"));
        cards.add(new GifCard("🕶️", "Babu Rao Swag", "Utha le re baba style!", "Trending", "babu_rao.gif"));
        cards.add(new GifCard("🤯", "Mind Blown", "Khatam! Bye bye! Tata!", "Trending", "mind_blown.gif"));
        cards.add(new GifCard("🎉", "Party Shuru", "Abhi toh party shuru hui hai!", "Trending", "party_celebration.gif"));

        // 2. Reactions
        cards.add(new GifCard("🤯", "Mind Blown", "Mind blown! Total shocker!", "Reactions", "mind_blown.gif"));
        cards.add(new GifCard("😂", "Hahaha Funny", "LMAO pet dard ho gaya!", "Reactions", "laughing_cat.gif"));
        cards.add(new GifCard("🤔", "Thinking", "25 din mein paisa double? Sochna padega!", "Reactions", "thinking.gif"));
        cards.add(new GifCard("😻", "Awesome Wow", "Awesome! So pretty and cute!", "Reactions", "awesome_kitty.gif"));
        cards.add(new GifCard("💯", "Super Solid", "100% Ekdum sahi baat boli!", "Reactions", "super_hit.gif"));
        cards.add(new GifCard("🔥", "Full Fire", "Aag laga di boss!", "Reactions", "pushpa_fire.gif"));

        // 3. Greetings
        cards.add(new GifCard("☀️", "Good Morning", "Shubh Prabhat! Have a wonderful day!", "Greetings", "good_morning.gif"));
        cards.add(new GifCard("🌙", "Good Night", "Shubh Ratri! Sweet dreams!", "Greetings", "good_night.gif"));
        cards.add(new GifCard("🙏", "Namaste Ji", "Namaste! Pranam aap kaise hain?", "Greetings", "namaste.gif"));
        cards.add(new GifCard("☕", "Chai Peelo", "Hello friends! Garma garam Chai peelo!", "Greetings", "chai_peelo.gif"));
        cards.add(new GifCard("😻", "Hello Cute", "Hello friend! Kaise ho sab badhiya?", "Greetings", "awesome_kitty.gif"));
        cards.add(new GifCard("❤️", "Pyar Bhara", "Dil se shubh kamnayein!", "Greetings", "heart_love.gif"));

        // 4. Perfect
        cards.add(new GifCard("💯", "Ekdum Perfect", "100% Ekdum lajawab aur spot on!", "Perfect", "super_hit.gif"));
        cards.add(new GifCard("😻", "Awesome Work", "Super awesome performance!", "Perfect", "awesome_kitty.gif"));
        cards.add(new GifCard("🏆", "Victory Champ", "Chak de phatte! Number 1!", "Perfect", "congrats_trophy.gif"));
        cards.add(new GifCard("🔥", "Superhit Fire", "Next level shaandar!", "Perfect", "pushpa_fire.gif"));

        // 5. Welcome
        cards.add(new GifCard("🙏", "Swagatam", "Aapka aana shubh hua! Swagatam!", "Welcome", "namaste.gif"));
        cards.add(new GifCard("💐", "Warm Welcome", "Dil khol ke tahe dil se swagat!", "Welcome", "thank_you.gif"));
        cards.add(new GifCard("🎉", "Grand Entry", "Dhol nagade bajao! Grand welcome!", "Welcome", "party_celebration.gif"));
        cards.add(new GifCard("😻", "Welcome Cute", "Welcome home friend!", "Welcome", "awesome_kitty.gif"));

        // 6. Congratulations
        cards.add(new GifCard("🎉", "Mubarak Ho!", "Bahut bahut badhai aur mubarak!", "Congratulations", "party_celebration.gif"));
        cards.add(new GifCard("🏆", "Winner Trophy", "Jeet gaye champion! Shandar jeet!", "Congratulations", "congrats_trophy.gif"));
        cards.add(new GifCard("💯", "Top Score", "100% Out of 100! Superhit!", "Congratulations", "super_hit.gif"));
        cards.add(new GifCard("😻", "Awesome Win", "Awesome achievement! Proud of you!", "Congratulations", "awesome_kitty.gif"));

        // 7. Thank you
        cards.add(new GifCard("🙏", "Dil Se Shukriya", "Dil se bahut bahut dhanyawad!", "Thank you", "namaste.gif"));
        cards.add(new GifCard("💐", "Thank You So Much", "You are the best! Thank you!", "Thank you", "thank_you.gif"));
        cards.add(new GifCard("❤️", "Bohot Shukriya", "Bohot shukriya aapki madad ke liye!", "Thank you", "heart_love.gif"));
        cards.add(new GifCard("☕", "Chai Treat", "Chai ki treat meri taraf se!", "Thank you", "chai_peelo.gif"));

        // 8. Excited
        cards.add(new GifCard("🕺", "Tauba Tauba", "Nacho nacho full on masti!", "Excited", "tauba_tauba.gif"));
        cards.add(new GifCard("🔥", "Aag Laga Di", "Full 440 volt current vibe!", "Excited", "pushpa_fire.gif"));
        cards.add(new GifCard("🎉", "Party All Night", "Abhi toh party shuru hui hai!", "Excited", "party_celebration.gif"));
        cards.add(new GifCard("😻", "Super Excited", "Awesome excitement and joy!", "Excited", "awesome_kitty.gif"));

        return cards;
    }
}

