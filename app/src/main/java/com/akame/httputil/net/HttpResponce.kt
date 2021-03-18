package com.akame.httputil.net

import com.akame.http.BaseResponse

data class HttpResponse<T>(
    var data: T? = null,
    val status: Int
) : BaseResponse<T> {

    override fun isRequestSuccess(): Boolean = status == 100

    override fun getRequestData(): T? = data

    override fun getErrorMsg(): String = ""

    override fun onRequestFail(data: T?) {

    }
}

data class Banner(val image: String, val title: String)