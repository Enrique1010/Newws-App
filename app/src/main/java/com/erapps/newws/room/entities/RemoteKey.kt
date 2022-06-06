package com.erapps.newws.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey val articleId: Int,
    val prevKey: Int?,
    val nextKey: Int?,
    val lastUpdated: Long?
)
