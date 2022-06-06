package com.erapps.newws.room

import androidx.paging.PagingSource
import androidx.room.*
import com.erapps.newws.room.entities.Article

@Dao
interface FavsDao {
    @Query("select * from favs order by id desc")
    fun getFavs(): PagingSource<Int, Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavs(article: Article)

    @Delete
    suspend fun deleteFavArticle(article: Article)
}