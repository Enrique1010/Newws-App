package com.erapps.newws.di

import com.erapps.newws.api.services.NewsApiService
import com.erapps.newws.data.source.local.INewsLocalDataSource
import com.erapps.newws.data.source.news.RemoteNewsRepository
import com.erapps.newws.data.source.local.NewsLocalDataSource
import com.erapps.newws.data.source.news.ILocalNewsRepository
import com.erapps.newws.data.source.news.INewsRepository
import com.erapps.newws.data.source.news.LocalNewsRepository
import com.erapps.newws.data.source.remote.INewsDataSource
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
    ): INewsDataSource {
        return NewsRemoteDataSource(newsApiService, ioDispatcher)
    }

    @Singleton
    @LocalNewsDataSource
    @Provides
    fun provideNewsLocalDataSource(
        articlesDao: ArticlesDao,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): INewsLocalDataSource {
        return NewsLocalDataSource(articlesDao, ioDispatcher)
    }

    @Singleton
    @Provides
    fun providesRemoteNewsRepository(
        @RemoteNewsDataSource remoteNewsDataSource: INewsDataSource,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): INewsRepository {
        return RemoteNewsRepository(
            remoteNewsDataSource,
            defaultDispatcher
        )
    }

    @Singleton
    @Provides
    fun providesLocalNewsRepository(
        @LocalNewsDataSource localNewsDataSource: INewsLocalDataSource,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): ILocalNewsRepository {
        return LocalNewsRepository(
            localNewsDataSource,
            defaultDispatcher
        )
    }

}