package com.amitbharat.hindikeyboard.keyboard

import android.view.inputmethod.EditorInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amitbharat.hindikeyboard.emoji.EmojiData
import com.amitbharat.hindikeyboard.suggestions.TypingMode
import com.amitbharat.hindikeyboard.theme.KeyboardColors
import com.amitbharat.hindikeyboard.theme.KeyboardThemeManager
import kotlinx.coroutines.delay

@Composable
fun KeyboardScreen(
    state: KeyboardUiState,
    onKeyPress: (String) -> Unit,
    onBackspace: () -> Unit,
    onSpace: () -> Unit,
    onEnter: () -> Unit,
    onLanguageToggle: () -> Unit,
    onShiftToggle: () -> Unit,
    onLayoutChange: (KeyboardLayoutType) -> Unit,
    onSuggestionClick: (String) -> Unit,
    onCursorMove: (Int) -> Unit,
    onVoiceClick: () -> Unit,
    onSettingsClick: () -> Unit,
    imeAction: Int = EditorInfo.IME_ACTION_DONE
) {
    val isDark = isSystemInDarkTheme()
    val colors = KeyboardThemeManager.getColors(state.activeTheme, isDark)

    // Elevated bottom padding to stay cleanly above device navigation bar
    val navBottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val bottomInset = if (navBottomPadding > 0.dp) navBottomPadding + 8.dp else 22.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(bottom = bottomInset)
    ) {
        // 1. Suggestion & Voice Action Bar
        SuggestionBar(
            suggestions = state.suggestions,
            mode = state.typingMode,
            isListeningVoice = state.isListeningVoice,
            voicePartialText = state.voicePartialText,
            colors = colors,
            onSuggestionClick = onSuggestionClick,
            onLanguageToggle = onLanguageToggle,
            onLayoutChange = onLayoutChange,
            onVoiceClick = onVoiceClick
        )

        // 2. Keyboard Views (QWERTY, Symbols, More Symbols, Emoji, Numeric)
        when (state.layoutType) {
            KeyboardLayoutType.QWERTY -> {
                QwertyLayout(
                    state = state,
                    colors = colors,
                    onKeyPress = onKeyPress,
                    onBackspace = onBackspace,
                    onSpace = onSpace,
                    onEnter = onEnter,
                    onLanguageToggle = onLanguageToggle,
                    onShiftToggle = onShiftToggle,
                    onLayoutChange = onLayoutChange,
                    onCursorMove = onCursorMove,
                    imeAction = imeAction
                )
            }
            KeyboardLayoutType.SYMBOLS -> {
                SymbolsLayout(
                    state = state,
                    colors = colors,
                    onKeyPress = onKeyPress,
                    onBackspace = onBackspace,
                    onSpace = onSpace,
                    onEnter = onEnter,
                    onLayoutChange = onLayoutChange,
                    imeAction = imeAction
                )
            }
            KeyboardLayoutType.MORE_SYMBOLS -> {
                MoreSymbolsLayout(
                    state = state,
                    colors = colors,
                    onKeyPress = onKeyPress,
                    onBackspace = onBackspace,
                    onSpace = onSpace,
                    onEnter = onEnter,
                    onLayoutChange = onLayoutChange,
                    imeAction = imeAction
                )
            }
            KeyboardLayoutType.EMOJI -> {
                EmojiLayout(
                    colors = colors,
                    onEmojiClick = onKeyPress,
                    onBackspace = onBackspace,
                    onBackToQwerty = { onLayoutChange(KeyboardLayoutType.QWERTY) }
                )
            }
            KeyboardLayoutType.NUMERIC -> {
                NumericLayout(
                    colors = colors,
                    onKeyPress = onKeyPress,
                    onBackspace = onBackspace,
                    onSpace = onSpace,
                    onEnter = onEnter,
                    onLayoutChange = onLayoutChange,
                    imeAction = imeAction
                )
            }
        }
    }
}

@Composable
fun SuggestionBar(
    suggestions: List<String>,
    mode: TypingMode,
    isListeningVoice: Boolean = false,
    voicePartialText: String = "",
    colors: KeyboardColors,
    onSuggestionClick: (String) -> Unit,
    onLanguageToggle: () -> Unit,
    onLayoutChange: (KeyboardLayoutType) -> Unit,
    onVoiceClick: () -> Unit
) {
    val isHindi = mode == TypingMode.HINDI_TRANSLITERATION

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(colors.suggestionBarBackground)
            .padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1st: HN/EN toggle button
        SpecialKeyItem(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .width(44.dp)
                .fillMaxHeight(),
            colors = colors,
            onClick = onLanguageToggle,
            isHighlighted = isHindi
        ) {
            Text(
                text = if (isHindi) "HN" else "EN",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (isHindi) Color.White else colors.specialKeyTextColor
            )
        }

        Spacer(Modifier.width(4.dp))

        // 2nd: Emoji Button
        IconButton(
            onClick = { onLayoutChange(KeyboardLayoutType.EMOJI) },
            modifier = Modifier.size(38.dp)
        ) {
            Icon(Icons.Default.EmojiEmotions, contentDescription = "Emoji", tint = colors.specialKeyTextColor, modifier = Modifier.size(22.dp))
        }

        // 3rd: Live Voice Typing Stream or Suggestions List
        if (isListeningVoice) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (voicePartialText.isNotEmpty()) voicePartialText else if (isHindi) "(Hindi Voice Typing)" else "(English Voice Typing)",
                    color = Color(0xFFE53935),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        } else {
            LazyRow(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(suggestions) { item ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp, vertical = 5.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(colors.keyBackground)
                            .clickable { onSuggestionClick(item) }
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item,
                            color = colors.suggestionTextColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // 4th: Live Continuous Voice Mic Button
        IconButton(
            onClick = onVoiceClick,
            modifier = Modifier.size(38.dp)
        ) {
            Icon(
                Icons.Default.Mic,
                contentDescription = "Voice Input",
                tint = if (isListeningVoice) Color(0xFFE53935) else colors.accentColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun QwertyLayout(
    state: KeyboardUiState,
    colors: KeyboardColors,
    onKeyPress: (String) -> Unit,
    onBackspace: () -> Unit,
    onSpace: () -> Unit,
    onEnter: () -> Unit,
    onLanguageToggle: () -> Unit,
    onShiftToggle: () -> Unit,
    onLayoutChange: (KeyboardLayoutType) -> Unit,
    onCursorMove: (Int) -> Unit,
    imeAction: Int
) {
    val row1 = listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p")
    val row2 = listOf("a", "s", "d", "f", "g", "h", "j", "k", "l")
    val row3 = listOf("z", "x", "c", "v", "b", "n", "m")
    val numberRow = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 2.dp)
    ) {
        // Dedicated Number Row (Font 26sp, Height 52dp)
        if (state.showNumberRow) {
            Row(modifier = Modifier.fillMaxWidth().height(52.dp)) {
                numberRow.forEach { num ->
                    KeyItem(text = num, modifier = Modifier.weight(1f), colors = colors, fontSize = 26.sp, onClick = { onKeyPress(num) })
                }
            }
        }

        // Row 1 (Q W E R T Y U I O P) - Height 60dp, Font 29sp
        Row(modifier = Modifier.fillMaxWidth().height(60.dp)) {
            row1.forEach { char ->
                val displayText = if (state.shiftState != ShiftState.OFF) char.uppercase() else char
                KeyItem(text = displayText, modifier = Modifier.weight(1f), colors = colors, fontSize = 29.sp, onClick = { onKeyPress(displayText) })
            }
        }

        // Row 2 (A S D F G H J K L) - Height 60dp, Font 29sp
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 16.dp)
        ) {
            row2.forEach { char ->
                val displayText = if (state.shiftState != ShiftState.OFF) char.uppercase() else char
                KeyItem(text = displayText, modifier = Modifier.weight(1f), colors = colors, fontSize = 29.sp, onClick = { onKeyPress(displayText) })
            }
        }

        // Row 3 (Shift + Z X C V B N M + Continuous Backspace) - Height 60dp, Font 29sp
        Row(modifier = Modifier.fillMaxWidth().height(60.dp)) {
            // Shift Key
            SpecialKeyItem(
                modifier = Modifier.weight(1.5f),
                colors = colors,
                onClick = onShiftToggle,
                isHighlighted = state.shiftState != ShiftState.OFF
            ) {
                Icon(
                    if (state.shiftState == ShiftState.CAPS_LOCK) Icons.Default.KeyboardCapslock else Icons.Default.ArrowUpward,
                    contentDescription = "Shift",
                    tint = if (state.shiftState != ShiftState.OFF) colors.accentColor else colors.specialKeyTextColor,
                    modifier = Modifier.size(26.dp)
                )
            }

            row3.forEach { char ->
                val displayText = if (state.shiftState != ShiftState.OFF) char.uppercase() else char
                KeyItem(text = displayText, modifier = Modifier.weight(1f), colors = colors, fontSize = 29.sp, onClick = { onKeyPress(displayText) })
            }

            // Continuous Repeating Backspace Key
            RepeatingSpecialKeyItem(
                modifier = Modifier.weight(1.5f),
                colors = colors,
                onTrigger = onBackspace
            ) {
                Icon(Icons.Default.Backspace, contentDescription = "Backspace", tint = colors.specialKeyTextColor, modifier = Modifier.size(26.dp))
            }
        }

        // Row 4 (123 Numeric, #+= Special Characters, Blank Spacebar, Period, Enter) - Height 62dp
        Row(modifier = Modifier.fillMaxWidth().height(62.dp)) {
            // Separate 123 Numeric Button
            SpecialKeyItem(
                modifier = Modifier.weight(1.3f),
                colors = colors,
                onClick = { onLayoutChange(KeyboardLayoutType.NUMERIC) }
            ) {
                Text("123", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = colors.specialKeyTextColor)
            }

            // Separate #+= Special Character Button
            SpecialKeyItem(
                modifier = Modifier.weight(1.3f),
                colors = colors,
                onClick = { onLayoutChange(KeyboardLayoutType.SYMBOLS) }
            ) {
                Text("#+=", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = colors.specialKeyTextColor)
            }

            // Spacebar (with swipe cursor navigation)
            var dragAccumulator by remember { mutableFloatStateOf(0f) }
            Box(
                modifier = Modifier
                    .weight(4.4f)
                    .fillMaxHeight()
                    .padding(horizontal = 2.5.dp, vertical = 3.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(colors.keyBackground)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                dragAccumulator += dragAmount.x
                                if (dragAccumulator > 28f) {
                                    onCursorMove(1)
                                    dragAccumulator = 0f
                                } else if (dragAccumulator < -28f) {
                                    onCursorMove(-1)
                                    dragAccumulator = 0f
                                }
                            },
                            onDragEnd = { dragAccumulator = 0f }
                        )
                    }
                    .clickable { onSpace() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Developed by Amit Bharat",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.keyTextColor.copy(alpha = 0.55f)
                )
            }

            // Period Key
            KeyItem(text = ".", modifier = Modifier.weight(1f), colors = colors, fontSize = 26.sp, onClick = { onKeyPress(".") })

            // Enter / Action Key
            SpecialKeyItem(
                modifier = Modifier.weight(1.5f),
                colors = colors,
                onClick = onEnter,
                isHighlighted = true
            ) {
                val icon = when (imeAction) {
                    EditorInfo.IME_ACTION_SEARCH -> Icons.Default.Search
                    EditorInfo.IME_ACTION_SEND -> Icons.Default.Send
                    EditorInfo.IME_ACTION_NEXT -> Icons.Default.ArrowForward
                    EditorInfo.IME_ACTION_GO -> Icons.Default.ArrowForward
                    else -> Icons.Default.KeyboardReturn
                }
                Icon(icon, contentDescription = "Enter", tint = Color.White, modifier = Modifier.size(26.dp))
            }
        }
    }
}

@Composable
fun SymbolsLayout(
    state: KeyboardUiState,
    colors: KeyboardColors,
    onKeyPress: (String) -> Unit,
    onBackspace: () -> Unit,
    onSpace: () -> Unit,
    onEnter: () -> Unit,
    onLayoutChange: (KeyboardLayoutType) -> Unit,
    imeAction: Int
) {
    val r1 = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
    val r2 = listOf("@", "#", "$", "_", "&", "-", "+", "(", ")", "/")
    val r3 = listOf("*", "\"", "'", ":", ";", "!", "?")

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp, vertical = 2.dp)) {
        Row(modifier = Modifier.fillMaxWidth().height(60.dp)) {
            r1.forEach { KeyItem(text = it, modifier = Modifier.weight(1f), colors = colors, fontSize = 26.sp, onClick = { onKeyPress(it) }) }
        }
        Row(modifier = Modifier.fillMaxWidth().height(60.dp)) {
            r2.forEach { KeyItem(text = it, modifier = Modifier.weight(1f), colors = colors, fontSize = 26.sp, onClick = { onKeyPress(it) }) }
        }
        Row(modifier = Modifier.fillMaxWidth().height(60.dp)) {
            SpecialKeyItem(modifier = Modifier.weight(1.5f), colors = colors, onClick = { onLayoutChange(KeyboardLayoutType.MORE_SYMBOLS) }) {
                Text("=/<", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = colors.specialKeyTextColor)
            }
            r3.forEach { KeyItem(text = it, modifier = Modifier.weight(1f), colors = colors, fontSize = 26.sp, onClick = { onKeyPress(it) }) }
            RepeatingSpecialKeyItem(modifier = Modifier.weight(1.5f), colors = colors, onTrigger = onBackspace) {
                Icon(Icons.Default.Backspace, contentDescription = "Backspace", tint = colors.specialKeyTextColor, modifier = Modifier.size(26.dp))
            }
        }
        Row(modifier = Modifier.fillMaxWidth().height(62.dp)) {
            SpecialKeyItem(modifier = Modifier.weight(1.5f), colors = colors, onClick = { onLayoutChange(KeyboardLayoutType.QWERTY) }) {
                Text("ABC", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = colors.specialKeyTextColor)
            }
            KeyItem(text = ",", modifier = Modifier.weight(1f), colors = colors, fontSize = 26.sp, onClick = { onKeyPress(",") })
            Box(
                modifier = Modifier
                    .weight(4.5f)
                    .fillMaxHeight()
                    .padding(horizontal = 2.5.dp, vertical = 3.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(colors.keyBackground)
                    .clickable { onSpace() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Developed by Amit Bharat",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.keyTextColor.copy(alpha = 0.55f)
                )
            }
            KeyItem(text = ".", modifier = Modifier.weight(1f), colors = colors, fontSize = 26.sp, onClick = { onKeyPress(".") })
            SpecialKeyItem(modifier = Modifier.weight(1.5f), colors = colors, onClick = onEnter, isHighlighted = true) {
                Icon(Icons.Default.KeyboardReturn, contentDescription = "Enter", tint = Color.White, modifier = Modifier.size(26.dp))
            }
        }
    }
}

@Composable
fun MoreSymbolsLayout(
    state: KeyboardUiState,
    colors: KeyboardColors,
    onKeyPress: (String) -> Unit,
    onBackspace: () -> Unit,
    onSpace: () -> Unit,
    onEnter: () -> Unit,
    onLayoutChange: (KeyboardLayoutType) -> Unit,
    imeAction: Int
) {
    val r1 = listOf("~", "`", "|", "•", "√", "π", "÷", "×", "¶", "∆")
    val r2 = listOf("£", "¥", "$", "¢", "^", "°", "=", "{", "}", "\\")
    val r3 = listOf("%", "©", "®", "™", "✓", "[", "]")

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp, vertical = 2.dp)) {
        Row(modifier = Modifier.fillMaxWidth().height(60.dp)) {
            r1.forEach { KeyItem(text = it, modifier = Modifier.weight(1f), colors = colors, fontSize = 25.sp, onClick = { onKeyPress(it) }) }
        }
        Row(modifier = Modifier.fillMaxWidth().height(60.dp)) {
            r2.forEach { KeyItem(text = it, modifier = Modifier.weight(1f), colors = colors, fontSize = 25.sp, onClick = { onKeyPress(it) }) }
        }
        Row(modifier = Modifier.fillMaxWidth().height(60.dp)) {
            SpecialKeyItem(modifier = Modifier.weight(1.5f), colors = colors, onClick = { onLayoutChange(KeyboardLayoutType.SYMBOLS) }) {
                Text("1/2", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = colors.specialKeyTextColor)
            }
            r3.forEach { KeyItem(text = it, modifier = Modifier.weight(1f), colors = colors, fontSize = 25.sp, onClick = { onKeyPress(it) }) }
            RepeatingSpecialKeyItem(modifier = Modifier.weight(1.5f), colors = colors, onTrigger = onBackspace) {
                Icon(Icons.Default.Backspace, contentDescription = "Backspace", tint = colors.specialKeyTextColor, modifier = Modifier.size(26.dp))
            }
        }
        Row(modifier = Modifier.fillMaxWidth().height(62.dp)) {
            SpecialKeyItem(modifier = Modifier.weight(1.5f), colors = colors, onClick = { onLayoutChange(KeyboardLayoutType.QWERTY) }) {
                Text("ABC", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = colors.specialKeyTextColor)
            }
            KeyItem(text = "<", modifier = Modifier.weight(1f), colors = colors, fontSize = 26.sp, onClick = { onKeyPress("<") })
            Box(
                modifier = Modifier
                    .weight(4.5f)
                    .fillMaxHeight()
                    .padding(horizontal = 2.5.dp, vertical = 3.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(colors.keyBackground)
                    .clickable { onSpace() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Developed by Amit Bharat",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.keyTextColor.copy(alpha = 0.55f)
                )
            }
            KeyItem(text = ">", modifier = Modifier.weight(1f), colors = colors, fontSize = 26.sp, onClick = { onKeyPress(">") })
            SpecialKeyItem(modifier = Modifier.weight(1.5f), colors = colors, onClick = onEnter, isHighlighted = true) {
                Icon(Icons.Default.KeyboardReturn, contentDescription = "Enter", tint = Color.White, modifier = Modifier.size(26.dp))
            }
        }
    }
}

@Composable
fun NumericLayout(
    colors: KeyboardColors,
    onKeyPress: (String) -> Unit,
    onBackspace: () -> Unit,
    onSpace: () -> Unit,
    onEnter: () -> Unit,
    onLayoutChange: (KeyboardLayoutType) -> Unit,
    imeAction: Int
) {
    val row1 = listOf("1", "2", "3")
    val row2 = listOf("4", "5", "6")
    val row3 = listOf("7", "8", "9")
    val row4 = listOf("-", "0", ".")

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp, vertical = 2.dp)) {
        Row(modifier = Modifier.fillMaxWidth().height(60.dp)) {
            row1.forEach { num ->
                KeyItem(text = num, modifier = Modifier.weight(1f), colors = colors, fontSize = 30.sp, onClick = { onKeyPress(num) })
            }
            RepeatingSpecialKeyItem(modifier = Modifier.weight(1f), colors = colors, onTrigger = onBackspace) {
                Icon(Icons.Default.Backspace, contentDescription = "Backspace", tint = colors.specialKeyTextColor, modifier = Modifier.size(26.dp))
            }
        }
        Row(modifier = Modifier.fillMaxWidth().height(60.dp)) {
            row2.forEach { num ->
                KeyItem(text = num, modifier = Modifier.weight(1f), colors = colors, fontSize = 30.sp, onClick = { onKeyPress(num) })
            }
            SpecialKeyItem(modifier = Modifier.weight(1f), colors = colors, onClick = { onKeyPress(",") }) {
                Text(",", fontWeight = FontWeight.Bold, fontSize = 28.sp, color = colors.keyTextColor)
            }
        }
        Row(modifier = Modifier.fillMaxWidth().height(60.dp)) {
            row3.forEach { num ->
                KeyItem(text = num, modifier = Modifier.weight(1f), colors = colors, fontSize = 30.sp, onClick = { onKeyPress(num) })
            }
            SpecialKeyItem(modifier = Modifier.weight(1f), colors = colors, onClick = { onKeyPress(" ") }) {
                Text("Space", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = colors.specialKeyTextColor)
            }
        }
        Row(modifier = Modifier.fillMaxWidth().height(62.dp)) {
            SpecialKeyItem(modifier = Modifier.weight(1f), colors = colors, onClick = { onLayoutChange(KeyboardLayoutType.QWERTY) }) {
                Text("ABC", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = colors.specialKeyTextColor)
            }
            row4.forEach { char ->
                KeyItem(text = char, modifier = Modifier.weight(1f), colors = colors, fontSize = 30.sp, onClick = { onKeyPress(char) })
            }
            SpecialKeyItem(modifier = Modifier.weight(1f), colors = colors, onClick = onEnter, isHighlighted = true) {
                val icon = when (imeAction) {
                    EditorInfo.IME_ACTION_SEARCH -> Icons.Default.Search
                    EditorInfo.IME_ACTION_SEND -> Icons.Default.Send
                    EditorInfo.IME_ACTION_NEXT -> Icons.Default.ArrowForward
                    EditorInfo.IME_ACTION_GO -> Icons.Default.ArrowForward
                    else -> Icons.Default.KeyboardReturn
                }
                Icon(icon, contentDescription = "Enter", tint = Color.White, modifier = Modifier.size(26.dp))
            }
        }
    }
}

@Composable
fun EmojiLayout(
    colors: KeyboardColors,
    onEmojiClick: (String) -> Unit,
    onBackspace: () -> Unit,
    onBackToQwerty: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("Smileys") }
    val currentEmojis = EmojiData.allCategories[selectedCategory] ?: EmojiData.smileys

    Column(modifier = Modifier.fillMaxWidth().height(280.dp)) {
        // Category Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .background(colors.suggestionBarBackground),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackToQwerty, modifier = Modifier.size(38.dp)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = colors.specialKeyTextColor)
            }

            EmojiData.allCategories.keys.forEach { cat ->
                Text(
                    text = cat,
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selectedCategory == cat) colors.accentColor else Color.Transparent)
                        .clickable { selectedCategory = cat }
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    color = if (selectedCategory == cat) Color.White else colors.specialKeyTextColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            RepeatingSpecialKeyItem(onClick = onBackspace, colors = colors, modifier = Modifier.size(38.dp)) {
                Icon(Icons.Default.Backspace, contentDescription = "Backspace", tint = colors.specialKeyTextColor)
            }
        }

        // Emoji Grid
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)
        ) {
            val chunked = currentEmojis.chunked(8)
            chunked.take(4).forEach { row ->
                Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    row.forEach { emoji ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable { onEmojiClick(emoji) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(emoji, fontSize = 30.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KeyItem(
    text: String,
    modifier: Modifier = Modifier,
    colors: KeyboardColors,
    fontSize: androidx.compose.ui.unit.TextUnit = 29.sp,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 2.5.dp, vertical = 3.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isPressed) colors.accentColor.copy(alpha = 0.35f) else colors.keyBackground)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        onClick()
                        tryAwaitRelease()
                        isPressed = false
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // Active keypress visual character preview popup bubble above key
        if (isPressed) {
            Box(
                modifier = Modifier
                    .offset(y = (-52).dp)
                    .size(width = 56.dp, height = 60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colors.popupBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    color = colors.keyTextColor,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Text(
            text = text,
            color = colors.keyTextColor,
            fontSize = fontSize,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SpecialKeyItem(
    modifier: Modifier = Modifier,
    colors: KeyboardColors,
    onClick: () -> Unit,
    isHighlighted: Boolean = false,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 2.5.dp, vertical = 3.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isHighlighted) colors.accentColor else colors.specialKeyBackground)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun RepeatingSpecialKeyItem(
    modifier: Modifier = Modifier,
    colors: KeyboardColors,
    onTrigger: () -> Unit = {},
    onClick: () -> Unit = onTrigger,
    isHighlighted: Boolean = false,
    content: @Composable () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            onClick()
            delay(350)
            while (isPressed) {
                onClick()
                delay(50)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 2.5.dp, vertical = 3.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isPressed) colors.accentColor.copy(alpha = 0.25f) else if (isHighlighted) colors.accentColor else colors.specialKeyBackground)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
