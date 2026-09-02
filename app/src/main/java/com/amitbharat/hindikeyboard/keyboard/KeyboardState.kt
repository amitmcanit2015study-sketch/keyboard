package com.amitbharat.hindikeyboard.keyboard

import com.amitbharat.hindikeyboard.suggestions.TypingMode
import com.amitbharat.hindikeyboard.theme.ThemeType

enum class KeyboardLayoutType {
    QWERTY,
    SYMBOLS,
    MORE_SYMBOLS,
    EMOJI,
    CLIPBOARD
}

enum class ShiftState {
    OFF,
    ON,
    CAPS_LOCK
}

data class KeyboardUiState(
    val typingMode: TypingMode = TypingMode.ENGLISH,
    val layoutType: KeyboardLayoutType = KeyboardLayoutType.QWERTY,
    val shiftState: ShiftState = ShiftState.OFF,
    val currentWord: String = "",
    val suggestions: List<String> = emptyList(),
    val showNumberRow: Boolean = true,
    val enableVibration: Boolean = true,
    val enableSound: Boolean = false,
    val activeTheme: ThemeType = ThemeType.SYSTEM_DEFAULT,
    val isListeningVoice: Boolean = false
)
