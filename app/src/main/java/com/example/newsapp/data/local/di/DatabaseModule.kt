package com.example.newsapp.data.local.di

import android.content.Context
import androidx.room.Room
import com.example.newsapp.data.local.dao.FavoriteArticleDao
import com.example.newsapp.data.local.db.NewsArticlesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): NewsArticlesDatabase {
        return Room.databaseBuilder(
            context,
            NewsArticlesDatabase::class.java,
            "NewsArticles.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteArticleDao(
        database: NewsArticlesDatabase
    ) : FavoriteArticleDao {
        return database.favoriteArticleDao()
    }


}