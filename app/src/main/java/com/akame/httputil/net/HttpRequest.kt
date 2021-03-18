package com.akame.httputil.net

import com.akame.http.BaseRetrofitClient
import com.akame.httputil.params.ParamsInterceptor
import okhttp3.OkHttpClient

class HttpRequest : BaseRetrofitClient() {
    override fun configOkHttpBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.addInterceptor(ParamsInterceptor()) //添加公共参数
        return builder
    }
}