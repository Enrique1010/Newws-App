package com.erapps.newws.room.entities

import androidx.room.*
import com.erapps.newws.data.Converters
import com.erapps.newws.data.models.Article
import kotlin.random.Random

@Entity
data class Favs(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = Random(100000).nextInt(),
    @ColumnInfo(name = "article")
    val article: Article?
)
