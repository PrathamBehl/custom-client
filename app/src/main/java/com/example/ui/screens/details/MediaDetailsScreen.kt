package com.example.ui.screens.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.MediaEntity
import com.example.data.model.MediaRelation
import com.example.ui.components.AniListScorePill
import com.example.ui.components.AudioVisualizerBar
import com.example.ui.components.FormatPill
import com.example.ui.components.MalScorePill
import com.example.ui.components.glowingBorder
import com.example.ui.components.rememberVibePalette
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
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MediaDetailsScreen(
    mediaId: Int,
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    onPlayClick: (MediaEntity, Int) -> Unit,
    onRelationClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val allMedia by viewModel.allMedia.collectAsStateWithLifecycle()
    val userList by viewModel.userList.collectAsStateWithLifecycle()

    val media = allMedia.find { it.id == mediaId } ?: return
    val userEntry = userList.find { it.mediaId == mediaId }
    val vibe = rememberVibePalette(media)
    val preset = LocalAppThemePreset.current
    var playingTrack by remember { mutableStateOf<String?>(null) }
    val isManga = media.format.equals("MANGA", ignoreCase = true)

    var showAddToListDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SlateBackground)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 40.dp)
        ) {
            // Ambient blurred header banner + Poster overlay
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                ) {
                    // Blurred banner
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(media.bannerUrl ?: media.coverUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(16.dp)
                    )

                    // Dynamic Vibe Scrim gradient
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.4f),
                                        vibe.dominant.copy(alpha = 0.20f),
                                        Color(0xCC121216),
                                        SlateBackground
                                    )
                                )
                            )
                    )

                    // Top Bar Back and Favorite Button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.5f))
                                .testTag("details_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        IconButton(
                            onClick = { viewModel.toggleFavorite(media) },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.5f))
                        ) {
                            Icon(
                                imageVector = if (media.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (media.isFavorite) Color(0xFFEF4444) else Color.White
                            )
                        }
                    }

                    // Poster & Quick Info overlay
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart)
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // Thumbnail poster with dynamic glowingBorder
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(media.coverUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = media.titleEnglish,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(110.dp)
                                .aspectRatio(0.7f)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.5.dp, SlateBorder, RoundedCornerShape(12.dp))
                                .glowingBorder(
                                    shape = RoundedCornerShape(12.dp),
                                    glowColor = vibe.dominant,
                                    glowRadius = 10.dp,
                                    glowAlpha = 0.32f
                                )
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                FormatPill(format = media.format)
                                media.aniListScore?.let { AniListScorePill(score = it) }
                                media.malScore?.let { MalScorePill(score = it) }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = media.titleEnglish,
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2
                            )

                            if (media.titleRomaji != media.titleEnglish) {
                                Text(
                                    text = media.titleRomaji,
                                    color = TextMuted,
                                    fontSize = 11.sp,
                                    maxLines = 1
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "${media.status} • ${media.season ?: "2026"}",
                                color = CyanAniList,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // CTAs: Play Episode / Read Chapter & Add To List
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            val nextUnit = (userEntry?.progress ?: 0) + 1
                            if (isManga) {
                                viewModel.startMangaReading(media, nextUnit)
                            } else {
                                onPlayClick(media, nextUnit)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = vibe.dominant),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .glowingBorder(
                                shape = RoundedCornerShape(12.dp),
                                glowColor = vibe.dominant,
                                glowRadius = 10.dp,
                                glowAlpha = 0.35f
                            )
                            .testTag("details_watch_now_button")
                    ) {
                        Icon(
                            imageVector = if (isManga) Icons.AutoMirrored.Filled.MenuBook else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isManga) {
                                if (userEntry != null && userEntry.progress > 0) "Resume Ch ${userEntry.progress + 1}" else "Read Chapter 1"
                            } else {
                                if (userEntry != null && userEntry.progress > 0) "Resume Ep ${userEntry.progress + 1}" else "Stream Episode 1"
                            },
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = { showAddToListDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = SlateSurface,
                            contentColor = TextPrimary
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = Brush.linearGradient(listOf(SlateBorder, SlateBorder))
                        ),
                        modifier = Modifier
                            .height(48.dp)
                            .glowingBorder(
                                shape = RoundedCornerShape(12.dp),
                                glowColor = preset.glow,
                                glowRadius = 8.dp,
                                glowAlpha = 0.28f
                            )
                            .testTag("details_add_to_list_button")
                    ) {
                        Icon(
                            imageVector = if (userEntry != null) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            tint = if (userEntry != null) preset.glow else TextPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = userEntry?.status ?: "Add to List",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Synopsis
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                    Text(
                        text = "Synopsis",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = media.description,
                        color = TextPrimary.copy(alpha = 0.85f),
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }
            }

            // Key-Value Metadata Grid
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                    Text(
                        text = "Information",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SlateSurface),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(SlateBorder, SlateBorder))),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            MetadataRow(label = "Format", value = media.format)
                            MetadataRow(label = "Episodes", value = "${media.episodes ?: "TBA"}")
                            MetadataRow(label = "Duration", value = "${media.durationMinutes ?: 24} mins per ep")
                            MetadataRow(label = "Source", value = media.source ?: "Manga")
                            MetadataRow(label = "Studios", value = media.studios.joinToString(", ").ifEmpty { "MAPPA / Wit" })
                            MetadataRow(label = "Start Date", value = media.startDate ?: "2026")
                            MetadataRow(label = "Mean Score", value = "${media.aniListScore ?: 8.5} / 10 (AniList)")
                        }
                    }
                }
            }

            // Tag Chips with percentage relevancy
            if (media.tagsWithRelevance.isNotEmpty()) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                        Text(
                            text = "Tags & Relevancy",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            media.tagsWithRelevance.forEach { (tag, relevance) ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(SlateSurface)
                                        .border(1.dp, SlateBorder, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = tag,
                                            color = TextPrimary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "$relevance%",
                                            color = NeonPurple,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Openings and Endings Track List with Minimalist 5-bar AudioVisualizerBar
            if (media.openings.isNotEmpty() || media.endings.isNotEmpty()) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Theme Songs (OP / ED)",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (playingTrack != null) {
                                Text(
                                    text = "PLAYING PREVIEW",
                                    color = vibe.dominant,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = SlateSurface),
                            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(SlateBorder, SlateBorder))),
                            modifier = Modifier
                                .fillMaxWidth()
                                .glowingBorder(
                                    shape = RoundedCornerShape(12.dp),
                                    glowColor = vibe.dominant,
                                    glowRadius = 6.dp,
                                    glowAlpha = if (playingTrack != null) 0.35f else 0.12f
                                )
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                media.openings.forEachIndexed { idx, op ->
                                    val isTrackPlaying = playingTrack == op
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable {
                                                playingTrack = if (isTrackPlaying) null else op
                                            }
                                            .padding(vertical = 6.dp, horizontal = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.MusicNote,
                                            contentDescription = null,
                                            tint = if (isTrackPlaying) CyanAniList else TextMuted,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "OP ${idx + 1}: $op",
                                            color = if (isTrackPlaying) CyanAniList else TextPrimary,
                                            fontSize = 12.sp,
                                            fontWeight = if (isTrackPlaying) FontWeight.Bold else FontWeight.Normal,
                                            modifier = Modifier.weight(1f),
                                            maxLines = 1
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        if (isTrackPlaying) {
                                            AudioVisualizerBar(
                                                isPlaying = true,
                                                barColor = CyanAniList,
                                                modifier = Modifier.padding(horizontal = 4.dp)
                                            )
                                        }
                                    }
                                }
                                media.endings.forEachIndexed { idx, ed ->
                                    val isTrackPlaying = playingTrack == ed
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable {
                                                playingTrack = if (isTrackPlaying) null else ed
                                            }
                                            .padding(vertical = 6.dp, horizontal = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.MusicNote,
                                            contentDescription = null,
                                            tint = if (isTrackPlaying) AmberMAL else TextMuted,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "ED ${idx + 1}: $ed",
                                            color = if (isTrackPlaying) AmberMAL else TextPrimary,
                                            fontSize = 12.sp,
                                            fontWeight = if (isTrackPlaying) FontWeight.Bold else FontWeight.Normal,
                                            modifier = Modifier.weight(1f),
                                            maxLines = 1
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        if (isTrackPlaying) {
                                            AudioVisualizerBar(
                                                isPlaying = true,
                                                barColor = AmberMAL,
                                                modifier = Modifier.padding(horizontal = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Relations (Prequel / Sequel)
            if (media.relations.isNotEmpty()) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                        Text(
                            text = "Relations",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(media.relations) { rel ->
                                RelationCard(
                                    relation = rel,
                                    onClick = { onRelationClick(rel.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Add to List Dialog
    if (showAddToListDialog) {
        AddToListDialog(
            currentStatus = userEntry?.status ?: "WATCHING",
            currentScore = userEntry?.userScore ?: 8.5f,
            onDismiss = { showAddToListDialog = false },
            onConfirm = { status, score ->
                viewModel.updateListStatus(media, status, score)
                showAddToListDialog = false
            }
        )
    }
}

@Composable
private fun MetadataRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextMuted, fontSize = 12.sp)
        Text(text = value, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun RelationCard(
    relation: MediaRelation,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .clickable(onClick = onClick)
            .border(1.dp, SlateBorder, RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(relation.coverUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = relation.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 44.dp, height = 60.dp)
                    .clip(RoundedCornerShape(6.dp))
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(NeonPurple.copy(alpha = 0.2f))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = relation.relationType,
                        color = NeonPurple,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = relation.title,
                    color = TextPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AddToListDialog(
    currentStatus: String,
    currentScore: Float,
    onDismiss: () -> Unit,
    onConfirm: (status: String, score: Float) -> Unit
) {
    val preset = LocalAppThemePreset.current
    var selectedStatus by remember { mutableStateOf(currentStatus) }
    var score by remember { mutableFloatStateOf(currentScore) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = preset.cards.copy(alpha = 0.94f),
            border = androidx.compose.foundation.BorderStroke(1.dp, preset.border.copy(alpha = 0.8f)),
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(0, offsetY.roundToInt().coerceAtLeast(0)) }
                .draggable(
                    state = rememberDraggableState { delta ->
                        offsetY += delta
                    },
                    orientation = Orientation.Vertical,
                    onDragStopped = { velocity ->
                        if (offsetY > 150f || velocity > 800f) {
                            onDismiss()
                        } else {
                            offsetY = 0f
                        }
                    }
                )
                .glowingBorder(
                    shape = RoundedCornerShape(20.dp),
                    glowColor = preset.glow,
                    glowRadius = 14.dp,
                    glowAlpha = 0.35f
                )
        ) {
            Column(modifier = Modifier.padding(22.dp)) {
                // Drag handle pill
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(40.dp)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.3f))
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sync with AniList & MAL",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(preset.glow)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Watch / Read Status", color = TextMuted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))

                val statuses = listOf("WATCHING", "PLANNING", "COMPLETED", "ON_HOLD", "DROPPED")
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    statuses.forEach { st ->
                        val isSelected = selectedStatus == st
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) preset.glow else SlateSurfaceHigh)
                                .clickable { selectedStatus = st }
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = st,
                                color = if (isSelected) Color.White else TextMuted,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Rating Score", color = TextMuted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text(
                        text = String.format("%.1f / 10", score),
                        color = AmberMAL,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Slider(
                    value = score,
                    onValueChange = { score = it },
                    valueRange = 1f..10f,
                    steps = 18,
                    colors = SliderDefaults.colors(
                        thumbColor = AmberMAL,
                        activeTrackColor = AmberMAL,
                        inactiveTrackColor = SlateSurfaceHigh
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Cancel", color = TextMuted)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = { onConfirm(selectedStatus, (Math.round(score * 10f) / 10f)) },
                        colors = ButtonDefaults.buttonColors(containerColor = preset.glow),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Save & Sync", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
