package com.example.newsapp.data.model

import com.example.newsapp.domain.model.News

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)

fun NewsResponse.toDomain(): News {
    return News(
        status = status,
        totalResults = totalResults,
        articles = articles.map { it.toDomain() }
    )
}
