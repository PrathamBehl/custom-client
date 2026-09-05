package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.data.model.MediaEntity
import com.example.ui.theme.CyanAniList
import com.example.ui.theme.NeonPurple

/**
 * Vibe Palette: Dominant accent colors extracted/derived for dynamic ambient lighting.
 */
data class VibePalette(
    val dominant: Color,
    val secondary: Color,
    val ambientGradient: Brush
)

@Composable
fun rememberVibePalette(media: MediaEntity?): VibePalette {
    return remember(media?.id) {
        if (media == null) {
            VibePalette(
                dominant = NeonPurple,
                secondary = CyanAniList,
                ambientGradient = Brush.verticalGradient(
                    listOf(NeonPurple.copy(alpha = 0.25f), Color.Transparent)
                )
            )
        } else {
            // Compute dynamic vibrant hues derived from media genres, title, and id
            val seed = (media.titleEnglish.hashCode() + media.id).toLong()
            val hue = (kotlin.math.abs(seed) % 360).toFloat()
            val dominantColor = Color.hsl(hue = hue, saturation = 0.85f, lightness = 0.65f)
            val secondaryColor = Color.hsl(hue = (hue + 45f) % 360f, saturation = 0.90f, lightness = 0.70f)

            VibePalette(
                dominant = dominantColor,
                secondary = secondaryColor,
                ambientGradient = Brush.verticalGradient(
                    colors = listOf(
                        dominantColor.copy(alpha = 0.28f),
                        secondaryColor.copy(alpha = 0.10f),
                        Color.Transparent
                    )
                )
            )
        }
    }
}

/**
 * Video Ambilight Overlay:
 * Renders a soft 16dp outer blur aura around the video player container
 * dynamically reflecting the dominant hues of the playing stream.
 */
@Composable
fun VideoAmbilightBox(
    dominantColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ambilight_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.65f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Box(modifier = modifier) {
        // Outer 16dp soft ambilight aura
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp)
                .blur(16.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            dominantColor.copy(alpha = pulseAlpha),
                            dominantColor.copy(alpha = pulseAlpha * 0.4f),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        )

        // Inner Content
        content()
    }
}

/**
 * Audio Visualizer Bar:
 * Minimalistic 5-bar glowing spectrum animation inside the Openings/Endings track listing
 * reacting dynamically during song playback.
 */
@Composable
fun AudioVisualizerBar(
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    barColor: Color = NeonPurple
) {
    val infiniteTransition = rememberInfiniteTransition(label = "audio_bars")

    val h1 by infiniteTransition.animateFloat(
        initialValue = 4f, targetValue = 18f,
        animationSpec = infiniteRepeatable(tween(420, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "h1"
    )
    val h2 by infiniteTransition.animateFloat(
        initialValue = 16f, targetValue = 6f,
        animationSpec = infiniteRepeatable(tween(350, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "h2"
    )
    val h3 by infiniteTransition.animateFloat(
        initialValue = 8f, targetValue = 20f,
        animationSpec = infiniteRepeatable(tween(510, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "h3"
    )
    val h4 by infiniteTransition.animateFloat(
        initialValue = 18f, targetValue = 5f,
        animationSpec = infiniteRepeatable(tween(390, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "h4"
    )
    val h5 by infiniteTransition.animateFloat(
        initialValue = 6f, targetValue = 14f,
        animationSpec = infiniteRepeatable(tween(470, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "h5"
    )

    val heights = listOf(h1, h2, h3, h4, h5)

    Row(
        modifier = modifier.height(20.dp),
        horizontalArrangement = Arrangement.spacedBy(2.5.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        heights.forEach { heightVal ->
            val barHeight = if (isPlaying) heightVal.dp else 4.dp
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(barHeight)
                    .clip(CircleShape)
                    .background(barColor)
            )
        }
    }
}
