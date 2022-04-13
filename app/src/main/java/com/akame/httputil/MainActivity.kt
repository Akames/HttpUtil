package com.akame.httputil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import com.akame.http.DownLoaderResult
import com.akame.http.ServerResult
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
        testSever()
        testDownLoader()
    }

    private fun testSever() {
        mainViewModel.getBanner().observe(this) {
            when (it) {
                is ServerResult.Loading -> {
                    Log.e("tag", "----> 请求开始")
                }

                is ServerResult.Success -> {
                    Log.e("tag", "----> 请求结束 ${it.data!!.data}")
                }

                is ServerResult.Error -> {
                    Log.e("tag", "----> 请求错误 ${it.exception!!.message}")
                }

                is ServerResult.Complete -> {
                    Log.e("tag", "----> 请求完成")
                }
            }
        }
    }

    private fun testDownLoader() {
        val process = findViewById<ProgressBar>(R.id.progress_horizontal)
        val tvDownloader = findViewById<TextView>(R.id.tv_downloader)
        tvDownloader.setOnClickListener {
            mainViewModel.testDownLoadFile(
                mainViewModel.getDownLoadUrl(),
                mainViewModel.getSavePath(this)
            ).observe(this) {
                when (it) {
                    is DownLoaderResult.Start -> Log.e("tag", "------>下载开始")
                    is DownLoaderResult.Success -> Log.e(
                        "tag",
                        "----> 下载成功 ${it.file.absolutePath}"
                    )
                    is DownLoaderResult.Fail -> Log.e("tag", "------> 下载失败 ${it.message}")
                    is DownLoaderResult.Complete -> Log.e("tag", "------> 下载完成")
                    is DownLoaderResult.Process -> {
                        process.progress = (it.process * 100).toInt()
                    }
                }
            }
        }
    }
}