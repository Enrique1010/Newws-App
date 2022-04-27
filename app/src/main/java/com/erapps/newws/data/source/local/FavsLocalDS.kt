package com.erapps.newws.data.source.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.erapps.newws.data.models.Article
import com.erapps.newws.data.source.favs.FavsDataSource
import com.erapps.newws.room.ArticlesDao
import com.erapps.newws.room.entities.Favs
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

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

    override suspend fun insertFavoriteArticle(article: Article) {
        articlesDao.insertFavs(Favs(article = article))
    }

}