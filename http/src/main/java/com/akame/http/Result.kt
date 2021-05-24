package com.akame.http

object Result {

    fun loading() = ApiResult(ApiStatue.LOADING, null, null)

    fun <T : BaseResponse> success(data: T) = ApiResult(ApiStatue.SUCCESS, data, null)

    fun error(exception: Exception? = null) = ApiResult(ApiStatue.FAIL, null, exception)

    fun complete() = ApiResult(ApiStatue.COMPLETE, null, null)
}

data class ApiResult<T>(val statue: ApiStatue, val result: T?, val exception: Exception?)

enum class ApiStatue {
    SUCCESS,
    FAIL,
    LOADING,
    COMPLETE
}

