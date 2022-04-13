package com.akame.httputil.net

import com.akame.http.BaseParamsInterceptor
import com.akame.http.BuildConfig
import com.akame.http.HttpClientManager
import com.akame.httputil.params.ParamsInterceptor
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface AppService {
    companion object {
        fun getHttpRequest(): AppService {
            return HttpClientManager(AppService::class.java, "https://www.wanandroid.com/")
                .apply {
                    isDebug = BuildConfig.DEBUG
                    addInterceptor(ParamsInterceptor())
                }.crateClientServer()
        }
    }

    @GET("banner/json")
    suspend fun getTest(): HttpResponse<List<Banner>>

    @Streaming
    @GET
    suspend fun downloadFile(@Url url: String): ResponseBody
}