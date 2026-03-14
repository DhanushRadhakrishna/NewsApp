package com.example.newsapp.data.model

import com.example.newsapp.domain.model.ArticleHeadline

data class Article(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)

fun Article.toDomain(): ArticleHeadline {
    return ArticleHeadline(
        source = source.toDomain(),
        title = title,
        description = description,
        url = url,
        publishedAt = publishedAt
    )
}
