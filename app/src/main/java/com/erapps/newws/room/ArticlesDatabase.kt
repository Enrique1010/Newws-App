package com.erapps.newws.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.erapps.newws.data.models.Article
import com.erapps.newws.room.entities.CachedArticles

@Database(
    entities = [CachedArticles::class, Article::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ArticlesDatabase: RoomDatabase() {
    abstract fun articlesDao(): ArticlesDao
}