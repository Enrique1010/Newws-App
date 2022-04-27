package com.erapps.newws.data.source.topheadlines

import androidx.paging.PagingData
import com.erapps.newws.data.models.Article
import kotlinx.coroutines.flow.Flow

interface TopHeadLinesRepository {
    suspend fun getTopNews(): Flow<PagingData<Article>>

    suspend fun getTopNewsByCountry(
        country: String
    ): Flow<PagingData<Article>>

    suspend fun getTopNewsByCategory(
        category: String
    ): Flow<PagingData<Article>>
}