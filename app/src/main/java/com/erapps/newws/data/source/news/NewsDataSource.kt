package com.erapps.newws.data.source.news

import androidx.paging.PagingData
import com.erapps.newws.data.models.Article
import kotlinx.coroutines.flow.Flow

interface NewsDataSource {
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

    suspend fun insertUpdatedList(list: List<Article>)

    suspend fun clearDatabase()
}