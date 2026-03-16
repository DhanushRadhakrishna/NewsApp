package com.example.newsapp.domain.model

import com.example.newsapp.data.local.entity.FavoriteArticle

data class ArticleHeadline(
    val source: HeadlineSource,
    val title: String,
    val description: String?,
    val url: String,
    val publishedAt: String,
)

fun ArticleHeadline.toFavoriteArticle() : FavoriteArticle {
    return FavoriteArticle(
        url = url,
        title = title,
        description = description,
        publishedAt = publishedAt,
        sourceName = source.name
    )
}

