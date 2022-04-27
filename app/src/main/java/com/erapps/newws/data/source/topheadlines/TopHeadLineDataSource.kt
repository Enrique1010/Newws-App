package com.erapps.newws.data.source.topheadlines

import androidx.paging.PagingData
import com.erapps.newws.data.Result
import com.erapps.newws.data.models.Article
import com.erapps.newws.data.models.ErrorModel
import kotlinx.coroutines.flow.Flow

interface TopHeadLineDataSource {
    suspend fun getTopNews(): Flow<PagingData<Article>>

    suspend fun getTopNewsByCountry(
        country: String
    ): Flow<PagingData<Article>>

    suspend fun getTopNewByCategory(
        category: String
    ): Flow<PagingData<Article>>
}