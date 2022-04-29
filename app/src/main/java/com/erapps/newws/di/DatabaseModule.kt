package com.erapps.newws.di

import android.content.Context
import androidx.databinding.adapters.Converters
import androidx.room.Room
import com.erapps.newws.room.ArticlesDao
import com.erapps.newws.room.ArticlesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideArticlesDao(articlesDatabase: ArticlesDatabase): ArticlesDao{
        return articlesDatabase.articlesDao()
    }

    @Singleton
    @Provides
    fun provideArticlesDatabase(@ApplicationContext appContext: Context): ArticlesDatabase {
        return Room.databaseBuilder(
            appContext,
            ArticlesDatabase::class.java,
            "Articles_DB"
        ).fallbackToDestructiveMigration().build()
    }
}