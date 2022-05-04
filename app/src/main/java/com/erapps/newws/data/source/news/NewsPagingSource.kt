package com.erapps.newws.data.source.news

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.erapps.newws.api.NetworkResponse
import com.erapps.newws.api.services.NewsApiService
import com.erapps.newws.data.models.Article
import retrofit2.HttpException
import java.io.IOException

private const val API_STARTING_PAGE_INDEX = 1

class NewsPagingSource(
    private val newsApiService: NewsApiService,
    private val sortBy: String? = "popularity",
    private val language: String? = "en",
    private val searchBy: String? = "the"
): PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: API_STARTING_PAGE_INDEX
        return try {
            val response = newsApiService.getAllNews(
                page = position,
                pageSize = params.loadSize,
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

            val nextKey = if (articles.isEmpty()) {
                null
            } else {
                position + (params.loadSize / 30)
            }

            LoadResult.Page(
                data = articles,
                prevKey = if (position == API_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        }catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}