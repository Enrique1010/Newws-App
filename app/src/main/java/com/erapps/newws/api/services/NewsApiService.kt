package com.erapps.newws.api.services

import com.erapps.newws.api.NetworkResponse
import com.erapps.newws.data.models.Article
import com.erapps.newws.data.models.ErrorModel
import com.erapps.newws.data.models.GetAllModel
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("everything")
    suspend fun getAllNews(
        @Query("sortBy") sortBy: String?,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
        @Query("language") language: String?,
        @Query("q") searchBy: String?
    ): NetworkResponse<GetAllModel<Article>, ErrorModel>

    @GET("top-headlines")
    suspend fun getTopHeadLines(
        @Query("country") country: String?,
        @Query("category") category: String?,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int
    ): NetworkResponse<GetAllModel<Article>, ErrorModel>
}