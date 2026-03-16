package com.example.newsapp.domain.usecase

import com.example.newsapp.domain.model.ArticleHeadline
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor() {
    suspend operator fun invoke(favoriteArticle : ArticleHeadline) {
        //call favoriterepository add function
    }
}