package com.erapps.newws.data

import java.io.IOException

sealed class Result<out R, out E> {

    data class Success<out T>(val data: T?) : Result<T, Nothing>()
    data class Error<out E>(
        val data: E? = null,
        val code: Int? = null,
        val exception: Throwable? = null
    ) : Result<Nothing, E>() {
        val isApiError: Boolean get() = data != null && code != null
        val isNetworkError: Boolean get() = exception is IOException
        val isUnknownError: Boolean get() = !isApiError && !isNetworkError
    }

    object Loading : Result<Nothing, Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error<*> -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}
val Result<*, *>.succeeded get() = this is Result.Success && data != null