package com.erapps.newws.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.erapps.newws.data.Converters
import com.erapps.newws.room.entities.CachedArticles
import com.erapps.newws.room.entities.Favs

@Database(
    entities = [CachedArticles::class, Favs::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ArticlesDatabase: RoomDatabase() {
    abstract fun articlesDao(): ArticlesDao
}