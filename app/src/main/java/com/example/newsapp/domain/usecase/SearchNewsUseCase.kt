package com.example.newsapp.domain.usecase

import com.example.newsapp.data.network.utils.Result
import com.example.newsapp.domain.model.News
import com.example.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

class SearchNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(query : String) : Result<News> {
        return newsRepository.getSearchedNews(query)
    }
}