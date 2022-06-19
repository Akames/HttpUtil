package com.akame.httputil.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.akame.http.ServerResult
import com.akame.http.apiDownload
import com.akame.http.doSuccess
import com.akame.httputil.net.AppService
import com.akame.httputil.net.HttpResponse
import com.akame.httputil.repository.MainRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {
//    private val testFlow  = MutableStateFlow()
    fun getBanner() = repository.loadMain().map {
        it.doSuccess {
            //拦截数据 做对应处理
        }
        it
    }

    fun testData() = repository.loadMain().switchMap {
        //这里没法变幻值，由于it已经制定确定了返回的泛型类型 重新switchMap并不能修改
        //这样让返回值变成了2种类型 造成了返回类型不确定 kt默认为Any
        liveData {
            it.doSuccess {
               val succes =  ServerResult.Success(HttpResponse<String>("1111",it.data.errCode,it.data.errMsg))
                emit(succes)
            }
            emit(it)
        }
    }

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
