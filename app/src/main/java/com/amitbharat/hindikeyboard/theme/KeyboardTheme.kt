package com.amitbharat.hindikeyboard.theme

import androidx.compose.ui.graphics.Color

enum class ThemeType {
    SYSTEM_DEFAULT,
    MATERIAL_LIGHT,
    MATERIAL_DARK,
    AMOLED_BLACK,
    ROYAL_INDIGO,
    EMERALD_GREEN
}

data class KeyboardColors(
    val background: Color,
    val keyBackground: Color,
    val keyTextColor: Color,
    val specialKeyBackground: Color,
    val specialKeyTextColor: Color,
    val accentColor: Color,
    val suggestionBarBackground: Color,
    val suggestionTextColor: Color,
    val popupBackground: Color
)

object KeyboardThemeManager {
    fun getColors(theme: ThemeType, isSystemDark: Boolean = false): KeyboardColors {
        val effectiveTheme = if (theme == ThemeType.SYSTEM_DEFAULT) {
            if (isSystemDark) ThemeType.MATERIAL_DARK else ThemeType.MATERIAL_LIGHT
        } else {
            theme
        }

        return when (effectiveTheme) {
            ThemeType.SYSTEM_DEFAULT,
            ThemeType.MATERIAL_LIGHT -> KeyboardColors(
                background = Color(0xFFECEFF1),
                keyBackground = Color(0xFFFFFFFF),
                keyTextColor = Color(0xFF1A1B1F),
                specialKeyBackground = Color(0xFFD7DCE0),
                specialKeyTextColor = Color(0xFF3B50DF),
                accentColor = Color(0xFF3B50DF),
                suggestionBarBackground = Color(0xFFE2E7EB),
                suggestionTextColor = Color(0xFF1A1B1F),
                popupBackground = Color(0xFFFFFFFF)
            )
            ThemeType.MATERIAL_DARK -> KeyboardColors(
                background = Color(0xFF1F2128),
                keyBackground = Color(0xFF2E323E),
                keyTextColor = Color(0xFFF1F3F4),
                specialKeyBackground = Color(0xFF3B404E),
                specialKeyTextColor = Color(0xFFBCC2FF),
                accentColor = Color(0xFF3B50DF),
                suggestionBarBackground = Color(0xFF262933),
                suggestionTextColor = Color(0xFFE4E1E6),
                popupBackground = Color(0xFF2E323E)
            )
            ThemeType.AMOLED_BLACK -> KeyboardColors(
                background = Color(0xFF000000),
                keyBackground = Color(0xFF121212),
                keyTextColor = Color(0xFFFFFFFF),
                specialKeyBackground = Color(0xFF222222),
                specialKeyTextColor = Color(0xFFBCC2FF),
                accentColor = Color(0xFF3B50DF),
                suggestionBarBackground = Color(0xFF0A0A0A),
                suggestionTextColor = Color(0xFFFFFFFF),
                popupBackground = Color(0xFF1A1A1A)
            )
            ThemeType.ROYAL_INDIGO -> KeyboardColors(
                background = Color(0xFF1A1C38),
                keyBackground = Color(0xFF292C54),
                keyTextColor = Color(0xFFFFFFFF),
                specialKeyBackground = Color(0xFF383C72),
                specialKeyTextColor = Color(0xFFBCC2FF),
                accentColor = Color(0xFF5B69FF),
                suggestionBarBackground = Color(0xFF202345),
                suggestionTextColor = Color(0xFFFFFFFF),
                popupBackground = Color(0xFF292C54)
            )
            ThemeType.EMERALD_GREEN -> KeyboardColors(
                background = Color(0xFF0F2B20),
                keyBackground = Color(0xFF194433),
                keyTextColor = Color(0xFFFFFFFF),
                specialKeyBackground = Color(0xFF245E47),
                specialKeyTextColor = Color(0xFFA7F3D0),
                accentColor = Color(0xFF10B981),
                suggestionBarBackground = Color(0xFF143729),
                suggestionTextColor = Color(0xFFFFFFFF),
                popupBackground = Color(0xFF194433)
            )
        }
    }
}
