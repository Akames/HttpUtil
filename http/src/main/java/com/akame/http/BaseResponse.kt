package com.akame.http

interface BaseResponse<T> {
    fun isRequestSuccess(): Boolean
    fun getRequestData(): T?
    fun getErrorMsg(): String
    fun onRequestFail(data: T?)
}