package com.erapps.newws.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.erapps.newws.data.models.Article

@Entity
data class CachedArticles(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 1,
    @ColumnInfo(name = "articles")
    val articles: List<Article>
)
