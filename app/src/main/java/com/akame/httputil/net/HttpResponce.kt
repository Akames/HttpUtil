package com.akame.httputil.net

import com.akame.http.BaseResponse
import com.google.gson.annotations.SerializedName

data class HttpResponse<T>(
    var data: T? = null,
    val errorCode: Int,
    @SerializedName("errorMsg")
    val errMsg: String,
) : BaseResponse {

    override fun isRequestSuccess(): Boolean = errorCode == 0

    override fun getErrorMsg(): String = errMsg

    override fun onRequestFail() {
        //这里判断 业务请求错误
    }
}

data class Banner(val imagePath: String, val desc: String)