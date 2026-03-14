package com.example.newsapp.domain.model

data class ArticleHeadline(
    val source: HeadlineSource,
    val title: String,
    val description: String?,
    val url: String,
    val publishedAt: String,
)
