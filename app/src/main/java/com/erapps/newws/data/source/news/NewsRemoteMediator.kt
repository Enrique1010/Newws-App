package com.erapps.newws.data.source.news

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.erapps.newws.api.NetworkResponse
import com.erapps.newws.api.services.NewsApiService
import com.erapps.newws.data.models.Article
import com.erapps.newws.room.ArticlesDatabase
import com.erapps.newws.room.entities.RemoteKey
import okio.IOException
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

const val STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val newsApiService: NewsApiService,
    private val db: ArticlesDatabase,
    private val sortBy: String? = "popularity",
    private val language: String? = "en",
    private val searchBy: String? = "the"
): RemoteMediator<Int, Article>() {
    private val newsDao = db.articlesDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Article>
    ): MediatorResult {
        return try {
            val page = when(loadType) {
                LoadType.REFRESH -> {
                    1
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(true)
                }
                LoadType.APPEND -> {
                    val remoteKey = db.withTransaction {
                        db.remoteKeysDao().remoteKeyArticle(1)
                    } ?: return MediatorResult.Success(true)

                    if(remoteKey.nextKey == null) {
                        return MediatorResult.Success(true)
                    }

                    remoteKey.nextKey
                }
            }
            val response = newsApiService.getAllNews(
                page = page,
                pageSize = state.config.pageSize,
                sortBy = sortBy,
                language = language,
                searchBy = searchBy
            )

            val articles = response.let {
                when(it){
                    is NetworkResponse.ApiError -> emptyList()
                    is NetworkResponse.NetworkError -> emptyList()
                    is NetworkResponse.Success -> it.body!!.data
                    is NetworkResponse.UnknownError -> emptyList()
                }
            }

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    newsDao.deleteArticles()
                    //db.remoteKeysDao().deleteAll()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (articles.isEmpty()) null else page + 1
                val keys = articles.map {
                    RemoteKey(1, prevKey = prevKey, nextKey = nextKey, lastUpdated = System.currentTimeMillis())
                }
                db.remoteKeysDao().insertAll(keys)
                newsDao.insertUpdatedList(articles)
            }

            MediatorResult.Success(endOfPaginationReached = articles.isEmpty())
        }catch (e: IOException){
            MediatorResult.Error(e)
        }catch (e: HttpException){
            MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        val remoteKey = db.withTransaction {
            db.remoteKeysDao().remoteKeyArticle(1)
        } ?: return InitializeAction.LAUNCH_INITIAL_REFRESH

        val cacheTimeout = TimeUnit.HOURS.convert(1, TimeUnit.MILLISECONDS)

        return if((System.currentTimeMillis() - remoteKey.lastUpdated!!) >= cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }
}