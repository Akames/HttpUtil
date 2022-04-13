package com.akame.httputil.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.akame.http.apiDownload
import com.akame.httputil.net.AppService
import com.akame.httputil.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    fun getBanner() = repository.loadMain()

    fun getBannerListNoResult() {
        viewModelScope.launch {
            repository.loadMain3()
        }
    }

    fun testDownLoadFile(downloadUrl: String, savaPath: String) = apiDownload(savaPath) {
        AppService.getHttpRequest().downloadFile(downloadUrl)
    }

    fun getDownLoadUrl() = "https://dl.todesk.com/macos/ToDesk_4.2.2.pkg"

    fun getSavePath(context: Context) = context.externalCacheDir?.let {
        it.absolutePath + "/呵呵.apk"
    } ?: ""
}

class MainViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = MainViewModel(repository)
        return viewModel as T
    }
}
