package com.akame.httputil.viewmodel

import androidx.lifecycle.ViewModel
import com.akame.http.apiRequest
import com.akame.httputil.net.Repository

class MainViewModel : ViewModel() {
    fun getBanner() = apiRequest({ Repository.getHttpRequest().getTest() },
        {
            //异常回调
        }, {
            //结束回调
        })


}