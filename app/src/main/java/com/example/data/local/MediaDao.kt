package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MediaEntity
import com.example.data.model.UserListEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {
    @Query("SELECT * FROM media_entries ORDER BY aniListScore DESC")
    fun getAllMedia(): Flow<List<MediaEntity>>

    @Query("SELECT * FROM media_entries WHERE id = :id")
    fun getMediaById(id: Int): Flow<MediaEntity?>

    @Query("SELECT * FROM media_entries WHERE titleEnglish LIKE '%' || :query || '%' OR titleRomaji LIKE '%' || :query || '%'")
    fun searchLocal(query: String): Flow<List<MediaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mediaList: List<MediaEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(media: MediaEntity)

    @Update
    suspend fun update(media: MediaEntity)

    @Query("SELECT * FROM media_entries WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<MediaEntity>>
}

@Dao
interface UserListDao {
    @Query("SELECT * FROM user_lists ORDER BY lastWatchedTimestamp DESC")
    fun getAllEntries(): Flow<List<UserListEntry>>

    @Query("SELECT * FROM user_lists WHERE status = :status ORDER BY lastWatchedTimestamp DESC")
    fun getEntriesByStatus(status: String): Flow<List<UserListEntry>>

    @Query("SELECT * FROM user_lists WHERE mediaId = :mediaId")
    fun getEntry(mediaId: Int): Flow<UserListEntry?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entry: UserListEntry)

    @Query("UPDATE user_lists SET progress = :progress, lastWatchedTimestamp = :timestamp WHERE mediaId = :mediaId")
    suspend fun updateProgress(mediaId: Int, progress: Int, timestamp: Long = System.currentTimeMillis())

    @Query("DELETE FROM user_lists WHERE mediaId = :mediaId")
    suspend fun delete(mediaId: Int)
}
