package com.example.newsapp.presentation.uistate

import com.example.newsapp.domain.model.ArticleHeadline



data class TopHeadlinesScreenState(
    val displayState : DisplayState? = null
)

sealed interface DisplayState{
    data object Loading : DisplayState
    data class Error(
        val message : String
    ) : DisplayState
    data class Content(
        val articleHeadlines : List<ArticleHeadline>,
        val contentDisplayState : ContentDisplayState? =null
    ) : DisplayState
}

sealed interface ContentDisplayState{
    data object Loading : ContentDisplayState
    data class PaginationError(
        val message : String
    ) : ContentDisplayState
}
/*

    problem 1: paginate() triggers even if I go scroll up and scroll down



 */

