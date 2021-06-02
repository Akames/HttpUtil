package com.akame.httputil.repository

import androidx.annotation.MainThread
import com.akame.http.apiRequest
import com.akame.http.apiRequestNoResult
import com.akame.httputil.net.AppService

class MainRepository {
    @MainThread
    fun loadMainData1(error: () -> Unit, complete: () -> Unit) =
        apiRequest({ AppService.getHttpRequest().getTest() }, {
            error.invoke()
        }, {
            complete.invoke()
        })

    fun loadMain2() = apiRequest { AppService.getHttpRequest().getTest() }

    suspend fun loadMain3() = apiRequestNoResult { AppService.getHttpRequest().getTest() }
}