package com.example.newsapp.domain.usecase

import com.example.newsapp.domain.model.News
import com.example.newsapp.domain.repository.NewsRepository
import javax.inject.Inject
import com.example.newsapp.data.network.utils.Result


class NewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
){

    //will need a repository to get list of news
    suspend operator fun invoke(page : Int) : Result<News> {
        return newsRepository.getPagedHeadlines(page)
    }

}