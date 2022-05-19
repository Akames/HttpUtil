package com.akame.http

sealed class ServerResult<T> {
    object Loading : ServerResult<Nothing>()

    class Success<T>(val data: T) : ServerResult<T>()

    class Error(val errorMessage: String, val errorCode: Int) : ServerResult<Nothing>()

    object Complete : ServerResult<Nothing>()
}