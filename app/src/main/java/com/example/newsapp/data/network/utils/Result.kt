package com.example.newsapp.data.network.utils

sealed class Result<out T> {

    data class Success<T>(val data : T) : Result<T>()
    data class Error<T>(val throwable : Throwable, val message : String) : Result<T>()

}