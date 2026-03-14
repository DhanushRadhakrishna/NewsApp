package com.example.newsapp.data.network.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
    return withContext(Dispatchers.IO) {
        try {
            Result.Success(apiCall())
        } catch (e: UnknownHostException) {
            Result.Error(e, "No internet connection. Please check your network and try again.")
        } catch (e: ConnectException) {
            Result.Error(e, "Unable to reach the server. Please try again later.")
        } catch (e: SocketTimeoutException) {
            Result.Error(e, "The request timed out. Please check your connection and try again.")
        } catch (e: HttpException) {
            val message = when (e.code()) {
                400 -> "Bad request. Please try again."
                401 -> "Unauthorized. Your API key may be invalid or expired."
                403 -> "Access forbidden. You don't have permission to access this resource."
                404 -> "The requested content could not be found."
                429 -> "Too many requests. Please wait a moment before trying again."
                500 -> "The server encountered an error. Please try again later."
                503 -> "Service is temporarily unavailable. Please try again later."
                else -> "Unexpected server error (${e.code()}). Please try again."
            }
            Result.Error(e, message)
        } catch (e: Exception) {
            Result.Error(e, "Something went wrong. Please try again.")
        }
    }
}
