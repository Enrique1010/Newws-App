package com.erapps.newws.room

import androidx.paging.PagingSource
import androidx.room.*
import com.erapps.newws.data.models.Article
import com.erapps.newws.room.entities.CachedArticles

@Dao
interface ArticlesDao {
    @Query("select articles from CachedArticles where id = 1")
    fun getAll(): PagingSource<Int, Article>

    @Query("select * from Article")
    fun getFavs(): PagingSource<Int, Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdatedList(cachedArticles: CachedArticles)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavs(article: Article)

    @Query("delete from CachedArticles")
    suspend fun deleteArticles()
}