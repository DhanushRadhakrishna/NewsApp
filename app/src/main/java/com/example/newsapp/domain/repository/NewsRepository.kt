package com.example.newsapp.domain.repository

import com.example.newsapp.domain.model.ArticleHeadline
import com.example.newsapp.domain.model.News
import com.example.newsapp.data.network.utils.Result

interface NewsRepository {


    suspend fun getPagedHeadlines(page : Int) : Result<News>

    //suspend fun getFavoriteNewsArticles()
}