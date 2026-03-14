package com.example.newsapp.domain.repository

import com.example.newsapp.domain.model.ArticleHeadline

interface NewsRepository {


    suspend fun getAllHeadlines() : Result<List<ArticleHeadline>>

    //suspend fun getFavoriteNewsArticles()
}