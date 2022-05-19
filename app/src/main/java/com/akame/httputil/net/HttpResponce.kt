package com.akame.httputil.net

import com.akame.http.BaseResponse
import com.google.gson.annotations.SerializedName

data class HttpResponse<T>(
    var data: T? = null,
    @SerializedName("errorCode")
    val errCode: Int,
    @SerializedName("errorMsg")
    val errMsg: String,
) : BaseResponse {

    override fun isRequestSuccess(): Boolean = errCode == 0

    override fun getErrorMsg(): String = errMsg

    override fun getErrorCode(): Int = errCode

    override fun onRequestFail() {
        //这里判断 业务请求错误
    }
}

data class Banner(val imagePath: String, val desc: String)