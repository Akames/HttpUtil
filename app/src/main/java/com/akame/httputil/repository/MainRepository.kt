package com.akame.httputil.repository

import androidx.annotation.MainThread
import com.akame.http.apiRequest
import com.akame.http.apiRequestNoResult
import com.akame.httputil.net.AppService

class MainRepository {

    fun loadMain() = apiRequest { AppService.getHttpRequest().getTest() }

    suspend fun loadMain3() = apiRequestNoResult { AppService.getHttpRequest().getTest() }

    suspend fun testFlow() = AppService.getHttpRequest().getTest()
}