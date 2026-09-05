package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AdaptiveScaffold
import com.example.ui.screens.calendar.CalendarScreen
import com.example.ui.screens.details.MediaDetailsScreen
import com.example.ui.screens.discovery.DiscoveryScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.library.LibraryScreen
import com.example.ui.screens.player.VideoPlayerScreen
import com.example.ui.screens.reader.MangaReaderScreen
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.screens.social.SocialFeedScreen
import com.example.ui.theme.LocalAppThemePreset
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SlateBackground
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val themeState by viewModel.themeState.collectAsStateWithLifecycle()
            MyApplicationTheme(themeState = themeState) {
                AnimeSlateApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun AnimeSlateApp(viewModel: MainViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val selectedMediaId by viewModel.selectedMediaId.collectAsStateWithLifecycle()
    val activePlayer by viewModel.activePlayer.collectAsStateWithLifecycle()
    val activeMangaReader by viewModel.activeMangaReader.collectAsStateWithLifecycle()
    val preset = LocalAppThemePreset.current

    // Handle back button behavior for nested manga reader, player & details
    BackHandler(enabled = activeMangaReader != null || activePlayer != null || selectedMediaId != null) {
        if (activeMangaReader != null) {
            viewModel.closeMangaReader()
        } else if (activePlayer != null) {
            viewModel.closePlayer()
        } else if (selectedMediaId != null) {
            viewModel.closeDetails()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(preset.surface)
    ) {
        if (activeMangaReader != null) {
            val (media, chapter) = activeMangaReader!!
            MangaReaderScreen(
                media = media,
                chapterNumber = chapter,
                onClose = { viewModel.closeMangaReader() },
                onChapterChange = { newCh ->
                    viewModel.startReading(media, newCh)
                }
            )
        } else if (activePlayer != null) {
            val (media, episode) = activePlayer!!
            VideoPlayerScreen(
                media = media,
                episodeNumber = episode,
                viewModel = viewModel,
                onClose = { viewModel.closePlayer() }
            )
        } else if (selectedMediaId != null) {
            MediaDetailsScreen(
                mediaId = selectedMediaId!!,
                viewModel = viewModel,
                onBackClick = { viewModel.closeDetails() },
                onPlayClick = { media, ep ->
                    viewModel.startPlaying(media, ep)
                },
                onRelationClick = { relId ->
                    viewModel.openDetails(relId)
                }
            )
        } else {
            AdaptiveScaffold(
                currentTab = currentTab,
                onTabSelected = { viewModel.selectTab(it) }
            ) { isTablet ->
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "tab_crossfade"
                ) { tab ->
                    when (tab) {
                        AppTab.HOME -> HomeScreen(
                            viewModel = viewModel,
                            isTablet = isTablet,
                            onMediaClick = { viewModel.openDetails(it) },
                            onPlayClick = { media, ep ->
                                viewModel.startPlaying(media, ep)
                            }
                        )
                        AppTab.CALENDAR -> CalendarScreen(
                            viewModel = viewModel,
                            isTablet = isTablet,
                            onMediaClick = { viewModel.openDetails(it) }
                        )
                        AppTab.DISCOVERY -> DiscoveryScreen(
                            viewModel = viewModel,
                            isTablet = isTablet,
                            onMediaClick = { viewModel.openDetails(it) }
                        )
                        AppTab.SOCIAL -> SocialFeedScreen(
                            viewModel = viewModel,
                            isTablet = isTablet,
                            onMediaClick = { viewModel.openDetails(it) }
                        )
                        AppTab.LIBRARY -> LibraryScreen(
                            viewModel = viewModel,
                            isTablet = isTablet,
                            onMediaClick = { viewModel.openDetails(it) }
                        )
                        AppTab.SETTINGS -> SettingsScreen(
                            viewModel = viewModel,
                            isTablet = isTablet
                        )
                    }
                }
            }
        }
    }
}
