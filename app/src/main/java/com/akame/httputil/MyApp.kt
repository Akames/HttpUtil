package com.akame.httputil

import android.app.Application
import com.akame.http.HttpConfig

class MyApp : Application() {
    companion object {
        lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        HttpConfig.printLogEnable = true

    }
}