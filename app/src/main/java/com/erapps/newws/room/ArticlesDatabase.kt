package com.erapps.newws.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.erapps.newws.data.models.Article
import com.erapps.newws.room.entities.RemoteKey

@Database(
    entities = [Article::class, com.erapps.newws.room.entities.Article::class, RemoteKey::class],
    version = 6,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ArticlesDatabase: RoomDatabase() {
    abstract fun articlesDao(): ArticlesDao
    abstract fun favsDao(): FavsDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}