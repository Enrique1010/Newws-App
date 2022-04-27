package com.erapps.newws.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.erapps.newws.data.models.Article
import com.erapps.newws.room.entities.CachedArticles
import com.erapps.newws.room.entities.Favs

@Dao
interface ArticlesDao {
    @Query("select articles from CachedArticles where id = 1")
    fun getAll(): PagingSource<Int, Article>

    @Query("select article from Favs")
    fun getFavs(): PagingSource<Int, Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdatedList(cachedArticles: CachedArticles)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavs(article: Favs)
}