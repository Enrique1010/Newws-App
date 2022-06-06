package com.erapps.newws.room

import androidx.paging.PagingSource
import androidx.room.*
import com.erapps.newws.data.models.Article

@Dao
interface ArticlesDao {
    @Query("select * from ARTICLES")
    fun getAll(): PagingSource<Int, Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdatedList(articles: List<Article>)

    @Query("delete from articles")
    suspend fun deleteArticles()
}