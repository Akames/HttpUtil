package com.akame.httputil.net

object Repository {
    fun getHttpRequest() =  HttpRequest().create(AppService::class.java, "https://gank.io/api/v2/")
}