package com.example.ui.components

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LocalAppThemePreset

/**
 * Ambient Gradient Glow Modifier:
 * Avoids harsh solid lines or boxy borders.
 * Uses a dual-layer soft ambient diffusion:
 * 1. Outer Gaussian aura (8dp to 12dp) with 20%-35% alpha transparency fading outward.
 * 2. Ultra-thin 0.75dp contour stroke with corner-weighted gradient opacity fading out along flat edges.
 */
@Composable
fun Modifier.glowingBorder(
    shape: CornerBasedShape = RoundedCornerShape(16.dp),
    glowColor: Color = Color.Unspecified,
    strokeWidth: Dp = 0.75.dp,
    glowRadius: Dp = 10.dp,
    glowAlpha: Float = 0.28f,
    enabled: Boolean = true
): Modifier {
    if (!enabled) return this

    val activeGlow = if (glowColor.isSpecified) glowColor else LocalAppThemePreset.current.glow
    val density = LocalDensity.current

    val strokePx = with(density) { strokeWidth.toPx() }
    val glowRadiusPx = with(density) { glowRadius.toPx() }

    return this
        // 1. Soft ambient outer aura (fading outward to 0% alpha)
        .drawBehind {
            val width = size.width
            val height = size.height
            val baseRadius = with(density) { shape.topStart.toPx(size, density) }

            // Multi-step atmospheric diffusion aura
            val steps = 5
            for (i in 1..steps) {
                val spread = (glowRadiusPx * i) / steps
                val layerAlpha = (glowAlpha * (1f - (i.toFloat() / (steps + 1)))) * 0.4f
                val auraBrush = Brush.linearGradient(
                    colors = listOf(
                        activeGlow.copy(alpha = layerAlpha * 1.2f),
                        activeGlow.copy(alpha = layerAlpha * 0.4f),
                        Color.Transparent
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(width + spread, height + spread)
                )

                drawRoundRect(
                    brush = auraBrush,
                    topLeft = Offset(-spread, -spread),
                    size = Size(width + (spread * 2), height + (spread * 2)),
                    cornerRadius = CornerRadius(baseRadius + spread, baseRadius + spread)
                )
            }
        }
        // 2. Ultra-thin 0.75dp contour stroke with heavier opacity at corners and soft fade along edges
        .drawWithCache {
            val width = size.width
            val height = size.height
            val cornerPx = shape.topStart.toPx(size, density)

            val contourBrush = Brush.linearGradient(
                colors = listOf(
                    activeGlow.copy(alpha = glowAlpha * 1.8f),
                    activeGlow.copy(alpha = glowAlpha * 0.5f),
                    activeGlow.copy(alpha = glowAlpha * 1.6f),
                    Color.Transparent,
                    activeGlow.copy(alpha = glowAlpha * 1.2f)
                ),
                start = Offset(0f, 0f),
                end = Offset(width, height)
            )

            val path = Path().apply {
                addRoundRect(
                    RoundRect(
                        left = strokePx / 2f,
                        top = strokePx / 2f,
                        right = width - (strokePx / 2f),
                        bottom = height - (strokePx / 2f),
                        radiusX = cornerPx,
                        radiusY = cornerPx
                    )
                )
            }

            onDrawWithContent {
                drawContent()
                drawPath(
                    path = path,
                    brush = contourBrush,
                    style = Stroke(width = strokePx)
                )
            }
        }
}
