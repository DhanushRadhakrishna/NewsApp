package com.example.newsapp.presentation.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.model.Article
import com.example.newsapp.domain.usecase.NewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.newsapp.data.network.utils.Result
import com.example.newsapp.data.network.utils.dummyArticles
import com.example.newsapp.data.network.utils.dummyNews
import com.example.newsapp.domain.model.ArticleHeadline
import com.example.newsapp.domain.usecase.SearchNewsUseCase
import com.example.newsapp.presentation.screens.TopHeadlines
import com.example.newsapp.presentation.uistate.ContentDisplayState
import com.example.newsapp.presentation.uistate.DisplayState
import com.example.newsapp.presentation.uistate.TopHeadlinesScreenState
import com.example.newsapp.presentation.userpreferences.UserPreferencesRepository
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

const val TAG = "MainViewModel"
@HiltViewModel
class MainViewModel @Inject constructor(
    private val newsUseCase : NewsUseCase,
    private val searchNewsUseCase: SearchNewsUseCase,
    private val firebaseAnalytics : FirebaseAnalytics,
)  : ViewModel(){

    private val _uiState = MutableStateFlow(TopHeadlinesScreenState(displayState = DisplayState.Loading))
    val uiState : StateFlow<TopHeadlinesScreenState> = _uiState.asStateFlow()

    private var page = 1
    private var reachedEndOfList = false

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery : StateFlow<String> = _searchQuery.asStateFlow()

    init {
        getTopHeadlines()
        observeSearchQuery()
    }

    fun getTopHeadlines(){  //this function should only be called when the activity is created
        page = 1
        viewModelScope.launch {
            _uiState.update { it.copy(displayState = DisplayState.Loading)
            }
//            val apiResult = newsUseCase.invoke(page)
            val apiResult = Result.Success(dummyNews)
            when(apiResult){
                is Result.Success ->{
                    _uiState.update {

                            it.copy(DisplayState.Content(apiResult.data.articles))
                    }
                }
//                is Result.Error ->{
//                    _uiState.update {
//                        it.copy(DisplayState.Error(apiResult.message))
//                    }
//                }
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

    fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(500)   // wait 500ms after user stops typing
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
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
        if(_searchQuery.value.trim() =="")
        {
            getTopHeadlines()
        }
    }


}
