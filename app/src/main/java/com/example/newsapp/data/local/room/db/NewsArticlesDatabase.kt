package com.example.newsapp.data.local.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapp.data.local.room.dao.FavoriteArticleDao
import com.example.newsapp.data.local.room.entity.FavoriteArticle


@Database(
    entities = [FavoriteArticle::class],
    version = 1,
    exportSchema = false
    )
abstract class NewsArticlesDatabase : RoomDatabase() {
    abstract fun favoriteArticleDao() : FavoriteArticleDao
}