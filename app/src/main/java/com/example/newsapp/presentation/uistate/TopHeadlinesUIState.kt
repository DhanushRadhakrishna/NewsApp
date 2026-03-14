package com.example.newsapp.presentation.uistate

import com.example.newsapp.domain.model.ArticleHeadline

sealed class TopHeadlinesUIState{
    object Loading : TopHeadlinesUIState()
    data class Success(val articlesHeadline : List<ArticleHeadline>) : TopHeadlinesUIState()
    data class Error(val message : String) : TopHeadlinesUIState()
}