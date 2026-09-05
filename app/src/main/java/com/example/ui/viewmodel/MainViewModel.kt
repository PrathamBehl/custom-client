package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.ActivityItem
import com.example.data.model.MediaEntity
import com.example.data.model.ReleaseScheduleItem
import com.example.data.model.StreamSource
import com.example.data.model.UserListEntry
import com.example.data.remote.AniListGraphQLRepository
import com.example.data.remote.DefaultMediaCatalog
import com.example.data.remote.StreamAggregator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab {
    HOME,
    CALENDAR,
    DISCOVERY,
    SOCIAL,
    LIBRARY,
    SETTINGS
}

data class UserStats(
    val episodesWatched: Int,
    val chaptersRead: Int,
    val meanScore: Float,
    val daysWatched: Float,
    val totalEntries: Int
)

@OptIn(FlowPreview::class)
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repository = AniListGraphQLRepository(db.mediaDao(), db.userListDao())
    private val streamAggregator = StreamAggregator()

    // Navigation State
    private val _currentTab = MutableStateFlow(AppTab.HOME)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _selectedMediaId = MutableStateFlow<Int?>(null)
    val selectedMediaId: StateFlow<Int?> = _selectedMediaId.asStateFlow()

    // Player State
    private val _activePlayer = MutableStateFlow<Pair<MediaEntity, Int>?>(null)
    val activePlayer: StateFlow<Pair<MediaEntity, Int>?> = _activePlayer.asStateFlow()

    // Manga Reader State
    private val _activeMangaReader = MutableStateFlow<Pair<MediaEntity, Int>?>(null)
    val activeMangaReader: StateFlow<Pair<MediaEntity, Int>?> = _activeMangaReader.asStateFlow()

    // Multi-Theme Engine & Contours State
    private val _themeState = MutableStateFlow(com.example.ui.theme.ThemeState(com.example.ui.theme.ThemePreset.LIQUID_CHARCOAL, isGlowEnabled = true))
    val themeState: StateFlow<com.example.ui.theme.ThemeState> = _themeState.asStateFlow()
    val themePreset = MutableStateFlow(com.example.ui.theme.ThemePreset.LIQUID_CHARCOAL)

    fun setThemePreset(preset: com.example.ui.theme.ThemePreset) {
        _themeState.value = _themeState.value.copy(preset = preset)
        themePreset.value = preset
    }

    fun setGlowEnabled(enabled: Boolean) {
        _themeState.value = _themeState.value.copy(isGlowEnabled = enabled)
    }

    // Dynamic Live Media Details State
    private val _currentMediaDetails = MutableStateFlow<MediaEntity?>(null)
    val currentMediaDetails: StateFlow<MediaEntity?> = _currentMediaDetails.asStateFlow()
    val isLoadingMediaDetails = MutableStateFlow(false)

    // AniList OAuth User Session
    private val _aniListUser = MutableStateFlow<com.example.data.remote.AniListUser?>(null)
    val aniListUser: StateFlow<com.example.data.remote.AniListUser?> = _aniListUser.asStateFlow()
    val isAuthenticating = MutableStateFlow(false)
    val authError = MutableStateFlow<String?>(null)

    fun loginWithAniListToken(token: String) {
        viewModelScope.launch {
            isAuthenticating.value = true
            authError.value = null
            val result = repository.remoteRepo.verifyAndFetchViewer(token.trim())
            if (result.isSuccess) {
                val user = result.getOrNull()
                _aniListUser.value = user
                val prefs = getApplication<Application>().getSharedPreferences("ani_slate_prefs", android.content.Context.MODE_PRIVATE)
                prefs.edit().putString("anilist_token", token.trim()).apply()
            } else {
                authError.value = result.exceptionOrNull()?.message ?: "Authentication failed"
            }
            isAuthenticating.value = false
        }
    }

    fun logoutAniList() {
        _aniListUser.value = null
        val prefs = getApplication<Application>().getSharedPreferences("ani_slate_prefs", android.content.Context.MODE_PRIVATE)
        prefs.edit().remove("anilist_token").apply()
    }

    fun startMangaReading(media: MediaEntity, chapter: Int = 1) {
        _activeMangaReader.value = Pair(media, chapter)
    }

    fun startReading(media: MediaEntity, chapter: Int = 1) {
        startMangaReading(media, chapter)
    }

    fun closeMangaReader() {
        _activeMangaReader.value = null
    }

    private val _availableStreams = MutableStateFlow<List<StreamSource>>(emptyList())
    val availableStreams: StateFlow<List<StreamSource>> = _availableStreams.asStateFlow()

    // Search Query (Debounced 200ms reactive search)
    val searchQuery = MutableStateFlow("")
    private val _remoteSearchResults = MutableStateFlow<List<MediaEntity>>(emptyList())
    val remoteSearchResults: StateFlow<List<MediaEntity>> = _remoteSearchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    // Content Filter (ALL, ANIME, MANGA)
    val mediaTypeFilter = MutableStateFlow("ALL")

    // Calendar Day Selector (1 = Monday, ..., 7 = Sunday)
    val selectedDayOfWeek = MutableStateFlow(5) // Default Friday
    val releaseSchedule = MutableStateFlow(DefaultMediaCatalog.sampleSchedule)

    // Social Activities
    val socialActivities = MutableStateFlow(DefaultMediaCatalog.sampleActivities)

    // All local media from Room
    val allMedia: StateFlow<List<MediaEntity>> = repository.getAllMedia()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DefaultMediaCatalog.sampleMediaList)

    // User List from Room
    val userList: StateFlow<List<UserListEntry>> = repository.getUserEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DefaultMediaCatalog.sampleUserListEntries)

    // Filtered lists
    val continueWatching: StateFlow<List<UserListEntry>> = userList.combine(mediaTypeFilter) { list, _ ->
        list.filter { it.status == "WATCHING" }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val continueReading: StateFlow<List<UserListEntry>> = userList.combine(mediaTypeFilter) { list, _ ->
        list.filter { it.status == "READING" }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Calculated User Stats
    val userStats: StateFlow<UserStats> = userList.combine(allMedia) { entries, _ ->
        val episodes = entries.filter { it.format != "MANGA" }.sumOf { it.progress }
        val chapters = entries.filter { it.format == "MANGA" }.sumOf { it.progress }
        val scoredList = entries.filter { it.userScore > 0 }
        val avgScore = if (scoredList.isNotEmpty()) scoredList.map { it.userScore }.average().toFloat() else 8.9f
        val daysWatched = (episodes * 24f) / (60f * 24f)
        UserStats(
            episodesWatched = episodes,
            chaptersRead = chapters,
            meanScore = (Math.round(avgScore * 10f) / 10f),
            daysWatched = (Math.round(daysWatched * 10f) / 10f),
            totalEntries = entries.size
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UserStats(episodesWatched = 46, chaptersRead = 265, meanScore = 9.2f, daysWatched = 0.8f, totalEntries = 4)
    )

    init {
        // Preload default local catalog into Room immediately
        viewModelScope.launch {
            repository.preloadDefaultCatalog()
            // Pull live AniList trending and calendar data in background
            try {
                repository.fetchTrendingLive()
                val liveSchedule = repository.fetchAiringScheduleLive()
                if (liveSchedule.isNotEmpty()) {
                    releaseSchedule.value = liveSchedule
                }
            } catch (e: Exception) {
                // Graceful fallback to default catalog
            }
        }

        // Restore previously authenticated AniList token
        val prefs = application.getSharedPreferences("ani_slate_prefs", android.content.Context.MODE_PRIVATE)
        val savedToken = prefs.getString("anilist_token", null)
        if (!savedToken.isNullOrBlank()) {
            viewModelScope.launch {
                val res = repository.remoteRepo.verifyAndFetchViewer(savedToken)
                if (res.isSuccess) {
                    _aniListUser.value = res.getOrNull()
                }
            }
        }

        // Debounced 200ms reactive search input
        viewModelScope.launch {
            searchQuery
                .debounce(200)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isNotBlank()) {
                        _isSearching.value = true
                        val results = repository.queryAniList(query)
                        _remoteSearchResults.value = results
                        _isSearching.value = false
                    } else {
                        _remoteSearchResults.value = emptyList()
                        _isSearching.value = false
                    }
                }
        }
    }

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun openDetails(mediaId: Int) {
        _selectedMediaId.value = mediaId
        loadLiveMediaDetails(mediaId)
    }

    fun loadLiveMediaDetails(mediaId: Int) {
        viewModelScope.launch {
            isLoadingMediaDetails.value = true
            // Instant cache hit
            val cached = allMedia.value.find { it.id == mediaId }
            if (cached != null) {
                _currentMediaDetails.value = cached
            }
            // Fetch live AniList GraphQL details including relations & tags
            val live = repository.fetchMediaDetailsLive(mediaId)
            if (live != null) {
                _currentMediaDetails.value = live
            }
            isLoadingMediaDetails.value = false
        }
    }

    fun refreshTrendingAnime() {
        viewModelScope.launch {
            repository.fetchTrendingLive()
        }
    }

    fun refreshPopularManga() {
        viewModelScope.launch {
            repository.fetchPopularMangaLive()
        }
    }

    fun refreshSchedule() {
        viewModelScope.launch {
            val live = repository.fetchAiringScheduleLive()
            if (live.isNotEmpty()) {
                releaseSchedule.value = live
            }
        }
    }

    fun closeDetails() {
        _selectedMediaId.value = null
        _currentMediaDetails.value = null
    }

    fun startPlaying(media: MediaEntity, episode: Int = 1) {
        _activePlayer.value = Pair(media, episode)
        // Concurrently aggregate streams in background
        viewModelScope.launch {
            val streams = streamAggregator.resolveBestStreams(media.id, episode)
            _availableStreams.value = streams
        }
    }

    fun closePlayer() {
        _activePlayer.value = null
    }

    fun updateWatchProgress(
        mediaId: Int,
        newEpisode: Int,
        totalUnits: Int,
        title: String,
        coverUrl: String,
        format: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateWatchProgress(
                mediaId = mediaId,
                newEpisode = newEpisode,
                totalUnits = totalUnits,
                title = title,
                coverUrl = coverUrl,
                format = format
            )
        }
    }

    /**
     * Called when the video playback progress reaches 85% threshold.
     * Automatically syncs and updates AniList & MAL progress!
     */
    fun onWatchThresholdReached(media: MediaEntity, episode: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateWatchProgress(
                mediaId = media.id,
                newEpisode = episode,
                totalUnits = media.episodes ?: 12,
                title = media.titleEnglish,
                coverUrl = media.coverUrl,
                format = media.format
            )
        }
    }

    fun updateListStatus(media: MediaEntity, status: String, rating: Float = 0f) {
        viewModelScope.launch {
            repository.setListStatus(media, status, rating)
        }
    }

    fun toggleFavorite(media: MediaEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(media)
        }
    }

    fun postSocialComment(mediaId: Int, text: String) {
        val newActivity = ActivityItem(
            id = "act_user_${System.currentTimeMillis()}",
            userName = "You",
            userAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&auto=format&fit=crop&q=80",
            activityType = "reviewed",
            progressText = "Shared a thought",
            text = text,
            timeAgo = "Just now",
            likesCount = 1,
            mediaId = mediaId,
            mediaTitle = "Anime Slate",
            mediaCover = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=400&auto=format&fit=crop&q=80",
            score = 9.0f
        )
        socialActivities.value = listOf(newActivity) + socialActivities.value
    }
}
