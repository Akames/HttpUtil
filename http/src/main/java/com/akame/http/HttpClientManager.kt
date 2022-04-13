package com.akame.http

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class HttpClientManager<S>(private val server: Class<S>, private val baseUrl: String) {
    var connectTimeout = 10L
    var writTimeout = 10L
    var readTimeout = 10L
    var retryConnect = true
    var isDebug = false
    private var interceptors: ArrayList<Interceptor>? = null

    fun addInterceptor(interceptor: Interceptor): HttpClientManager<S> {
        (interceptors ?: ArrayList()).add(interceptor)
        return this
    }

    fun crateClientServer(): S {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getHttpClient(interceptors))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(server)
    }

    private fun getHttpClient(interceptors: ArrayList<Interceptor>?): OkHttpClient {
        val client = OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .writeTimeout(writTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
            .retryOnConnectionFailure(retryConnect)
            .addInterceptor(getLoggingInterceptor(isDebug))
        interceptors?.forEach {
            client.addInterceptor(it)
        }
        return client.build()
    }

    private fun getLoggingInterceptor(isDebug: Boolean): Interceptor {
        val level =
            if (isDebug) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return HttpLoggingInterceptor().setLevel(level)
    }

}