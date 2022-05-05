package com.erapps.newws.data.source.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.erapps.newws.data.models.Article
import com.erapps.newws.room.ArticlesDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

interface FavsDataSource{
    suspend fun getFavoritesArticles(): Flow<PagingData<Article>>
    suspend fun insertFavoriteArticle(article: Article)
    suspend fun deleteFavArticle(article: Article)
}

class FavsLocalDS(
    private val articlesDao: ArticlesDao,
    private val ioDispatcher: CoroutineDispatcher
): FavsDataSource {

    override suspend fun getFavoritesArticles(): Flow<PagingData<Article>> {
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
                articlesDao.getFavs()
            }
        ).flow.flowOn(ioDispatcher)
    }

    override suspend fun insertFavoriteArticle(article: Article) = withContext(ioDispatcher) {
        articlesDao.insertFavs(article = article)
    }

    override suspend fun deleteFavArticle(article: Article) = withContext(ioDispatcher) {
        articlesDao.deleteFavArticle(article)
    }

}