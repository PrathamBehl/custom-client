package com.example.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.UserListEntry
import com.example.ui.components.FormatPill
import com.example.ui.components.ThemeSelectorModal
import com.example.ui.components.glowingBorder
import com.example.ui.theme.AmberMAL
import com.example.ui.theme.CyanAniList
import com.example.ui.theme.LocalAppThemePreset
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SlateBackground
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateSurfaceHigh
import com.example.ui.theme.SlateSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.viewmodel.MainViewModel

val listStatuses = listOf("ALL", "WATCHING", "READING", "COMPLETED", "PLANNING")

@Composable
fun LibraryScreen(
    viewModel: MainViewModel,
    isTablet: Boolean,
    onMediaClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val userList by viewModel.userList.collectAsStateWithLifecycle()
    val stats by viewModel.userStats.collectAsStateWithLifecycle()
    val currentTheme by viewModel.themePreset.collectAsStateWithLifecycle()
    val preset = LocalAppThemePreset.current

    var selectedStatus by remember { mutableStateOf("ALL") }
    var showThemeModal by remember { mutableStateOf(false) }

    val filteredList = userList.filter {
        if (selectedStatus == "ALL") true else it.status == selectedStatus
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SlateBackground)
            .statusBarsPadding()
    ) {
        // User Profile & Stats Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(preset.glow)
                        .glowingBorder(
                            shape = CircleShape,
                            glowColor = preset.glow,
                            glowRadius = 10.dp,
                            glowAlpha = 0.35f
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "JD",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "AniSync User",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.CloudDone,
                            contentDescription = "AniList/MAL Synced",
                            tint = CyanAniList,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = "AniList: Connected • MyAnimeList: Synced",
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }

                // Theme Engine Quick Selector Trigger
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(SlateSurfaceHigh)
                        .border(1.dp, preset.glow.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .glowingBorder(
                            shape = RoundedCornerShape(12.dp),
                            glowColor = preset.glow,
                            glowRadius = 8.dp,
                            glowAlpha = 0.30f
                        )
                        .clickable { showThemeModal = true }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Theme Engine",
                            tint = preset.glow,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = currentTheme.displayName,
                            color = TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4 Stats Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    label = "Episodes",
                    value = "${stats.episodesWatched}",
                    color = preset.glow,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Chapters",
                    value = "${stats.chaptersRead}",
                    color = CyanAniList,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Mean Score",
                    value = "${stats.meanScore}",
                    color = AmberMAL,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Days",
                    value = "${stats.daysWatched}",
                    color = Color(0xFF10B981),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Custom List Filter Tabs
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(listStatuses) { status ->
                    val isSelected = selectedStatus == status
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) NeonPurple else SlateSurface)
                            .border(
                                1.dp,
                                if (isSelected) NeonPurple else SlateBorder,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedStatus = status }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = status,
                            color = if (isSelected) Color.White else TextMuted,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        // List Entries
        LazyColumn(
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 4.dp,
                bottom = if (isTablet) 32.dp else 96.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .testTag("library_entries_list")
        ) {
            items(filteredList, key = { it.mediaId }) { entry ->
                LibraryEntryCard(
                    entry = entry,
                    onMediaClick = { onMediaClick(entry.mediaId) },
                    onIncrementProgress = {
                        viewModel.updateWatchProgress(
                            mediaId = entry.mediaId,
                            newEpisode = (entry.progress + 1).coerceAtMost(entry.totalUnits),
                            totalUnits = entry.totalUnits,
                            title = entry.title,
                            coverUrl = entry.coverUrl,
                            format = entry.format
                        )
                    }
                )
            }
        }

        if (showThemeModal) {
            ThemeSelectorModal(
                selectedPreset = currentTheme,
                onPresetSelect = { newTheme ->
                    viewModel.setThemePreset(newTheme)
                },
                onDismiss = { showThemeModal = false }
            )
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SlateSurface)
            .border(1.dp, SlateBorder, RoundedCornerShape(12.dp))
            .glowingBorder(
                shape = RoundedCornerShape(12.dp),
                glowColor = color,
                glowRadius = 6.dp,
                glowAlpha = 0.18f
            )
            .padding(vertical = 10.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                color = color,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                color = TextMuted,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun LibraryEntryCard(
    entry: UserListEntry,
    onMediaClick: () -> Unit,
    onIncrementProgress: () -> Unit
) {
    val preset = LocalAppThemePreset.current
    val progressFraction = (entry.progress.toFloat() / entry.totalUnits.toFloat()).coerceIn(0f, 1f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SlateBorder, RoundedCornerShape(14.dp))
            .glowingBorder(
                shape = RoundedCornerShape(14.dp),
                glowColor = preset.glow,
                glowRadius = 6.dp,
                glowAlpha = 0.12f
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onMediaClick)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(entry.coverUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = entry.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 54.dp, height = 76.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = entry.title,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    FormatPill(format = entry.format)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (entry.format == "MANGA") "Ch. ${entry.progress} of ${entry.totalUnits}" else "Ep. ${entry.progress} of ${entry.totalUnits}",
                        color = preset.glow,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    if (entry.userScore > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = AmberMAL,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${entry.userScore}",
                                color = AmberMAL,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    if (entry.rewatchCount > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Autorenew,
                                contentDescription = null,
                                tint = CyanAniList,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "x${entry.rewatchCount}",
                                color = CyanAniList,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { progressFraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = preset.glow,
                    trackColor = SlateSurfaceHigh
                )

                if (entry.notes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = entry.notes,
                        color = TextMuted,
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Quick +1 Episode / Chapter Button
            IconButton(
                onClick = onIncrementProgress,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(SlateSurfaceHigh)
                    .size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "+1 Progress",
                    tint = TextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
