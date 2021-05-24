package com.akame.http

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class BaseRetrofitClient {
    fun <S> create(serverClass: Class<S>, baseUrl: String): S =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(serverClass)

    private val client: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
                .connectTimeout(HttpConfig.connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(HttpConfig.writeTimeout, TimeUnit.SECONDS)
                .readTimeout(HttpConfig.readTimeout, TimeUnit.SECONDS)
                .retryOnConnectionFailure(HttpConfig.retryConnect)
                //添加请求日志
                .addInterceptor(HttpLoggingInterceptor().setLevel(if (HttpConfig.printLogEnable) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE))
            return configOkHttpBuilder(builder).build()
        }

    protected abstract fun configOkHttpBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder
}