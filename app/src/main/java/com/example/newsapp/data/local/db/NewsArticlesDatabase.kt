package com.example.newsapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapp.data.local.dao.FavoriteArticleDao
import com.example.newsapp.data.local.entity.FavoriteArticle


@Database(
    entities = [FavoriteArticle::class],
    version = 1,
    exportSchema = false
    )
abstract class NewsArticlesDatabase : RoomDatabase() {
    abstract fun favoriteArticleDao() : FavoriteArticleDao
}