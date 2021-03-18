package com.akame.httputil.net

import retrofit2.http.GET

interface AppService {
    @GET("banners")
    suspend fun getTest(): HttpResponse<List<Banner>>
}