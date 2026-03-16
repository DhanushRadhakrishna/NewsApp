package com.example.newsapp.domain.usecase

import com.example.newsapp.data.local.entity.FavoriteArticle
import com.example.newsapp.domain.model.ArticleHeadline
import com.example.newsapp.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritesUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {

    val allFavorites: Flow<List<FavoriteArticle>> = favoriteRepository.allFavorites

    suspend fun addFavorite(article: ArticleHeadline) {
        favoriteRepository.addFavorite(article)
    }

    suspend fun removeFavorite(article: FavoriteArticle) {
        favoriteRepository.removeFavorite(article)
    }

    fun isFavorite(url: String): Flow<Boolean> {
        return favoriteRepository.isFavorite(url)
    }

}
