package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

fun getThemeColorScheme(preset: ThemePreset) = darkColorScheme(
    primary = preset.glow,
    onPrimary = Color.White,
    primaryContainer = preset.surfaceHigh,
    onPrimaryContainer = preset.glow,
    secondary = preset.accentSecondary,
    onSecondary = preset.surface,
    secondaryContainer = preset.surfaceHigh,
    onSecondaryContainer = preset.accentSecondary,
    tertiary = AmberMAL,
    onTertiary = preset.surface,
    tertiaryContainer = Color(0xFF533306),
    onTertiaryContainer = AmberMAL,
    background = preset.surface,
    onBackground = TextPrimary,
    surface = preset.cards,
    onSurface = TextPrimary,
    surfaceVariant = preset.surfaceHigh,
    onSurfaceVariant = TextSecondary,
    outline = preset.border,
    outlineVariant = preset.surfaceHigh
)

@Composable
fun MyApplicationTheme(
    preset: ThemePreset = ThemePreset.LIQUID_CHARCOAL,
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = getThemeColorScheme(preset)
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = false
                insetsController.isAppearanceLightNavigationBars = false
            }
        }
    }

    CompositionLocalProvider(LocalAppThemePreset provides preset) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}


