package com.example.newsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.data.local.entity.FavoriteArticle
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoriteArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(article: FavoriteArticle)

    @Delete
    suspend fun deleteFavorite(article: FavoriteArticle)

    @Query("SELECT * FROM favorite_articles")
    fun getAllFavorites(): Flow<List<FavoriteArticle>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_articles WHERE url = :url)")
    fun isFavorite(url: String): Flow<Boolean>
}