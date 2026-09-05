package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberDelayed
import com.example.ui.theme.AmberMAL
import com.example.ui.theme.CyanAniList
import com.example.ui.theme.GreenAiring
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SlateBackground
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.TextPrimary

@Composable
fun AniListScorePill(score: Float?, modifier: Modifier = Modifier) {
    if (score == null) return
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .background(CyanAniList)
            .padding(horizontal = 7.dp, vertical = 2.dp)
    ) {
        Text(
            text = "ANILIST ${String.format("%.1f", score)}",
            color = Color.White,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.4.sp
        )
    }
}

@Composable
fun MalScorePill(score: Float?, modifier: Modifier = Modifier) {
    if (score == null) return
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .background(AmberMAL)
            .padding(horizontal = 7.dp, vertical = 2.dp)
    ) {
        Text(
            text = "MAL ${String.format("%.2f", score)}",
            color = SlateBackground,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.4.sp
        )
    }
}

@Composable
fun StatusAiringIndicator(
    status: String,
    modifier: Modifier = Modifier
) {
    val isDelayed = status.contains("DELAY", ignoreCase = true)
    val dotColor = if (isDelayed) AmberDelayed else GreenAiring
    val labelText = if (isDelayed) "DELAYED" else "AIRING"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = labelText,
            color = dotColor,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun HighDensityProgressTag(
    percentage: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(SlateBackground.copy(alpha = 0.85f))
            .border(1.dp, SlateBorder, RoundedCornerShape(6.dp))
            .padding(horizontal = 5.dp, vertical = 2.dp)
    ) {
        Text(
            text = "$percentage%",
            color = CyanAniList,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
fun CountdownBadge(seconds: Long, episode: Int, modifier: Modifier = Modifier) {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val timeFormatted = if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(SlateBackground.copy(alpha = 0.85f))
            .border(1.dp, SlateBorder, RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null,
                tint = NeonPurple,
                modifier = Modifier.size(10.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = "EP $episode IN $timeFormatted",
                color = TextPrimary,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.4.sp
            )
        }
    }
}

@Composable
fun FormatPill(format: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color.Black.copy(alpha = 0.65f))
            .padding(horizontal = 5.dp, vertical = 2.dp)
    ) {
        Text(
            text = format.uppercase(),
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

