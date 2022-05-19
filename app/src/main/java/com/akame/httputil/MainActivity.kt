package com.akame.httputil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import android.widget.ProgressBar
import android.widget.TextView
import com.akame.http.*
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
        findViewById<CalendarView>(R.id.calendar_view).setOnClickListener {
            mainViewModel.getBanner().observe(this) { result ->
                result.doSuccess {
                    Log.e("tag", "---->> ${it.data}")
                }.doComplete {
                    Log.e("tag","------>> 完成1")
                }.doSuccess {
                    Log.e("tag", "----->>  我也是成功的 ${it.data}")
                }.doComplete {
                    Log.e("tag","------>> 完成2")
                }.doError {
                    Log.e(
                        "Tag",
                        "----->> 请求错误 ： errorMsg = ${it.errorMessage} code:${it.errorCode} "
                    )
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
                it.doStart {
                    Log.e("tag", "------>下载开始")
                }.doSuccess {
                    Log.e(
                        "tag",
                        "----> 下载成功 ${it.file.absolutePath}"
                    )
                }.doFail {
                    Log.e("tag", "------> 下载失败 ${it.message}")
                }.doComplete {
                    Log.e("tag", "------> 下载完成")
                }.doProcess {
                    process.progress = (it.process * 100).toInt()
                }
            }
        }
    }


}