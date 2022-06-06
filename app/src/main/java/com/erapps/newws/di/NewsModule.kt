package com.erapps.newws.di

import com.erapps.newws.api.services.NewsApiService
import com.erapps.newws.data.source.news.RemoteNewsRepository
import com.erapps.newws.data.source.news.INewsRepository
import com.erapps.newws.data.source.remote.INewsDataSource
import com.erapps.newws.data.source.remote.NewsRemoteDataSource
import com.erapps.newws.room.ArticlesDatabase
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

    @Singleton
    @RemoteNewsDataSource
    @Provides
    fun provideNewsRemoteDataSource(
        newsApiService: NewsApiService,
        database: ArticlesDatabase,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): INewsDataSource {
        return NewsRemoteDataSource(newsApiService, ioDispatcher, database)
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

}