package com.erapps.newws.data.source.news

import androidx.paging.PagingData
import com.erapps.newws.data.models.Article
import com.erapps.newws.data.source.remote.INewsDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

interface INewsRepository {
    suspend fun getAllNews(): Flow<PagingData<Article>>

    suspend fun getByUserQuery(
        searchBy: String
    ): Flow<PagingData<Article>>
}

class RemoteNewsRepository(
    private val dataSource: INewsDataSource,
    private val defaultDispatcher: CoroutineDispatcher
): INewsRepository {
    override suspend fun getAllNews(): Flow<PagingData<Article>> {
        return dataSource.getAllNews().flowOn(defaultDispatcher)
    }

    override suspend fun getByUserQuery(
        searchBy: String
    ): Flow<PagingData<Article>> {
        return dataSource.getByUserQuery(searchBy).flowOn(defaultDispatcher)
    }

}