package com.erapps.newws.data.source.topheadlines

import androidx.paging.PagingData
import com.erapps.newws.data.models.Article
import com.erapps.newws.data.source.remote.TopHeadLinesRemoteDS
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class DefaultTopHeadLinesRepository(
    private val remoteDS: TopHeadLinesRemoteDS,
    private val defaultDispatcher: CoroutineDispatcher
): TopHeadLinesRepository {
    override suspend fun getTopNews(): Flow<PagingData<Article>> {
        return remoteDS.getTopNews().flowOn(defaultDispatcher)
    }

    override suspend fun getTopNewsByCountry(
        country: String
    ): Flow<PagingData<Article>> {
        return remoteDS.getTopNewsByCountry(country).flowOn(defaultDispatcher)
    }

    override suspend fun getTopNewsByCategory(
        category: String
    ): Flow<PagingData<Article>> {
        return remoteDS.getTopNewByCategory(category).flowOn(defaultDispatcher)
    }
}