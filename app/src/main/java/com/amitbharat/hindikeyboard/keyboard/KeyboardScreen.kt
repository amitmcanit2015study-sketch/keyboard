package com.amitbharat.hindikeyboard.keyboard

import android.view.inputmethod.EditorInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.isSystemInDarkTheme
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(bottom = 6.dp)
    ) {
        // 1. Suggestion & Shortcut Bar
        SuggestionBar(
            suggestions = state.suggestions,
            mode = state.typingMode,
            colors = colors,
            onSuggestionClick = onSuggestionClick,
            onLayoutChange = onLayoutChange,
            onVoiceClick = onVoiceClick,
            onSettingsClick = onSettingsClick
        )

        // 2. Keyboard Views (QWERTY, Symbols, Emoji, Clipboard)
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
            KeyboardLayoutType.CLIPBOARD -> {
                ClipboardLayout(
                    colors = colors,
                    onPaste = onKeyPress,
                    onBackToQwerty = { onLayoutChange(KeyboardLayoutType.QWERTY) }
                )
            }
        }
    }
}

@Composable
fun SuggestionBar(
    suggestions: List<String>,
    mode: TypingMode,
    colors: KeyboardColors,
    onSuggestionClick: (String) -> Unit,
    onLayoutChange: (KeyboardLayoutType) -> Unit,
    onVoiceClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(colors.suggestionBarBackground)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Quick Action Icons
        IconButton(
            onClick = { onLayoutChange(KeyboardLayoutType.CLIPBOARD) },
            modifier = Modifier.size(36.dp)
        ) {
            Icon(Icons.Default.ContentPaste, contentDescription = "Clipboard", tint = colors.specialKeyTextColor, modifier = Modifier.size(19.dp))
        }

        IconButton(
            onClick = { onLayoutChange(KeyboardLayoutType.EMOJI) },
            modifier = Modifier.size(36.dp)
        ) {
            Icon(Icons.Default.EmojiEmotions, contentDescription = "Emoji", tint = colors.specialKeyTextColor, modifier = Modifier.size(19.dp))
        }

        // Suggestions List
        LazyRow(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(suggestions) { item ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colors.keyBackground)
                        .clickable { onSuggestionClick(item) }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item,
                        color = colors.suggestionTextColor,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Voice Typing Button
        IconButton(
            onClick = onVoiceClick,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(Icons.Default.Mic, contentDescription = "Voice", tint = colors.accentColor, modifier = Modifier.size(20.dp))
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
            .padding(horizontal = 3.dp, vertical = 2.dp)
    ) {
        // Optional Dedicated Number Row
        if (state.showNumberRow) {
            Row(modifier = Modifier.fillMaxWidth().height(44.dp)) {
                numberRow.forEach { num ->
                    KeyItem(text = num, modifier = Modifier.weight(1f), colors = colors, onClick = { onKeyPress(num) })
                }
            }
        }

        // Row 1 (Q W E R T Y U I O P)
        Row(modifier = Modifier.fillMaxWidth().height(48.dp)) {
            row1.forEach { char ->
                val displayText = if (state.shiftState != ShiftState.OFF) char.uppercase() else char
                KeyItem(text = displayText, modifier = Modifier.weight(1f), colors = colors, onClick = { onKeyPress(displayText) })
            }
        }

        // Row 2 (A S D F G H J K L)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 14.dp)
        ) {
            row2.forEach { char ->
                val displayText = if (state.shiftState != ShiftState.OFF) char.uppercase() else char
                KeyItem(text = displayText, modifier = Modifier.weight(1f), colors = colors, onClick = { onKeyPress(displayText) })
            }
        }

        // Row 3 (Shift + Z X C V B N M + Backspace)
        Row(modifier = Modifier.fillMaxWidth().height(48.dp)) {
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
                    modifier = Modifier.size(20.dp)
                )
            }

            row3.forEach { char ->
                val displayText = if (state.shiftState != ShiftState.OFF) char.uppercase() else char
                KeyItem(text = displayText, modifier = Modifier.weight(1f), colors = colors, onClick = { onKeyPress(displayText) })
            }

            // Backspace Key
            SpecialKeyItem(
                modifier = Modifier.weight(1.5f),
                colors = colors,
                onClick = onBackspace
            ) {
                Icon(Icons.Default.Backspace, contentDescription = "Backspace", tint = colors.specialKeyTextColor, modifier = Modifier.size(20.dp))
            }
        }

        // Row 4 (123, Language Switch Button, Spacebar, Period/Comma, Enter)
        Row(modifier = Modifier.fillMaxWidth().height(50.dp)) {
            // ?123 Symbols Button
            SpecialKeyItem(
                modifier = Modifier.weight(1.3f),
                colors = colors,
                onClick = { onLayoutChange(KeyboardLayoutType.SYMBOLS) }
            ) {
                Text("?123", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = colors.specialKeyTextColor)
            }

            // One-Tap EN / Hindi Toggle Button (Icon Key)
            val isHindi = state.typingMode == TypingMode.HINDI_TRANSLITERATION
            SpecialKeyItem(
                modifier = Modifier.weight(1.3f),
                colors = colors,
                onClick = onLanguageToggle,
                isHighlighted = isHindi
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Language,
                        contentDescription = if (isHindi) "Switch to English" else "Switch to Hindi",
                        tint = if (isHindi) Color.White else colors.specialKeyTextColor,
                        modifier = Modifier.size(20.dp)
                    )
                    // Compact indicator badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 6.dp, y = 6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (isHindi) Color.White.copy(alpha = 0.25f) else colors.keyTextColor.copy(alpha = 0.12f))
                            .padding(horizontal = 2.dp, vertical = 0.5.dp)
                    ) {
                        Text(
                            text = if (isHindi) "हि" else "EN",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isHindi) Color.White else colors.specialKeyTextColor
                        )
                    }
                }
            }

            // Spacebar (with swipe cursor navigation)
            var dragAccumulator by remember { mutableFloatStateOf(0f) }
            Box(
                modifier = Modifier
                    .weight(4.4f)
                    .fillMaxHeight()
                    .padding(2.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colors.keyBackground)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                dragAccumulator += dragAmount.x
                                if (dragAccumulator > 30f) {
                                    onCursorMove(1)
                                    dragAccumulator = 0f
                                } else if (dragAccumulator < -30f) {
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
                    text = if (isHindi) "हिन्दी (Transliteration)" else "English",
                    fontSize = 12.sp,
                    color = colors.keyTextColor.copy(alpha = 0.5f)
                )
            }

            // Period Key
            KeyItem(text = ".", modifier = Modifier.weight(1f), colors = colors, onClick = { onKeyPress(".") })

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
                    else -> Icons.Default.KeyboardReturn
                }
                Icon(icon, contentDescription = "Enter", tint = Color.White, modifier = Modifier.size(20.dp))
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

    Column(modifier = Modifier.fillMaxWidth().padding(3.dp)) {
        Row(modifier = Modifier.fillMaxWidth().height(48.dp)) {
            r1.forEach { KeyItem(text = it, modifier = Modifier.weight(1f), colors = colors, onClick = { onKeyPress(it) }) }
        }
        Row(modifier = Modifier.fillMaxWidth().height(48.dp)) {
            r2.forEach { KeyItem(text = it, modifier = Modifier.weight(1f), colors = colors, onClick = { onKeyPress(it) }) }
        }
        Row(modifier = Modifier.fillMaxWidth().height(48.dp)) {
            SpecialKeyItem(modifier = Modifier.weight(1.5f), colors = colors, onClick = { onLayoutChange(KeyboardLayoutType.MORE_SYMBOLS) }) {
                Text("=/<", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = colors.specialKeyTextColor)
            }
            r3.forEach { KeyItem(text = it, modifier = Modifier.weight(1f), colors = colors, onClick = { onKeyPress(it) }) }
            SpecialKeyItem(modifier = Modifier.weight(1.5f), colors = colors, onClick = onBackspace) {
                Icon(Icons.Default.Backspace, contentDescription = "Backspace", tint = colors.specialKeyTextColor, modifier = Modifier.size(20.dp))
            }
        }
        Row(modifier = Modifier.fillMaxWidth().height(50.dp)) {
            SpecialKeyItem(modifier = Modifier.weight(1.5f), colors = colors, onClick = { onLayoutChange(KeyboardLayoutType.QWERTY) }) {
                Text("ABC", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = colors.specialKeyTextColor)
            }
            KeyItem(text = ",", modifier = Modifier.weight(1f), colors = colors, onClick = { onKeyPress(",") })
            Box(
                modifier = Modifier.weight(4.5f).fillMaxHeight().padding(2.dp).clip(RoundedCornerShape(8.dp)).background(colors.keyBackground).clickable { onSpace() },
                contentAlignment = Alignment.Center
            ) {
                Text("Space", fontSize = 12.sp, color = colors.keyTextColor.copy(alpha = 0.5f))
            }
            KeyItem(text = ".", modifier = Modifier.weight(1f), colors = colors, onClick = { onKeyPress(".") })
            SpecialKeyItem(modifier = Modifier.weight(1.5f), colors = colors, onClick = onEnter, isHighlighted = true) {
                Icon(Icons.Default.KeyboardReturn, contentDescription = "Enter", tint = Color.White, modifier = Modifier.size(20.dp))
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
    val r2 = listOf("£", "¥", "€", "¢", "^", "°", "=", "{", "}", "\\")
    val r3 = listOf("%", "©", "®", "™", "✓", "[", "]")

    Column(modifier = Modifier.fillMaxWidth().padding(3.dp)) {
        Row(modifier = Modifier.fillMaxWidth().height(48.dp)) {
            r1.forEach { KeyItem(text = it, modifier = Modifier.weight(1f), colors = colors, onClick = { onKeyPress(it) }) }
        }
        Row(modifier = Modifier.fillMaxWidth().height(48.dp)) {
            r2.forEach { KeyItem(text = it, modifier = Modifier.weight(1f), colors = colors, onClick = { onKeyPress(it) }) }
        }
        Row(modifier = Modifier.fillMaxWidth().height(48.dp)) {
            SpecialKeyItem(modifier = Modifier.weight(1.5f), colors = colors, onClick = { onLayoutChange(KeyboardLayoutType.SYMBOLS) }) {
                Text("?123", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = colors.specialKeyTextColor)
            }
            r3.forEach { KeyItem(text = it, modifier = Modifier.weight(1f), colors = colors, onClick = { onKeyPress(it) }) }
            SpecialKeyItem(modifier = Modifier.weight(1.5f), colors = colors, onClick = onBackspace) {
                Icon(Icons.Default.Backspace, contentDescription = "Backspace", tint = colors.specialKeyTextColor, modifier = Modifier.size(20.dp))
            }
        }
        Row(modifier = Modifier.fillMaxWidth().height(50.dp)) {
            SpecialKeyItem(modifier = Modifier.weight(1.5f), colors = colors, onClick = { onLayoutChange(KeyboardLayoutType.QWERTY) }) {
                Text("ABC", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = colors.specialKeyTextColor)
            }
            KeyItem(text = "<", modifier = Modifier.weight(1f), colors = colors, onClick = { onKeyPress("<") })
            Box(
                modifier = Modifier.weight(4.5f).fillMaxHeight().padding(2.dp).clip(RoundedCornerShape(8.dp)).background(colors.keyBackground).clickable { onSpace() },
                contentAlignment = Alignment.Center
            ) {
                Text("Space", fontSize = 12.sp, color = colors.keyTextColor.copy(alpha = 0.5f))
            }
            KeyItem(text = ">", modifier = Modifier.weight(1f), colors = colors, onClick = { onKeyPress(">") })
            SpecialKeyItem(modifier = Modifier.weight(1.5f), colors = colors, onClick = onEnter, isHighlighted = true) {
                Icon(Icons.Default.KeyboardReturn, contentDescription = "Enter", tint = Color.White, modifier = Modifier.size(20.dp))
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

    Column(modifier = Modifier.fillMaxWidth().height(240.dp)) {
        // Category Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(colors.suggestionBarBackground),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackToQwerty, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = colors.specialKeyTextColor)
            }

            EmojiData.allCategories.keys.forEach { cat ->
                Text(
                    text = cat,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selectedCategory == cat) colors.accentColor else Color.Transparent)
                        .clickable { selectedCategory = cat }
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    color = if (selectedCategory == cat) Color.White else colors.specialKeyTextColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = onBackspace, modifier = Modifier.size(36.dp)) {
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
                            Text(emoji, fontSize = 24.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClipboardLayout(
    colors: KeyboardColors,
    onPaste: (String) -> Unit,
    onBackToQwerty: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().height(240.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(colors.suggestionBarBackground)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackToQwerty, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = colors.specialKeyTextColor)
            }
            Text("Clipboard History", color = colors.suggestionTextColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        val sampleClips = listOf(
            "Namaste! Kaise ho aap?",
            "Mera naam Amit Bharat hai.",
            "Rooys Soft Tech - rooyssofttech2020@gmail.com",
            "Shubh Prabhat! Have a wonderful day.",
            "Dhanyawad!"
        )

        LazyRow(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            items(sampleClips) { clip ->
                Box(
                    modifier = Modifier
                        .width(180.dp)
                        .fillMaxHeight()
                        .padding(end = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.keyBackground)
                        .clickable { onPaste(clip) }
                        .padding(12.dp)
                ) {
                    Text(clip, color = colors.keyTextColor, fontSize = 13.sp)
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
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .padding(2.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(colors.keyBackground)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = colors.keyTextColor,
            fontSize = 18.5.sp,
            fontWeight = FontWeight.Medium,
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
            .padding(2.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isHighlighted) colors.accentColor else colors.specialKeyBackground)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
