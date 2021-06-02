package com.akame.httputil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.akame.httputil.repository.MainRepository
import com.akame.httputil.viewmodel.MainViewModel
import com.akame.httputil.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private val mainViewModel by lazy {
        MainViewModelFactory(MainRepository()).create(MainViewModel::class.java)
    }

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