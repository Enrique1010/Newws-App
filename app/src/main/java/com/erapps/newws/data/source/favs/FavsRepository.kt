package com.erapps.newws.data.source.favs

import androidx.paging.PagingData
import com.erapps.newws.data.models.Article
import kotlinx.coroutines.flow.Flow

interface FavsRepository {
    suspend fun getFavoritesArticles(): Flow<PagingData<Article>>
    suspend fun insertFavoriteArticle(article: Article)
    suspend fun deleteFavArticle(article: Article)
}