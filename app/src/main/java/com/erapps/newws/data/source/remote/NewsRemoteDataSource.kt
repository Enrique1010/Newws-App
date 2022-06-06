package com.erapps.newws.data.source.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.erapps.newws.api.services.NewsApiService
import com.erapps.newws.data.models.Article
import com.erapps.newws.data.source.news.NewsPagingSource
import com.erapps.newws.data.source.news.NewsRemoteMediator
import com.erapps.newws.room.ArticlesDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*

interface INewsDataSource {
    suspend fun getAllNews(): Flow<PagingData<Article>>

    suspend fun getFilteredNews(
        sortBy: String
    ): Flow<PagingData<Article>>

    suspend fun getNewsByLanguage(
        language: String
    ): Flow<PagingData<Article>>

    suspend fun getByUserQuery(
        searchBy: String
    ): Flow<PagingData<Article>>
}

class NewsRemoteDataSource(
    private val newsApiService: NewsApiService,
    private val ioDispatcher: CoroutineDispatcher,
    private val database: ArticlesDatabase
): INewsDataSource {

    override suspend fun getAllNews(): Flow<PagingData<Article>> {
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
                NewsPagingSource(newsApiService)
            }
        ).flow.flowOn(ioDispatcher)
    }

    override suspend fun getFilteredNews(
        sortBy: String
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
                NewsPagingSource(newsApiService, sortBy = sortBy)
            }
        ).flow.flowOn(ioDispatcher)
    }

    override suspend fun getNewsByLanguage(
        language: String
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
                NewsPagingSource(newsApiService, language = language)
            }
        ).flow.flowOn(ioDispatcher)
    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getByUserQuery(
        searchBy: String
    ): Flow<PagingData<Article>> {
        val pageSize = 30
        val maxSize = pageSize + (pageSize * 2)
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false,
                prefetchDistance = pageSize,
                maxSize = maxSize
            ),
            pagingSourceFactory = { database.articlesDao().getAll() },
            remoteMediator = NewsRemoteMediator(
                newsApiService = newsApiService,
                db = database,
                searchBy = searchBy
            )
        ).flow.flowOn(ioDispatcher)
    }
}