package com.erapps.newws.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.erapps.newws.api.services.NewsApiService
import com.erapps.newws.data.models.Article
import com.erapps.newws.data.source.topheadlines.TopHeadLinesPagingSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

interface ITopHeadLineDataSource {
    suspend fun getTopNews(): Flow<PagingData<Article>>

    suspend fun getTopNewByCategory(
        category: String
    ): Flow<PagingData<Article>>
}

class TopHeadLinesRemoteDS(
    private val newsApiService: NewsApiService,
    private val ioDispatcher: CoroutineDispatcher
): ITopHeadLineDataSource {

    override suspend fun getTopNews(): Flow<PagingData<Article>> {
        val pageSize = 30
        val maxSize = pageSize + (pageSize * 9)
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false,
                prefetchDistance = pageSize,
                maxSize = maxSize
            ),
            pagingSourceFactory = {
                TopHeadLinesPagingSource(newsApiService)
            }
        ).flow.flowOn(ioDispatcher)
    }

    override suspend fun getTopNewByCategory(
        category: String
    ): Flow<PagingData<Article>> {
        val pageSize = 30
        val maxSize = pageSize + (pageSize * 9)
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false,
                prefetchDistance = pageSize,
                maxSize = maxSize
            ),
            pagingSourceFactory = {
                TopHeadLinesPagingSource(newsApiService, category = category)
            }
        ).flow.flowOn(ioDispatcher)
    }
}