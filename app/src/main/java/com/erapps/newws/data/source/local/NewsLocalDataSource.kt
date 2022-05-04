package com.erapps.newws.data.source.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.erapps.newws.data.models.Article
import com.erapps.newws.room.ArticlesDao
import com.erapps.newws.room.entities.CachedArticles
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*

interface INewsLocalDataSource {
    suspend fun getAllNews(): Flow<PagingData<Article>>
    suspend fun getByUserQuery(searchBy: String): Flow<PagingData<Article>>
    suspend fun insertUpdatedList(list: List<Article>)
    suspend fun clearDatabase()
}

class NewsLocalDataSource(
    private val articlesDao: ArticlesDao,
    private val ioDispatcher: CoroutineDispatcher
): INewsLocalDataSource {
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

    override suspend fun insertUpdatedList(list: List<Article>) {
        articlesDao.insertUpdatedList(CachedArticles(
            articles = list
        ))
    }

    override suspend fun clearDatabase() {
        articlesDao.deleteArticles()
    }
}