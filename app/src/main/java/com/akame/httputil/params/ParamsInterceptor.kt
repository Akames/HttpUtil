package com.akame.httputil.params

import com.akame.http.BaseParamsInterceptor
import okhttp3.HttpUrl
import okhttp3.Request

class ParamsInterceptor : BaseParamsInterceptor() {
    override fun addParams(builder: HttpUrl.Builder) {
        //添加公共参数
        builder.addQueryParameter("canshu", "2222")
    }

    override fun addHeadParams(builder: Request.Builder) {
        //添加请求头
        builder.addHeader("canshu", "111")
    }
}