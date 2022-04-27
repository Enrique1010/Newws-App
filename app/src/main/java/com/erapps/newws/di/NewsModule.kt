package com.erapps.newws.di

import com.erapps.newws.api.services.NewsApiService
import com.erapps.newws.data.source.news.DefaultNewsRepository
import com.erapps.newws.data.source.news.NewsDataSource
import com.erapps.newws.data.source.news.NewsRepository
import com.erapps.newws.data.source.local.NewsLocalDataSource
import com.erapps.newws.data.source.remote.NewsRemoteDataSource
import com.erapps.newws.room.ArticlesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RemoteNewsDataSource

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LocalNewsDataSource

    @Singleton
    @RemoteNewsDataSource
    @Provides
    fun provideNewsRemoteDataSource(
        newsApiService: NewsApiService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): NewsDataSource {
        return NewsRemoteDataSource(newsApiService, ioDispatcher)
    }

    @Singleton
    @LocalNewsDataSource
    @Provides
    fun provideNewsLocalDataSource(
        articlesDao: ArticlesDao,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): NewsDataSource {
        return NewsLocalDataSource(articlesDao, ioDispatcher)
    }

    @Singleton
    @Provides
    fun providesNewsRepository(
        @RemoteNewsDataSource remoteNewsDataSource: NewsDataSource,
        @LocalNewsDataSource localNewsDataSource: NewsDataSource,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): NewsRepository {
        return DefaultNewsRepository(
            remoteNewsDataSource,
            localNewsDataSource,
            defaultDispatcher
        )
    }

}