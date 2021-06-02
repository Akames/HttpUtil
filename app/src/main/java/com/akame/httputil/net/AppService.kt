package com.akame.httputil.net

import retrofit2.http.GET

interface AppService {
    companion object {
        fun getHttpRequest() =  HttpRequest().create(AppService::class.java, "https://gank.io/api/v2/")
    }

    @GET("banners")
    suspend fun getTest(): HttpResponse<List<Banner>>
}