package com.akame.http

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

abstract class BaseParamsInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        //添加公有参数
        request = addParams(request)
        return chain.proceed(request)
    }

    private fun addParams(request: Request): Request {
        val builder = request.url.newBuilder()
        addParams(builder)
        val requestBuild = request.newBuilder()
        addHeadParams(requestBuild)
        return requestBuild
            .method(request.method, request.body)
            .url(builder.build())
            .build()
    }

    abstract fun addParams(builder: HttpUrl.Builder)

    abstract fun addHeadParams(builder: Request.Builder)
}