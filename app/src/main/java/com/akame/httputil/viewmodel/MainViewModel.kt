package com.akame.httputil.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.akame.http.DownloadCallback
import com.akame.http.apiDownload
import com.akame.httputil.net.AppService
import com.akame.httputil.repository.MainRepository
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    fun getBanner() = repository.loadMainData1({
    }, {

    })

    fun getBannerList() = repository.loadMain2()

    fun getBannerListNoResult() {
        viewModelScope.launch {
            repository.loadMain3()
        }
    }

    fun testDownLoadFile(downloadUrl: String, savaPath: String) {
        viewModelScope.launch {
            apiDownload({
                AppService.getHttpRequest().downloadFile(downloadUrl)
            },
                savaPath,
                object : DownloadCallback {
                    override fun onStart() {
                        Log.e("tag","----下载开始 ")
                    }

                    override fun onComplete() {
                        Log.e("tag","----下载完毕 ")
                    }

                    override fun onSuccess(file: File) {
                        Log.e("tag","----下载成功 ${file.absolutePath}")
                    }

                    override fun onFail(msg: String) {
                        Log.e("tag","----下载失败 ${msg}")
                    }

                    override fun onProcess(process: Float) {
                        Log.e("tag","----下载中 ${process}")
                    }
                }
            )
        }
    }
}

class MainViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = MainViewModel(repository)
        return viewModel as T
    }
}
