package com.example.newsapp.presentation.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.usecase.NewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.newsapp.data.network.utils.Result
import com.example.newsapp.domain.model.ArticleHeadline
import com.example.newsapp.domain.model.toFavoriteArticle
import com.example.newsapp.domain.usecase.FavoritesUseCase
import com.example.newsapp.domain.usecase.SearchNewsUseCase
import com.example.newsapp.presentation.uistate.ContentDisplayState
import com.example.newsapp.presentation.uistate.DisplayState
import com.example.newsapp.presentation.uistate.TopHeadlinesScreenState
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

const val TAG = "MainViewModel"
@HiltViewModel
class MainViewModel @Inject constructor(
    private val newsUseCase : NewsUseCase,
    private val searchNewsUseCase: SearchNewsUseCase,
    private val firebaseAnalytics : FirebaseAnalytics,
    private val favoritesUseCase: FavoritesUseCase
)  : ViewModel(){

    private val _uiState = MutableStateFlow(TopHeadlinesScreenState(displayState = DisplayState.Loading))
    val uiState : StateFlow<TopHeadlinesScreenState> = _uiState.asStateFlow()

    private var page = 1
    private var reachedEndOfList = false

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery : StateFlow<String> = _searchQuery.asStateFlow()

    val favoriteUrls: StateFlow<Set<String>> = favoritesUseCase.allFavorites
        .map { favorites -> favorites.map { it.url }.toSet() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    init {
        getTopHeadlines()
        observeSearchQuery()
    }

    fun getTopHeadlines(){  //this function should only be called when page 0 is needed
        page = 1
        _searchQuery.value = ""
        viewModelScope.launch {
            _uiState.update { it.copy(displayState = DisplayState.Loading) }
            val apiResult = newsUseCase.invoke(page)
            when(apiResult){
                is Result.Success ->{
                    _uiState.update {

                            it.copy(DisplayState.Content(apiResult.data.articles))
                    }
                }
                is Result.Error ->{
                    _uiState.update {
                        it.copy(DisplayState.Error(apiResult.message))
                    }
                }
            }
        }
        firebaseAnalytics.logEvent("Refresh",null)
    }

    fun onArticleClick(url : String)
    {
        firebaseAnalytics.logEvent("ArticleClick",Bundle().apply {
            putString("article_url", url)
        })
    }

    fun paginate()
    {
        val currDisplayType  = _uiState.value.displayState
        if(currDisplayType is DisplayState.Content)
        {
            val currArticleList = currDisplayType.articleHeadlines
            _uiState.update {
                it.copy(
                    displayState = currDisplayType.copy(
                        contentDisplayState = ContentDisplayState.Loading
                    )
                )
            }
            if(!reachedEndOfList) // get the next page only if the end of list is not reached
            {
                getNextPageData(currArticleList)
            }
            else{
                pagingError("Could not load more.")
            }

        }
    }
    fun getNextPageData(currArticleHeadlines : List<ArticleHeadline>)
    {
        page++
        Log.i(TAG,"Page = $page")
        viewModelScope.launch {
                val apiResult = newsUseCase.invoke(page)
                when(apiResult){
                    is Result.Success ->{
                        val newArticlesPage = apiResult.data.articles
                        if(newArticlesPage.isEmpty())
                        {
                            pagingError("Could not load more.")
                            reachedEndOfList = true
                        }
                        else{
                            _uiState.update {
                                it.copy(displayState = DisplayState.Content(currArticleHeadlines+apiResult.data.articles))
                            }
                        }
                        Log.i(TAG, "Response = ${apiResult.data.articles}")
                    }
                    is Result.Error ->{
                        val currDisplayType = _uiState.value.displayState
                        if(currDisplayType is DisplayState.Content)
                        {
                            _uiState.update {
                                it.copy(
                                    displayState = currDisplayType.copy(
                                        contentDisplayState = ContentDisplayState.PaginationError(apiResult.message)
                                    )
                                )
                            }
                            Log.i(TAG, "Response = ${apiResult.message}")
                        }
                }
            }
        }
        firebaseAnalytics.logEvent("Page",Bundle().apply {
            putInt("page_number_request", page)
        })
    }
    fun pagingError(message: String) {
        val currDisplayType = _uiState.value.displayState
        if (currDisplayType is DisplayState.Content) {
            _uiState.update {
                it.copy(
                    displayState = currDisplayType.copy(
                        contentDisplayState = ContentDisplayState.PaginationError(message)
                    )
                )
            }
            firebaseAnalytics.logEvent("EndOfPages",Bundle().apply {
                putInt("page_number_request", page)
            })
        }
    }

    @OptIn(FlowPreview::class)
    fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(500)   // wait 500ms after user stops typing
                .filter { it.isNotBlank() }
                .distinctUntilChanged() //filters all same values that are collected
                .map { it.trim() }
                .collect { query ->
                    _uiState.update { it.copy(displayState = DisplayState.Loading) }
                    val apiResult = searchNewsUseCase.invoke(query)
                    when (apiResult) {
                        is Result.Success -> _uiState.update { it.copy(DisplayState.Content(apiResult.data.articles)) }
                        is Result.Error -> _uiState.update { it.copy(DisplayState.Error(apiResult.message)) }
                    }
                    Log.i(TAG, query)
                }
        }
        firebaseAnalytics.logEvent("SearchRequest",Bundle().apply {
            putString("search_query", _searchQuery.value)
        })
    }

    fun onSearchQueryChange(query : String)
    {
        _searchQuery.value = query
        //if the search query is empty, we need to get back to the top headlines
        if(_searchQuery.value.trim().isEmpty())
        {
            getTopHeadlines()
        }
    }

    fun onAddFavorite(article : ArticleHeadline)
    {
        viewModelScope.launch {
            if(favoritesUseCase.isFavorite(article.url).first()){
                favoritesUseCase.removeFavorite(article.toFavoriteArticle())
            }
            else{
                favoritesUseCase.addFavorite(article)
            }
        }
        firebaseAnalytics.logEvent("AddedFavorite",Bundle().apply { putString("favorite_article",article.url) })
    }

}
