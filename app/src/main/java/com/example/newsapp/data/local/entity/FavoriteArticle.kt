package com.example.newsapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_articles")
data class FavoriteArticle(
    @PrimaryKey
    val url: String,
    val title: String,
    val description: String?,
    val publishedAt: String,
    val sourceName: String
)
