package com.example.newsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.local.entity.FavoriteArticle
import com.example.newsapp.domain.repository.FavoriteRepository
import com.example.newsapp.domain.usecase.FavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor (
    private val favoritesUseCase: FavoritesUseCase
) : ViewModel(){

    val allFavorites : StateFlow<List<FavoriteArticle>> = favoritesUseCase.allFavorites
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun removeFromFavorites(article : FavoriteArticle)
    {
        viewModelScope.launch { favoritesUseCase.removeFavorite(article) }
    }

}