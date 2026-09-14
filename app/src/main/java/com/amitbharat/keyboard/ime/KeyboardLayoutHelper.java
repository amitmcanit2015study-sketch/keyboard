package com.amitbharat.keyboard.ime;

import com.amitbharat.keyboard.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeyboardLayoutHelper {

    public static final int MODE_ALPHA = 0;
    public static final int MODE_NUMERIC = 1;
    public static final int MODE_SYMBOLS = 2;
    public static final int MODE_NUMPAD = 3;

    public static List<List<KeyboardKey>> createQwertyLayout(boolean isShifted, boolean isCapsLock) {
        List<List<KeyboardKey>> rows = new ArrayList<>();

        // Row 1: Q W E R T Y U I O P (Hints: 1 2 3 4 5 6 7 8 9 0)
        String[] r1Chars = {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p"};
        String[] r1Hints = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        List<KeyboardKey> row1 = new ArrayList<>();
        for (int i = 0; i < r1Chars.length; i++) {
            String ch = (isShifted || isCapsLock) ? r1Chars[i].toUpperCase() : r1Chars[i];
            row1.add(new KeyboardKey(ch.charAt(0), ch, r1Hints[i], 1.0f));
        }
        rows.add(row1);

        // Row 2: A S D F G H J K L (Hints: @ # $ % & - + ( ))
        String[] r2Chars = {"a", "s", "d", "f", "g", "h", "j", "k", "l"};
        String[] r2Hints = {"@", "#", "$", "%", "&", "-", "+", "(", ")"};
        List<KeyboardKey> row2 = new ArrayList<>();
        for (int i = 0; i < r2Chars.length; i++) {
            String ch = (isShifted || isCapsLock) ? r2Chars[i].toUpperCase() : r2Chars[i];
            row2.add(new KeyboardKey(ch.charAt(0), ch, r2Hints[i], 1.0f));
        }
        rows.add(row2);

        // Row 3: [SHIFT] Z X C V B N M [BACKSPACE]
        List<KeyboardKey> row3 = new ArrayList<>();
        int shiftIcon = isCapsLock ? R.drawable.ic_shift_caps : R.drawable.ic_shift;
        row3.add(new KeyboardKey(KeyboardKey.CODE_SHIFT, "", shiftIcon, 1.4f, true, isShifted || isCapsLock));

        String[] r3Chars = {"z", "x", "c", "v", "b", "n", "m"};
        String[] r3Hints = {"*", "\"", "'", ":", ";", "!", "?"};
        for (int i = 0; i < r3Chars.length; i++) {
            String ch = (isShifted || isCapsLock) ? r3Chars[i].toUpperCase() : r3Chars[i];
            row3.add(new KeyboardKey(ch.charAt(0), ch, r3Hints[i], 1.0f));
        }
        row3.add(new KeyboardKey(KeyboardKey.CODE_BACKSPACE, "", R.drawable.ic_backspace, 1.4f, true, false));
        rows.add(row3);

        // Row 4: [?123] [EMOJI] [,] [   SPACE   ] [.] [ENTER]
        List<KeyboardKey> row4 = new ArrayList<>();
        row4.add(new KeyboardKey(KeyboardKey.CODE_MODE_NUM, "?123", 0, 1.4f, true, false));
        row4.add(new KeyboardKey(KeyboardKey.CODE_EMOJI, "", R.drawable.ic_emoji, 1.1f, true, false));
        row4.add(new KeyboardKey(',', ",", null, 1.0f));
        row4.add(new KeyboardKey(KeyboardKey.CODE_SPACE, "Space", null, 4.2f));
        row4.add(new KeyboardKey('.', ".", null, 1.0f));
        row4.add(new KeyboardKey(KeyboardKey.CODE_ENTER, "", R.drawable.ic_enter, 1.4f, true, true));
        rows.add(row4);

        return rows;
    }

    public static List<List<KeyboardKey>> createNumericLayout() {
        List<List<KeyboardKey>> rows = new ArrayList<>();

        // Row 1: 1 2 3 4 5 6 7 8 9 0
        String[] r1 = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        List<KeyboardKey> row1 = new ArrayList<>();
        for (String ch : r1) {
            row1.add(new KeyboardKey(ch.charAt(0), ch, null, 1.0f));
        }
        rows.add(row1);

        // Row 2: @ # $ % & - + ( ) /
        String[] r2 = {"@", "#", "$", "%", "&", "-", "+", "(", ")", "/"};
        List<KeyboardKey> row2 = new ArrayList<>();
        for (String ch : r2) {
            row2.add(new KeyboardKey(ch.charAt(0), ch, null, 1.0f));
        }
        rows.add(row2);

        // Row 3: [=\<] * " ' : ; ! ? [BACKSPACE]
        List<KeyboardKey> row3 = new ArrayList<>();
        row3.add(new KeyboardKey(KeyboardKey.CODE_MODE_SYM, "=\\<", 0, 1.4f, true, false));
        String[] r3 = {"*", "\"", "'", ":", ";", "!", "?"};
        for (String ch : r3) {
            row3.add(new KeyboardKey(ch.charAt(0), ch, null, 1.0f));
        }
        row3.add(new KeyboardKey(KeyboardKey.CODE_BACKSPACE, "", R.drawable.ic_backspace, 1.4f, true, false));
        rows.add(row3);

        // Row 4: [ABC] [EMOJI] [,] [   SPACE   ] [.] [ENTER]
        List<KeyboardKey> row4 = new ArrayList<>();
        row4.add(new KeyboardKey(KeyboardKey.CODE_MODE_ALPHA, "ABC", 0, 1.4f, true, false));
        row4.add(new KeyboardKey(KeyboardKey.CODE_EMOJI, "", R.drawable.ic_emoji, 1.1f, true, false));
        row4.add(new KeyboardKey(',', ",", null, 1.0f));
        row4.add(new KeyboardKey(KeyboardKey.CODE_SPACE, "Space", null, 4.2f));
        row4.add(new KeyboardKey('.', ".", null, 1.0f));
        row4.add(new KeyboardKey(KeyboardKey.CODE_ENTER, "", R.drawable.ic_enter, 1.4f, true, true));
        rows.add(row4);

        return rows;
    }

    public static List<List<KeyboardKey>> createSymbolsLayout() {
        List<List<KeyboardKey>> rows = new ArrayList<>();

        // Row 1: ~ ` | • √ π ÷ × ¶ ∆
        String[] r1 = {"~", "`", "|", "•", "√", "π", "÷", "×", "¶", "∆"};
        List<KeyboardKey> row1 = new ArrayList<>();
        for (String ch : r1) {
            row1.add(new KeyboardKey(ch.charAt(0), ch, null, 1.0f));
        }
        rows.add(row1);

        // Row 2: £ ¢ € ¥ ^ ° = { } \
        String[] r2 = {"£", "¢", "€", "¥", "^", "°", "=", "{", "}", "\\"};
        List<KeyboardKey> row2 = new ArrayList<>();
        for (String ch : r2) {
            row2.add(new KeyboardKey(ch.charAt(0), ch, null, 1.0f));
        }
        rows.add(row2);

        // Row 3: [?123] % © ® ™ ✓ [ ] [BACKSPACE]
        List<KeyboardKey> row3 = new ArrayList<>();
        row3.add(new KeyboardKey(KeyboardKey.CODE_MODE_NUM, "?123", 0, 1.4f, true, false));
        String[] r3 = {"%", "©", "®", "™", "✓", "[", "]"};
        for (String ch : r3) {
            row3.add(new KeyboardKey(ch.charAt(0), ch, null, 1.0f));
        }
        row3.add(new KeyboardKey(KeyboardKey.CODE_BACKSPACE, "", R.drawable.ic_backspace, 1.4f, true, false));
        rows.add(row3);

        // Row 4: [ABC] [EMOJI] [,] [   SPACE   ] [.] [ENTER]
        List<KeyboardKey> row4 = new ArrayList<>();
        row4.add(new KeyboardKey(KeyboardKey.CODE_MODE_ALPHA, "ABC", 0, 1.4f, true, false));
        row4.add(new KeyboardKey(KeyboardKey.CODE_EMOJI, "", R.drawable.ic_emoji, 1.1f, true, false));
        row4.add(new KeyboardKey(',', ",", null, 1.0f));
        row4.add(new KeyboardKey(KeyboardKey.CODE_SPACE, "Space", null, 4.2f));
        row4.add(new KeyboardKey('.', ".", null, 1.0f));
        row4.add(new KeyboardKey(KeyboardKey.CODE_ENTER, "", R.drawable.ic_enter, 1.4f, true, true));
        rows.add(row4);

        return rows;
    }

    public static List<List<KeyboardKey>> createNumpadLayout() {
        List<List<KeyboardKey>> rows = new ArrayList<>();

        // Row 1: 1 2 3 [BACKSPACE]
        List<KeyboardKey> row1 = new ArrayList<>();
        row1.add(new KeyboardKey('1', "1", null, 1.0f));
        row1.add(new KeyboardKey('2', "2", null, 1.0f));
        row1.add(new KeyboardKey('3', "3", null, 1.0f));
        row1.add(new KeyboardKey(KeyboardKey.CODE_BACKSPACE, "", R.drawable.ic_backspace, 1.0f, true, false));
        rows.add(row1);

        // Row 2: 4 5 6 -
        List<KeyboardKey> row2 = new ArrayList<>();
        row2.add(new KeyboardKey('4', "4", null, 1.0f));
        row2.add(new KeyboardKey('5', "5", null, 1.0f));
        row2.add(new KeyboardKey('6', "6", null, 1.0f));
        row2.add(new KeyboardKey('-', "-", null, 1.0f));
        rows.add(row2);

        // Row 3: 7 8 9 +
        List<KeyboardKey> row3 = new ArrayList<>();
        row3.add(new KeyboardKey('7', "7", null, 1.0f));
        row3.add(new KeyboardKey('8', "8", null, 1.0f));
        row3.add(new KeyboardKey('9', "9", null, 1.0f));
        row3.add(new KeyboardKey('+', "+", null, 1.0f));
        rows.add(row3);

        // Row 4: [ABC] 0 . [ENTER]
        List<KeyboardKey> row4 = new ArrayList<>();
        row4.add(new KeyboardKey(KeyboardKey.CODE_MODE_ALPHA, "ABC", 0, 1.0f, true, false));
        row4.add(new KeyboardKey('0', "0", null, 1.0f));
        row4.add(new KeyboardKey('.', ".", null, 1.0f));
        row4.add(new KeyboardKey(KeyboardKey.CODE_ENTER, "", R.drawable.ic_enter, 1.0f, true, true));
        rows.add(row4);

        return rows;
    }

    public static final List<String> EMOJIS = Arrays.asList(
            "😀", "😃", "😄", "😁", "😆", "😅", "😂", "🤣", "😊", "😇",
            "🙂", "🙃", "😉", "😌", "😍", "🥰", "😘", "😗", "😙", "😚",
            "😋", "😛", "😝", "😜", "🤪", "🤨", "🧐", "🤓", "😎", "🤩",
            "🥳", "😏", "😒", "😞", "😔", "😟", "😕", "🙁", "☹️", "😣",
            "😖", "😫", "😩", "🥺", "😢", "😭", "😤", "😠", "😡", "🤬",
            "🤯", "😳", "🥵", "🥶", "😱", "😨", "😰", "😥", "😓", "🤗",
            "🤔", "🤭", "🤫", "🤥", "😶", "😐", "😑", "😬", "🙄", "😯",
            "👋", "🤚", "🖐️", "✋", "🖖", "👌", "🤌", "🤏", "✌️", "🤞",
            "🤟", "🤘", "🤙", "👈", "👉", "👆", "🖕", "👇", "☝️", "👍",
            "👎", "✊", "👊", "🤛", "🤜", "👏", "🙌", "👐", "🤲", "🤝",
            "🙏", "✍️", "💅", "🤳", "💪", "❤️", "🧡", "💛", "💚", "💙",
            "💜", "🖤", "🤍", "🤎", "💔", "❣️", "💕", "💞", "💓", "💗",
            "💖", "💘", "💝", "💟", "🇮🇳", "🔥", "✨", "⭐", "🌟", "🎉"
    );
}
