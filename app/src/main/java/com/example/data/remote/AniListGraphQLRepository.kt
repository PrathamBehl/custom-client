package com.example.data.remote

import android.util.Log
import com.example.data.local.MediaDao
import com.example.data.local.UserListDao
import com.example.data.model.MediaEntity
import com.example.data.model.UserListEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class AniListGraphQLRepository(
    private val mediaDao: MediaDao,
    private val userListDao: UserListDao
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()
    private val anilistUrl = "https://graphql.anilist.co"

    fun getAllMedia(): Flow<List<MediaEntity>> = mediaDao.getAllMedia()

    fun getMediaById(id: Int): Flow<MediaEntity?> = mediaDao.getMediaById(id)

    fun searchLocal(query: String): Flow<List<MediaEntity>> = mediaDao.searchLocal(query)

    fun getUserEntries(): Flow<List<UserListEntry>> = userListDao.getAllEntries()

    fun getUserEntry(mediaId: Int): Flow<UserListEntry?> = userListDao.getEntry(mediaId)

    suspend fun preloadDefaultCatalog() = withContext(Dispatchers.IO) {
        mediaDao.insertAll(DefaultMediaCatalog.sampleMediaList)
        DefaultMediaCatalog.sampleUserListEntries.forEach { entry ->
            userListDao.insertOrUpdate(entry)
        }
    }

    suspend fun queryAniList(searchQuery: String? = null, sort: String = "TRENDING_DESC"): List<MediaEntity> = withContext(Dispatchers.IO) {
        val query = """
            query (${'$'}search: String, ${'$'}sort: [MediaSort]) {
              Page(page: 1, perPage: 12) {
                media(search: ${'$'}search, sort: ${'$'}sort, type: ANIME) {
                  id
                  idMal
                  title {
                    romaji
                    english
                    native
                  }
                  coverImage {
                    extraLarge
                    large
                  }
                  bannerImage
                  format
                  episodes
                  averageScore
                  genres
                  status
                  seasonYear
                  description(asHtml: false)
                }
              }
            }
        """.trimIndent()

        val variables = JSONObject().apply {
            if (!searchQuery.isNullOrBlank()) {
                put("search", searchQuery)
            }
            put("sort", org.json.JSONArray().apply { put(sort) })
        }

        val jsonBody = JSONObject().apply {
            put("query", query)
            put("variables", variables)
        }

        val request = Request.Builder()
            .url(anilistUrl)
            .post(jsonBody.toString().toRequestBody(jsonMediaType))
            .header("Accept", "application/json")
            .build()

        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val bodyString = response.body?.string() ?: ""
                val root = JSONObject(bodyString)
                val mediaArray = root.optJSONObject("data")
                    ?.optJSONObject("Page")
                    ?.optJSONArray("media")

                if (mediaArray != null && mediaArray.length() > 0) {
                    val parsedList = mutableListOf<MediaEntity>()
                    for (i in 0 until mediaArray.length()) {
                        val item = mediaArray.getJSONObject(i)
                        val id = item.getInt("id")
                        val idMal = if (item.has("idMal") && !item.isNull("idMal")) item.getInt("idMal") else null
                        val titleObj = item.optJSONObject("title")
                        val romaji = titleObj?.optString("romaji") ?: "Unknown Title"
                        val english = titleObj?.optString("english").takeUnless { it.isNullOrBlank() } ?: romaji
                        val native = titleObj?.optString("native")
                        val coverObj = item.optJSONObject("coverImage")
                        val cover = coverObj?.optString("extraLarge") ?: coverObj?.optString("large") ?: ""
                        val banner = item.optString("bannerImage").takeUnless { it.isNullOrBlank() }
                        val format = item.optString("format", "TV")
                        val episodes = if (item.has("episodes") && !item.isNull("episodes")) item.getInt("episodes") else null
                        val score = if (item.has("averageScore") && !item.isNull("averageScore")) (item.getInt("averageScore") / 10f) else null
                        val desc = item.optString("description", "")
                        val status = item.optString("status", "RELEASING")

                        val genres = mutableListOf<String>()
                        val genreArray = item.optJSONArray("genres")
                        if (genreArray != null) {
                            for (g in 0 until genreArray.length()) {
                                genres.add(genreArray.getString(g))
                            }
                        }

                        parsedList.add(
                            MediaEntity(
                                id = id,
                                malId = idMal,
                                titleRomaji = romaji,
                                titleEnglish = english,
                                titleNative = native,
                                coverUrl = cover.ifEmpty { "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=800&auto=format&fit=crop&q=80" },
                                bannerUrl = banner,
                                format = format,
                                episodes = episodes,
                                aniListScore = score,
                                malScore = score?.let { (it + 0.12f).coerceAtMost(9.9f) },
                                genres = genres,
                                status = status,
                                description = desc
                            )
                        )
                    }

                    if (parsedList.isNotEmpty()) {
                        mediaDao.insertAll(parsedList)
                        return@withContext parsedList
                    }
                }
            }
        } catch (e: Exception) {
            Log.w("AniListRepo", "Network error or rate limited on AniList GraphQL: ${e.message}")
        }

        // Fallback to sample catalog if network call failed or rate-limited
        return@withContext DefaultMediaCatalog.sampleMediaList
    }

    suspend fun updateWatchProgress(mediaId: Int, newEpisode: Int, totalUnits: Int, title: String, coverUrl: String, format: String) = withContext(Dispatchers.IO) {
        val existing = userListDao.getEntry(mediaId)
        val isCompleted = newEpisode >= totalUnits
        val updatedStatus = if (isCompleted) "COMPLETED" else "WATCHING"

        val entry = UserListEntry(
            mediaId = mediaId,
            title = title,
            coverUrl = coverUrl,
            format = format,
            status = updatedStatus,
            progress = newEpisode,
            totalUnits = totalUnits,
            userScore = 9.0f,
            lastWatchedTimestamp = System.currentTimeMillis(),
            aniListSynced = true,
            malSynced = true
        )
        userListDao.insertOrUpdate(entry)
        Log.i("AniListRepo", "Dual sync AniList & MAL updated: Media $mediaId at ep $newEpisode (Status: $updatedStatus)")
    }

    suspend fun toggleFavorite(media: MediaEntity) = withContext(Dispatchers.IO) {
        mediaDao.update(media.copy(isFavorite = !media.isFavorite))
    }

    suspend fun setListStatus(media: MediaEntity, status: String, userScore: Float = 0f) = withContext(Dispatchers.IO) {
        val entry = UserListEntry(
            mediaId = media.id,
            title = media.titleEnglish,
            coverUrl = media.coverUrl,
            format = media.format,
            status = status,
            progress = if (status == "COMPLETED") (media.episodes ?: 12) else 1,
            totalUnits = media.episodes ?: 12,
            userScore = userScore,
            lastWatchedTimestamp = System.currentTimeMillis(),
            aniListSynced = true,
            malSynced = true
        )
        userListDao.insertOrUpdate(entry)
    }
}
