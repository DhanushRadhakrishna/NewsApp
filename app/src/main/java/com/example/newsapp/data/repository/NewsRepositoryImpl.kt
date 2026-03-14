package com.example.newsapp.data.repository

import com.example.newsapp.data.model.toDomain
import com.example.newsapp.data.network.api.NewsApi
import com.example.newsapp.data.network.utils.safeApiCall
import com.example.newsapp.domain.model.ArticleHeadline
import com.example.newsapp.domain.repository.NewsRepository
import javax.inject.Inject
import com.example.newsapp.domain.model.News
import com.example.newsapp.data.network.utils.Result


class NewsRepositoryImpl @Inject constructor(
    private val newsApiService : NewsApi
) : NewsRepository {
    override suspend fun getPagedHeadlines(page : Int): Result<News> {

        //the api call needs page number
        //since the responses do not include any page number

        return safeApiCall { newsApiService.getPagedHeadlines(page = page).toDomain() }
    }

}