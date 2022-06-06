package com.erapps.newws.room

import androidx.room.*
import com.erapps.newws.room.entities.RemoteKey

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKey>)

    @Query("SELECT lastUpdated from remote_keys")
    suspend fun getLastUpdated(): Long?

    @Query("SELECT * FROM remote_keys WHERE articleId = :id")
    suspend fun remoteKeyArticle(id: Int): RemoteKey?

    @Query("Delete from remote_keys")
    suspend fun deleteAll()
}