package com.akame.http

sealed class Result<T>(val data: T? = null, val exception: Exception? = null) {
    class Loading : Result<Nothing>()

    class Success<T>(data: T?) : Result<T>(data)

    class Error(exception: Exception?) : Result<Nothing>(exception = exception)
}