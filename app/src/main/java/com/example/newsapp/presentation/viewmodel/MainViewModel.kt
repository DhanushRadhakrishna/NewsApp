package com.example.newsapp.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.usecase.NewsUseCase
import com.example.newsapp.presentation.uistate.TopHeadlinesUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.newsapp.data.network.utils.Result
import com.example.newsapp.presentation.screens.TopHeadlines
import kotlinx.coroutines.flow.update

@HiltViewModel
class MainViewModel @Inject constructor(
    private val newsUseCase : NewsUseCase
)  : ViewModel(){


    private val _uiState = MutableStateFlow<TopHeadlinesUIState>(TopHeadlinesUIState.Loading)
    val uiState : StateFlow<TopHeadlinesUIState> = _uiState.asStateFlow()

    private var page = 1

    init {
        page = 1
        getTopHeadlines()
    }

    fun getTopHeadlines(){
        page = 1
        viewModelScope.launch {
            _uiState.value = TopHeadlinesUIState.Loading
            val apiResult = newsUseCase.invoke(page)
            when(apiResult){
                is Result.Success ->{
                    _uiState.value = TopHeadlinesUIState.Success(apiResult.data.articles)
                }
                is Result.Error ->{
                    _uiState.value = TopHeadlinesUIState.Error(apiResult.message+" More Info: ${apiResult.throwable.message}")
                }
            }
        }
    }
}