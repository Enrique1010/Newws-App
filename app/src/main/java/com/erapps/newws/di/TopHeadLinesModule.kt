package com.erapps.newws.di

import com.erapps.newws.api.services.NewsApiService
import com.erapps.newws.data.source.news.NewsDataSource
import com.erapps.newws.data.source.remote.NewsRemoteDataSource
import com.erapps.newws.data.source.remote.TopHeadLinesRemoteDS
import com.erapps.newws.data.source.topheadlines.DefaultTopHeadLinesRepository
import com.erapps.newws.data.source.topheadlines.TopHeadLinesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TopHeadLinesModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RemoteTopHeadLinesDataSource

    @Singleton
    @RemoteTopHeadLinesDataSource
    @Provides
    fun providesTopHeadLinesRemoteDS(
        newsApiService: NewsApiService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): TopHeadLinesRemoteDS {
        return TopHeadLinesRemoteDS(newsApiService, ioDispatcher)
    }

    @Singleton
    @Provides
    fun providesTopHeadLinesRepository(
        @RemoteTopHeadLinesDataSource remoteTopHeadLinesDataSource: TopHeadLinesRemoteDS,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): TopHeadLinesRepository {
        return DefaultTopHeadLinesRepository(
            remoteTopHeadLinesDataSource,
            defaultDispatcher
        )
    }
}