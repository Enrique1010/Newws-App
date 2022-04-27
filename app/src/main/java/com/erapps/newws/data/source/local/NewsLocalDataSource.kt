package com.erapps.newws.data.source.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.erapps.newws.data.models.Article
import com.erapps.newws.data.source.news.NewsDataSource
import com.erapps.newws.room.ArticlesDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class NewsLocalDataSource(
    private val articlesDao: ArticlesDao,
    private val ioDispatcher: CoroutineDispatcher
): NewsDataSource {
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
                articlesDao.getAll()
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
                articlesDao.getAll()
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
                articlesDao.getAll()
            }
        ).flow.flowOn(ioDispatcher)
    }

    override suspend fun getByUserQuery(
        searchBy: String
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
                articlesDao.getAll()
            }
        ).flow.flowOn(ioDispatcher)
    }
}