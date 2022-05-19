package com.akame.http

interface BaseResponse {
    fun isRequestSuccess(): Boolean

    fun getErrorMsg(): String

    fun getErrorCode():Int

    fun onRequestFail()
}