package com.akame.http

sealed class ServerResult<T>(val data: T? = null, val exception: Exception? = null) {
    object Loading : ServerResult<Nothing>()

    class Success<T>(data: T) : ServerResult<T>(data)

    class Error(exception: Exception) : ServerResult<Nothing>(exception = exception)

    object Complete : ServerResult<Nothing>()
}