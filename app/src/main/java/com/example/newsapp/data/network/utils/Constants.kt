package com.example.newsapp.data.network.utils

import com.example.newsapp.domain.model.ArticleHeadline
import com.example.newsapp.domain.model.HeadlineSource
import com.example.newsapp.domain.model.News

object Constants {

    val BASE_URL = "https://newsapi.org/v2/"
}


val dummyArticles = listOf(
    ArticleHeadline(
        source = HeadlineSource(id = "101", name = "ScienceAlert"),
        title = "'Major Discovery': After Years of Research, Scientists Found a New Chemical Reaction",
        description = "A peculiar observation during laboratory experiments has led researchers to the breakthrough of a lifetime.",
        url = "https://www.sciencealert.com/major-discovery-after-years-of-research-scientists-found-a-new-chemical-reaction",
        publishedAt = "2026-03-13T10:03:29Z"
    ),
    ArticleHeadline(
        source = HeadlineSource(id = "the-wall-street-journal", name = "The Wall Street Journal"),
        title = "Adobe CEO to Depart After 18 Years Amid AI Disruptions",
        description = "Adobe posts higher sales with CEO set to depart.",
        url = "https://www.wsj.com/business/earnings/adobe-posts-higher-sales-with-ceo-set-to-depart-ce30b19f",
        publishedAt = "2026-03-13T09:11:00Z"
    ),
    ArticleHeadline(
        source = HeadlineSource(id = "politico", name = "Politico"),
        title = "Vance was 'skeptical' voice in White House on Iran strikes",
        description = "White House officials revealed that the vice president made his opposition known in the leadup.",
        url = "https://www.politico.com/news/2026/03/13/jd-vance-skeptical-iran-operation-00826780",
        publishedAt = "2026-03-13T09:00:00Z"
    ),
    ArticleHeadline(
        source = HeadlineSource(id = "NPR", name = "NPR"),
        title = "Egg prices have taken a beating. What's behind the drop?",
        description = "A year ago, eggs were scarce and prices were sky-high. But avian flu took a much smaller toll on America's egg-laying chickens this winter than last, and egg prices have tumbled 42%.",
        url = "https://www.npr.org/2026/03/13/nx-s1-5745075/egg-prices-avian-flu",
        publishedAt = "2026-03-13T09:00:00Z"
    ),
    ArticleHeadline(
        source = HeadlineSource(id = "Financial Times", name = "Financial Times"),
        title = "Poland's Eurosceptic president vetoes EU funding for defence",
        description = "Karol Nawrocki opposes Safe loans, claiming they would give Brussels a say in national security matters.",
        url = "https://www.ft.com/content/da80dd64-f475-409e-b9ea-1ad979e1a40d",
        publishedAt = "2026-03-13T08:50:17Z"
    ),
    ArticleHeadline(
        source = HeadlineSource(id = "101", name = "ScienceAlert"),
        title = "'Major Discovery': After Years of Research, Scientists Found a New Chemical Reaction",
        description = "A peculiar observation during laboratory experiments has led researchers to the breakthrough of a lifetime.",
        url = "https://www.sciencealert.com/major-discovery-after-years-of-research-scientists-found-a-new-chemical-reaction",
        publishedAt = "2026-03-13T10:03:29Z"
    ),
    ArticleHeadline(
        source = HeadlineSource(id = "the-wall-street-journal", name = "The Wall Street Journal"),
        title = "Adobe CEO to Depart After 18 Years Amid AI Disruptions",
        description = "Adobe posts higher sales with CEO set to depart.",
        url = "https://www.wsj.com/business/earnings/adobe-posts-higher-sales-with-ceo-set-to-depart-ce30b19f",
        publishedAt = "2026-03-13T09:11:00Z"
    ),
    ArticleHeadline(
        source = HeadlineSource(id = "politico", name = "Politico"),
        title = "Vance was 'skeptical' voice in White House on Iran strikes",
        description = "White House officials revealed that the vice president made his opposition known in the leadup.",
        url = "https://www.politico.com/news/2026/03/13/jd-vance-skeptical-iran-operation-00826780",
        publishedAt = "2026-03-13T09:00:00Z"
    ),
    ArticleHeadline(
        source = HeadlineSource(id = "NPR", name = "NPR"),
        title = "Egg prices have taken a beating. What's behind the drop?",
        description = "A year ago, eggs were scarce and prices were sky-high. But avian flu took a much smaller toll on America's egg-laying chickens this winter than last, and egg prices have tumbled 42%.",
        url = "https://www.npr.org/2026/03/13/nx-s1-5745075/egg-prices-avian-flu",
        publishedAt = "2026-03-13T09:00:00Z"
    ),
    ArticleHeadline(
        source = HeadlineSource(id = "Financial Times", name = "Financial Times"),
        title = "Poland's Eurosceptic president vetoes EU funding for defence",
        description = "Karol Nawrocki opposes Safe loans, claiming they would give Brussels a say in national security matters.",
        url = "https://www.ft.com/content/da80dd64-f475-409e-b9ea-1ad979e1a40d",
        publishedAt = "2026-03-13T08:50:17Z"
    )
)
val dummyNews = News(dummyArticles)
