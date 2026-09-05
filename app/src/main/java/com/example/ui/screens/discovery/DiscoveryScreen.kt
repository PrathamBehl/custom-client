package com.example.ui.screens.discovery

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.MediaPosterCard
import com.example.ui.theme.AmberMAL
import com.example.ui.theme.CyanAniList
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SlateBackground
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateSurfaceHigh
import com.example.ui.theme.SlateSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.viewmodel.MainViewModel

val genresList = listOf(
    "All Genres",
    "Action",
    "Adventure",
    "Fantasy",
    "Romance",
    "Mystery",
    "Comedy",
    "Slice of Life",
    "Psychological"
)

val seasonsList = listOf(
    "Summer 2026",
    "Spring 2026",
    "Winter 2026",
    "Fall 2025"
)

@Composable
fun DiscoveryScreen(
    viewModel: MainViewModel,
    isTablet: Boolean,
    onMediaClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val allMedia by viewModel.allMedia.collectAsStateWithLifecycle()
    var selectedGenre by remember { mutableStateOf("All Genres") }
    var selectedSeason by remember { mutableStateOf("Summer 2026") }
    var selectedSortMode by remember { mutableStateOf("TRENDING") } // TRENDING, TOP_RATED, SEASONAL

    val filteredList = allMedia.filter { media ->
        val genreMatches = if (selectedGenre == "All Genres") true else media.genres.contains(selectedGenre)
        val seasonMatches = if (selectedSortMode == "SEASONAL") media.season?.equals(selectedSeason, ignoreCase = true) == true else true
        genreMatches && seasonMatches
    }.sortedWith(
        when (selectedSortMode) {
            "TOP_RATED" -> compareByDescending { it.aniListScore ?: 0f }
            else -> compareByDescending { it.id }
        }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SlateBackground)
            .statusBarsPadding()
    ) {
        // Top Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Discovery Hub",
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Browse seasonal charts, top scores, and curated categories",
                color = TextMuted,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Sort Pill Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DiscoveryTabPill(
                    label = "Trending",
                    icon = Icons.Default.TrendingUp,
                    isSelected = selectedSortMode == "TRENDING",
                    onClick = { selectedSortMode = "TRENDING" }
                )
                DiscoveryTabPill(
                    label = "Top Rated",
                    icon = Icons.Default.AutoAwesome,
                    isSelected = selectedSortMode == "TOP_RATED",
                    onClick = { selectedSortMode = "TOP_RATED" }
                )
                DiscoveryTabPill(
                    label = "Seasonal",
                    icon = Icons.Default.Category,
                    isSelected = selectedSortMode == "SEASONAL",
                    onClick = { selectedSortMode = "SEASONAL" }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Genre Chips Horizontal Row
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(genresList) { genre ->
                    val isSelected = selectedGenre == genre
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) NeonPurple else SlateSurfaceVariant)
                            .border(
                                1.dp,
                                if (isSelected) NeonPurple else SlateBorder,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedGenre = genre }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = genre,
                            color = if (isSelected) Color.White else TextMuted,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            // Seasonal Pills (if Seasonal selected)
            if (selectedSortMode == "SEASONAL") {
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(seasonsList) { season ->
                        val isSelected = selectedSeason == season
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) CyanAniList else SlateSurfaceHigh)
                            .clickable { selectedSeason = season }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = season,
                                color = if (isSelected) Color.Black else TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Multi-column Grid
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = if (isTablet) 150.dp else 115.dp),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = if (isTablet) 32.dp else 96.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .testTag("discovery_grid")
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Showing ${filteredList.size} Titles",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                    Text(
                        text = if (selectedSortMode == "TOP_RATED") "Sorted by Mean Score" else "AniList Real-Time",
                        color = AmberMAL,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            items(filteredList, key = { it.id }) { media ->
                MediaPosterCard(
                    media = media,
                    onClick = { onMediaClick(media.id) }
                )
            }
        }
    }
}

@Composable
private fun DiscoveryTabPill(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) NeonPurple else SlateSurface)
            .border(
                1.dp,
                if (isSelected) NeonPurple else SlateBorder,
                RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color.White else TextMuted,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                color = if (isSelected) Color.White else TextMuted,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}
