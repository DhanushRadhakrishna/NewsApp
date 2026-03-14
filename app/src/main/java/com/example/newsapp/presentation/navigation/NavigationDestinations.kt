package com.example.newsapp.presentation.navigation

import kotlinx.serialization.Serializable

interface Destination{
    val title : String
}

@Serializable
object FavoritesDestination : Destination{
    override val title: String
        get() = "Favorites"
}

@Serializable
object HomeDestination : Destination{
    override val title: String
        get() = "Home"
}