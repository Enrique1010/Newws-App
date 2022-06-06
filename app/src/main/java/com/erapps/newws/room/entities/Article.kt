package com.erapps.newws.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.erapps.newws.data.models.Source
import com.erapps.newws.utils.Exclude
import java.io.Serializable

@Entity(tableName = "favs")
data class Article (
    @Exclude(deserialize = false)
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "source")
    val source      : Source? = null,
    @ColumnInfo(name = "author")
    val author      : String? = null,
    @ColumnInfo(name = "title")
    val title       : String? = null,
    @ColumnInfo(name = "description")
    val description : String? = null,
    @ColumnInfo(name = "url")
    val url         : String? = null,
    @ColumnInfo(name = "urlToImage")
    val urlToImage  : String? = null,
    @ColumnInfo(name = "publishedAt")
    val publishedAt : String? = null,
    @ColumnInfo(name = "content")
    val content     : String? = null
): Serializable
