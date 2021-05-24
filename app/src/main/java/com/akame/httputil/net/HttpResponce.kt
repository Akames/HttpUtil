package com.akame.httputil.net

import com.akame.http.BaseResponse

data class HttpResponse<T>(
    var data: T? = null,
    val status: Int,
    val resultNum: Int,
    val total: Int
) : BaseResponse {

    override fun isRequestSuccess(): Boolean = status == 100


    override fun getErrorMsg(): String = ""

    override fun onRequestFail() {
        //这里判断 业务请求错误
    }

    fun isLastPage(): Boolean = resultNum == total
}

data class Banner(val image: String, val title: String)