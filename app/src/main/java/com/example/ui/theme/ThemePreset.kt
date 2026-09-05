package com.example.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Multi-Theme Engine Presets:
 * 1. Liquid Charcoal (Default): Surface #121216, Cards #1E1E24, Glow #7C4DFF
 * 2. Midnight Cyberpunk: Surface #0A0E17, Cards #131B2E, Glow #00E5FF
 * 3. AMOLED Abyss: True Black #000000, Cards #101010, Glow #FF0055
 * 4. Royal Velvet: Surface #120A1A, Cards #1D122B, Glow #D500F9
 * 5. Neon Matrix (Neon Green): Surface #08100C, Cards #101F18, Glow #00FF66
 * 6. Inferno Flare (Orangish Red): Surface #140A07, Cards #21110B, Glow #FF3D00
 * 7. Electric Indigo (Blueish Purple): Surface #0B0A1A, Cards #14122E, Glow #6C5CE7
 */
enum class ThemePreset(
    val title: String,
    val surface: Color,
    val cards: Color,
    val glow: Color,
    val surfaceHigh: Color,
    val border: Color,
    val accentSecondary: Color = Color(0xFF00B5E2)
) {
    LIQUID_CHARCOAL(
        title = "Liquid Charcoal",
        surface = Color(0xFF121216),
        cards = Color(0xFF1E1E24),
        glow = Color(0xFF7C4DFF),
        surfaceHigh = Color(0xFF2C2C36),
        border = Color(0xFF282834),
        accentSecondary = Color(0xFF00B5E2)
    ),
    MIDNIGHT_CYBERPUNK(
        title = "Midnight Cyberpunk",
        surface = Color(0xFF0A0E17),
        cards = Color(0xFF131B2E),
        glow = Color(0xFF00E5FF),
        surfaceHigh = Color(0xFF1C2742),
        border = Color(0xFF1E2D4A),
        accentSecondary = Color(0xFFE040FB)
    ),
    AMOLED_ABYSS(
        title = "AMOLED Abyss",
        surface = Color(0xFF000000),
        cards = Color(0xFF101010),
        glow = Color(0xFFFF0055),
        surfaceHigh = Color(0xFF1A1A1A),
        border = Color(0xFF262626),
        accentSecondary = Color(0xFFFF5252)
    ),
    ROYAL_VELVET(
        title = "Royal Velvet",
        surface = Color(0xFF120A1A),
        cards = Color(0xFF1D122B),
        glow = Color(0xFFD500F9),
        surfaceHigh = Color(0xFF2B1B40),
        border = Color(0xFF331E4D),
        accentSecondary = Color(0xFF651FFF)
    ),
    NEON_MATRIX(
        title = "Neon Matrix",
        surface = Color(0xFF08100C),
        cards = Color(0xFF101F18),
        glow = Color(0xFF00FF66),
        surfaceHigh = Color(0xFF183025),
        border = Color(0xFF1F3D2F),
        accentSecondary = Color(0xFF00E676)
    ),
    INFERNO_FLARE(
        title = "Inferno Flare",
        surface = Color(0xFF140A07),
        cards = Color(0xFF21110B),
        glow = Color(0xFFFF3D00),
        surfaceHigh = Color(0xFF331B12),
        border = Color(0xFF402217),
        accentSecondary = Color(0xFFFFAB00)
    ),
    ELECTRIC_INDIGO(
        title = "Electric Indigo",
        surface = Color(0xFF0B0A1A),
        cards = Color(0xFF14122E),
        glow = Color(0xFF6C5CE7),
        surfaceHigh = Color(0xFF201D47),
        border = Color(0xFF2D295E),
        accentSecondary = Color(0xFF74B9FF)
    );

    val displayName: String get() = title
}

data class ThemeState(
    val preset: ThemePreset = ThemePreset.LIQUID_CHARCOAL,
    val isGlowEnabled: Boolean = true
)

val LocalAppThemePreset = compositionLocalOf { ThemePreset.LIQUID_CHARCOAL }
val LocalThemeState = compositionLocalOf { ThemeState() }
