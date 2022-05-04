package com.erapps.newws.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.erapps.newws.utils.Exclude
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Article (
    @Exclude(deserialize = false)
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "source")
    @SerializedName("source")
    val source      : Source,
    @ColumnInfo(name = "author")
    @SerializedName("author")
    val author      : String,
    @ColumnInfo(name = "title")
    @SerializedName("title")
    val title       : String,
    @ColumnInfo(name = "description")
    @SerializedName("description")
    val description : String,
    @ColumnInfo(name = "url")
    @SerializedName("url")
    val url         : String,
    @ColumnInfo(name = "urlToImage")
    @SerializedName("urlToImage")
    val urlToImage  : String,
    @ColumnInfo(name = "publishedAt")
    @SerializedName("publishedAt")
    val publishedAt : String,
    @ColumnInfo(name = "content")
    @SerializedName("content")
    val content     : String
): Serializable
