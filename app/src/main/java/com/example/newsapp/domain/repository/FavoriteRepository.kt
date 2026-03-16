package com.example.newsapp.domain.repository

import com.example.newsapp.data.local.entity.FavoriteArticle
import com.example.newsapp.domain.model.ArticleHeadline
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    val allFavorites : Flow<List<FavoriteArticle>>

    suspend fun addFavorite(favoriteArticle : ArticleHeadline)

    fun isFavorite(url : String) : Flow<Boolean>

    suspend fun removeFavorite(article : FavoriteArticle)

}