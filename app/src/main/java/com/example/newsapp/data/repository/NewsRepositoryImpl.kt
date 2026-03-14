package com.example.newsapp.data.repository

import com.example.newsapp.domain.model.ArticleHeadline
import com.example.newsapp.domain.repository.NewsRepository

class NewsRepositoryImpl : NewsRepository {
    override suspend fun getAllHeadlines(): Result<List<ArticleHeadline>> {
        TODO("Not yet implemented")
    }

}