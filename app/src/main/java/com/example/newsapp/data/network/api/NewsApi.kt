package com.example.newsapp.data.network.api

import com.example.newsapp.data.model.Article
import com.example.newsapp.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

//base_url = https://newsapi.org/v2/
interface NewsApi {

    @GET("top-headlines")
    suspend fun getHeadLines(
        @Query("country") country : String = "us",

        ) : NewsResponse

    @GET("top-headlines")
    suspend fun getPagedHeadlines(
        @Query("country") country : String ="us",
        @Query("pageSize") pageSize : Int = 10,
        @Query("page") page : Int
    ) : NewsResponse

    @GET("everything")
    suspend fun getSearchedNews(
        @Query("q") query : String
    ) : NewsResponse

}