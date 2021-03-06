package com.erapps.newws.data.source.favs

import androidx.paging.PagingData
import com.erapps.newws.data.models.Article
import com.erapps.newws.data.source.local.FavsLocalDS
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class FavsDefaultRepository(
    private val favsLocalDS: FavsLocalDS,
    private val defaultDispatcher: CoroutineDispatcher
): FavsRepository {
    override suspend fun getFavoritesArticles(): Flow<PagingData<Article>> {
        return favsLocalDS.getFavoritesArticles().flowOn(defaultDispatcher)
    }

    override suspend fun insertFavoriteArticle(article: Article) = withContext(defaultDispatcher) {
            favsLocalDS.insertFavoriteArticle(article)
    }

    override suspend fun deleteFavArticle(article: Article) = withContext(defaultDispatcher) {
        favsLocalDS.deleteFavArticle(article)
    }
}