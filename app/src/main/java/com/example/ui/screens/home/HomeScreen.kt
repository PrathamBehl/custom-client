package com.example.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.MediaEntity
import com.example.data.model.UserListEntry
import com.example.ui.components.HeroCarousel
import com.example.ui.components.MediaPosterCard
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SlateBackground
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateSurfaceHigh
import com.example.ui.theme.SlateSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    isTablet: Boolean,
    onMediaClick: (Int) -> Unit,
    onPlayClick: (MediaEntity, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val allMedia by viewModel.allMedia.collectAsStateWithLifecycle()
    val continueWatching by viewModel.continueWatching.collectAsStateWithLifecycle()
    val continueReading by viewModel.continueReading.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val remoteResults by viewModel.remoteSearchResults.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.mediaTypeFilter.collectAsStateWithLifecycle()

    var showSearchInput by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    val filteredMedia = allMedia.filter {
        when (selectedFilter) {
            "ANIME" -> it.format != "MANGA"
            "MANGA" -> it.format == "MANGA"
            else -> true
        }
    }

    val displayGridItems = if (searchQuery.isNotBlank() && remoteResults.isNotEmpty()) {
        remoteResults
    } else {
        filteredMedia
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SlateBackground)
            .statusBarsPadding()
    ) {
        // High Density Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Anime Slate",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "SPRING SEASON 2026",
                    color = NeonPurple,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.2.sp
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                // Search Icon Circle
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(SlateSurface)
                        .border(1.dp, SlateBorder, CircleShape)
                        .clickable { showSearchInput = !showSearchInput },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = if (showSearchInput || searchQuery.isNotEmpty()) NeonPurple else TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Profile Avatar Circle
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(NeonPurple),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "JD",
                        color = SlateBackground,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Search Bar (Expanded when toggled or query active)
        AnimatedVisibility(visible = showSearchInput || searchQuery.isNotEmpty()) {
            HomeSearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.searchQuery.value = it },
                isSearching = isSearching,
                selectedFilter = selectedFilter,
                onFilterChange = { viewModel.mediaTypeFilter.value = it },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        // Quick Category Filter Pills when search is not expanded
        if (!showSearchInput && searchQuery.isEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                listOf("ALL", "ANIME", "MANGA").forEach { filter ->
                    val isSelected = selectedFilter == filter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) NeonPurple else SlateSurface)
                            .border(
                                1.dp,
                                if (isSelected) NeonPurple else SlateBorder,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { viewModel.mediaTypeFilter.value = filter }
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = filter,
                            color = if (isSelected) SlateBackground else TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }

        // Dynamic multi-column grid
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = if (isTablet) 150.dp else 115.dp),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 6.dp,
                bottom = if (isTablet) 32.dp else 96.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .testTag("home_media_grid")
        ) {
            // Show Hero Carousel only when not searching
            if (searchQuery.isBlank()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    HeroCarousel(
                        items = allMedia.filter { it.format != "MANGA" }.take(5),
                        onPlayClick = { onPlayClick(it, 1) },
                        onDetailsClick = { onMediaClick(it.id) },
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                // Continue Watching Rail
                if (continueWatching.isNotEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        ContinueSectionRail(
                            title = "Continue Watching",
                            items = continueWatching,
                            onItemClick = { entry ->
                                val media = allMedia.find { it.id == entry.mediaId }
                                if (media != null) {
                                    onPlayClick(media, entry.progress + 1)
                                } else {
                                    onMediaClick(entry.mediaId)
                                }
                            }
                        )
                    }
                }

                // Continue Reading Rail
                if (continueReading.isNotEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        ContinueSectionRail(
                            title = "Continue Reading",
                            items = continueReading,
                            onItemClick = { onMediaClick(it.mediaId) }
                        )
                    }
                }

                // Section Header
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = if (selectedFilter == "MANGA") "Top Manga Spotlight" else "Seasonal Favorites",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "UPDATED 2M AGO",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            } else {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = "Results for \"$searchQuery\"",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            // Grid Items
            items(displayGridItems, key = { it.id }) { media ->
                MediaPosterCard(
                    media = media,
                    onClick = { onMediaClick(media.id) }
                )
            }
        }
    }
}

@Composable
private fun HomeSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    isSearching: Boolean,
    selectedFilter: String,
    onFilterChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search 10,000+ Anime & Manga (AniList)...", fontSize = 13.sp, color = TextMuted) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TextMuted,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                if (isSearching) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        color = NeonPurple,
                        modifier = Modifier.size(16.dp)
                    )
                } else if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SlateSurface,
                unfocusedContainerColor = SlateSurface,
                focusedBorderColor = NeonPurple,
                unfocusedBorderColor = SlateBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_input_field")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Quick Category Filter Pills
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("ALL", "ANIME", "MANGA").forEach { filter ->
                val isSelected = selectedFilter == filter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) NeonPurple else SlateSurfaceVariant)
                        .border(
                            1.dp,
                            if (isSelected) NeonPurple else SlateBorder,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { onFilterChange(filter) }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = filter,
                        color = if (isSelected) Color.White else TextMuted,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun ContinueSectionRail(
    title: String,
    items: List<UserListEntry>,
    onItemClick: (UserListEntry) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "VIEW ALL",
                color = NeonPurple,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items, key = { it.mediaId }) { entry ->
                ContinueCard(entry = entry, onClick = { onItemClick(entry) })
            }
        }
    }
}

@Composable
private fun ContinueCard(
    entry: UserListEntry,
    onClick: () -> Unit
) {
    val progressFraction = (entry.progress.toFloat() / entry.totalUnits.toFloat()).coerceIn(0f, 1f)
    val percentage = (progressFraction * 100).toInt()

    Box(
        modifier = Modifier
            .width(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(SlateSurface)
            .border(1.dp, SlateBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Aspect-video thumbnail box with progress tag overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(SlateSurfaceHigh)
            ) {
                if (entry.coverUrl.isNotBlank()) {
                    coil.compose.AsyncImage(
                        model = entry.coverUrl,
                        contentDescription = entry.title,
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Top right high-density percentage tag
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(5.dp)
                ) {
                    com.example.ui.components.HighDensityProgressTag(percentage = percentage)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = entry.title,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(1.dp))

            Text(
                text = if (entry.format == "MANGA") "Ch ${entry.progress} of ${entry.totalUnits}" else "Ep ${entry.progress} of ${entry.totalUnits}",
                color = Color(0xFF94A3B8),
                fontSize = 9.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Thin progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(SlateSurfaceHigh)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = progressFraction)
                        .height(3.dp)
                        .background(NeonPurple)
                )
            }
        }
    }
}
