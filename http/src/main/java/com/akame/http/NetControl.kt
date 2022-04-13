package com.akame.http

import androidx.lifecycle.liveData
import kotlinx.coroutines.*
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.*
import kotlin.contracts.contract

fun <T : BaseResponse> apiRequest(
    requestServer: suspend () -> T,
) = liveData(Dispatchers.Main) {
    try {
        emit(ServerResult.Loading)
        val data = withContext(Dispatchers.IO) {
            //进行网络请求 返回请求数据
            requestServer()
        }
        //分析后端返回的数据是否正在的请求成功
        data.checkResult({
            emit(ServerResult.Success(data))
        }, {
            emit(ServerResult.Error(Exception(data.getErrorMsg())))
        })
    } catch (e: Exception) {
        val errorMsg = AnalyzeNetException.analyze(e)
        emit(ServerResult.Error(Exception(errorMsg)))
    } finally {
        emit(ServerResult.Complete)
    }
}

suspend fun <T : BaseResponse> apiRequestNoResult(
    requestServer: suspend () -> T
) = withContext(Dispatchers.Main) {
    try {
        withContext(Dispatchers.IO) {
            requestServer()
        }.checkResult()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private inline fun BaseResponse.checkResult(success: () -> Unit, fail: () -> Unit) {
    if (isRequestSuccess()) {
        success.invoke()
    } else {
        onRequestFail()
        fail.invoke()
    }
}

private fun BaseResponse.checkResult() {
    if (!isRequestSuccess()) {
        onRequestFail()
    }
}


