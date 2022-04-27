package com.erapps.newws.data.source.news

import androidx.paging.PagingData
import com.erapps.newws.data.models.Article
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class DefaultNewsRepository(
    private val newsRemoteDataSource: NewsDataSource,
    private val newsLocalDataSource: NewsDataSource,
    private val defaultDispatcher: CoroutineDispatcher
): NewsRepository {
    override suspend fun getAllNews(): Flow<PagingData<Article>> {
        return newsRemoteDataSource.getAllNews().flowOn(defaultDispatcher)
    }

    override suspend fun getFilteredNews(
        sortBy: String
    ): Flow<PagingData<Article>> {
        return newsRemoteDataSource.getFilteredNews(sortBy).flowOn(defaultDispatcher)
    }

    override suspend fun getNewsByLanguage(
        language: String
    ): Flow<PagingData<Article>> {
        return newsRemoteDataSource.getNewsByLanguage(language).flowOn(defaultDispatcher)
    }

    override suspend fun getByUserQuery(
        searchBy: String
    ): Flow<PagingData<Article>> {
        return newsRemoteDataSource.getByUserQuery(searchBy).flowOn(defaultDispatcher)
    }

}