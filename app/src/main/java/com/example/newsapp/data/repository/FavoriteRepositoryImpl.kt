package com.example.newsapp.data.repository

import com.example.newsapp.data.local.room.dao.FavoriteArticleDao
import com.example.newsapp.data.local.room.entity.FavoriteArticle
import com.example.newsapp.domain.model.ArticleHeadline
import com.example.newsapp.domain.model.toFavoriteArticle
import com.example.newsapp.domain.repository.FavoriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteArticleDao: FavoriteArticleDao
): FavoriteRepository {

    override val allFavorites: Flow<List<FavoriteArticle>> = favoriteArticleDao.getAllFavorites().flowOn(
        Dispatchers.IO)

    override suspend fun addFavorite(favoriteArticle: ArticleHeadline) {
        withContext(Dispatchers.IO){
            favoriteArticleDao.insertFavorite(favoriteArticle.toFavoriteArticle())
        }
    }

    override fun isFavorite(url : String): Flow<Boolean> =
        favoriteArticleDao.isFavorite(url).flowOn(Dispatchers.IO)

    override suspend fun removeFavorite(article: FavoriteArticle) {
        withContext(Dispatchers.IO)
        {
            favoriteArticleDao.deleteFavorite(article)
        }
    }

}