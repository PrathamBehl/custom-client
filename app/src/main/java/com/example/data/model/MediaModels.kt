package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@Entity(tableName = "media_entries")
data class MediaEntity(
    @PrimaryKey val id: Int, // AniList ID
    val malId: Int? = null,
    val titleRomaji: String,
    val titleEnglish: String,
    val titleNative: String? = null,
    val coverUrl: String,
    val bannerUrl: String? = null,
    val format: String = "TV", // TV, MOVIE, MANGA, OVA, ONA
    val episodes: Int? = null,
    val chapters: Int? = null,
    val durationMinutes: Int? = 24,
    val aniListScore: Float? = null, // e.g. 8.4 (out of 10)
    val malScore: Float? = null,     // e.g. 8.62 (out of 10)
    val genres: List<String> = emptyList(),
    val themes: List<String> = emptyList(),
    val status: String = "RELEASING", // RELEASING, FINISHED, NOT_YET_RELEASED
    val season: String? = "SUMMER 2026",
    val description: String = "",
    val source: String? = "Manga",
    val startDate: String? = "2026-04-01",
    val endDate: String? = null,
    val studios: List<String> = emptyList(),
    val openings: List<String> = emptyList(),
    val endings: List<String> = emptyList(),
    val nextAiringEpisode: Int? = null,
    val nextAiringSeconds: Long? = null,
    val tagsWithRelevance: Map<String, Int> = emptyMap(),
    val relations: List<MediaRelation> = emptyList(),
    val isFavorite: Boolean = false,
    val isRewatching: Boolean = false
)

data class MediaRelation(
    val id: Int,
    val title: String,
    val relationType: String, // PREQUEL, SEQUEL, SPIN_OFF, SIDE_STORY
    val format: String,
    val coverUrl: String
)

@Entity(tableName = "user_lists")
data class UserListEntry(
    @PrimaryKey val mediaId: Int,
    val title: String,
    val coverUrl: String,
    val format: String,
    val status: String, // WATCHING, READING, PLANNING, COMPLETED, ON_HOLD, DROPPED
    val progress: Int = 0,
    val totalUnits: Int = 12,
    val userScore: Float = 0f,
    val rewatchCount: Int = 0,
    val notes: String = "",
    val lastWatchedTimestamp: Long = System.currentTimeMillis(),
    val aniListSynced: Boolean = true,
    val malSynced: Boolean = true
)

data class ActivityItem(
    val id: String,
    val userName: String,
    val userAvatar: String,
    val activityType: String, // "watched", "completed", "rated", "reviewed"
    val progressText: String,
    val text: String,
    val timeAgo: String,
    val likesCount: Int,
    val mediaId: Int,
    val mediaTitle: String,
    val mediaCover: String,
    val score: Float? = null
)

data class ReleaseScheduleItem(
    val mediaId: Int,
    val title: String,
    val coverUrl: String,
    val episode: Int,
    val dayOfWeek: Int, // 1 = Monday ... 7 = Sunday
    val airTime: String, // "18:30 JST"
    val countdownSeconds: Long,
    val format: String = "TV",
    val aniListScore: Float? = 8.5f,
    val isSimulcast: Boolean = true
)

data class StreamSource(
    val id: String,
    val quality: String, // "1080p Ultra", "720p HD", "Auto"
    val resolutionLabel: String,
    val url: String,
    val isHls: Boolean = true,
    val bitrate: String = "4.8 Mbps",
    val audioLanguage: String = "Japanese (Original)",
    val latencyMs: Long = 24
)

data class SubtitleTrack(
    val id: String,
    val language: String,
    val format: String = "ASS", // ASS or SRT
    val isSelected: Boolean = false
)

class MediaConverters {
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return value?.joinToString(";;;") ?: ""
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrEmpty()) return emptyList()
        return value.split(";;;").filter { it.isNotEmpty() }
    }

    @TypeConverter
    fun fromTagMap(value: Map<String, Int>?): String {
        if (value.isNullOrEmpty()) return ""
        val type = Types.newParameterizedType(Map::class.java, String::class.java, Integer::class.java)
        val adapter = moshi.adapter<Map<String, Int>>(type)
        return adapter.toJson(value)
    }

    @TypeConverter
    fun toTagMap(value: String?): Map<String, Int> {
        if (value.isNullOrEmpty()) return emptyMap()
        return try {
            val type = Types.newParameterizedType(Map::class.java, String::class.java, Integer::class.java)
            val adapter = moshi.adapter<Map<String, Int>>(type)
            adapter.fromJson(value) ?: emptyMap()
        } catch (e: Exception) {
            emptyMap()
        }
    }

    @TypeConverter
    fun fromRelationsList(value: List<MediaRelation>?): String {
        if (value.isNullOrEmpty()) return ""
        val type = Types.newParameterizedType(List::class.java, MediaRelation::class.java)
        val adapter = moshi.adapter<List<MediaRelation>>(type)
        return adapter.toJson(value)
    }

    @TypeConverter
    fun toRelationsList(value: String?): List<MediaRelation> {
        if (value.isNullOrEmpty()) return emptyList()
        return try {
            val type = Types.newParameterizedType(List::class.java, MediaRelation::class.java)
            val adapter = moshi.adapter<List<MediaRelation>>(type)
            adapter.fromJson(value) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
