package com.erapps.newws.data.source.news

import androidx.paging.PagingData
import com.erapps.newws.data.models.Article
import com.erapps.newws.data.source.local.INewsLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

interface ILocalNewsRepository {
    suspend fun getAllNews(): Flow<PagingData<Article>>
    suspend fun getByUserQuery(searchBy: String): Flow<PagingData<Article>>
    suspend fun insertUpdatedList(list: List<Article>)
    suspend fun clearDatabase()
}

class LocalNewsRepository(
    private val localDataSource: INewsLocalDataSource,
    private val defaultDispatcher: CoroutineDispatcher
): ILocalNewsRepository {
    override suspend fun getAllNews(): Flow<PagingData<Article>> {
        return localDataSource.getAllNews().flowOn(defaultDispatcher)
    }

    override suspend fun getByUserQuery(searchBy: String): Flow<PagingData<Article>> {
        return localDataSource.getByUserQuery(searchBy).flowOn(defaultDispatcher)
    }

    override suspend fun insertUpdatedList(list: List<Article>) = withContext(defaultDispatcher){
        localDataSource.insertUpdatedList(list)
    }

    override suspend fun clearDatabase() = withContext(defaultDispatcher){
            localDataSource.clearDatabase()
    }
}