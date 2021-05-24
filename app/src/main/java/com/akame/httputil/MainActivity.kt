package com.akame.httputil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.akame.http.ApiStatue
import com.akame.httputil.net.Banner
import com.akame.httputil.net.HttpResponse
import com.akame.httputil.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private val mainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getData()
    }

    private fun getData() {
        mainViewModel.getBanner().observe(this) {
            Log.e("tag", "---requestResult--->>  ${it.data?.toString() ?: ""}")
        }
    }
}