package com.akame.http

import android.util.Log
import androidx.lifecycle.liveData
import kotlinx.coroutines.*

fun <T : BaseResponse> apiRequest(
    requestServer: suspend () -> T,
) = liveData(Dispatchers.Main) {
    try {
        emit(Result.Loading())
        val data = withContext(Dispatchers.IO) {
            //进行网络请求 返回请求数据
            requestServer()
        }
        //分析后端返回的数据是否正在的请求成功
        data.checkResult({
            emit(Result.Success(data))
        }, {
            emit(Result.Error(Exception(data.getErrorMsg())))
        })
    } catch (e: Exception) {
        val errorMsg = AnalyzeNetException.analyze(e)
        if (HttpConfig.printLogEnable)
            Log.e(javaClass.simpleName, errorMsg)
        emit(Result.Error(e))
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
        val errorMsg = AnalyzeNetException.analyze(e)
        if (HttpConfig.printLogEnable)
            Log.e(javaClass.simpleName, errorMsg)
    }
}


/**
 * 进行网络请求
 * @param requestServer  请求对象
 * @param fail 网络请求失败，比如断网 参数错误，地址错误等等
 * @param complete 请求完成 不论成功和失败 最终都会回调用 用来处理一些资源回收等 可以不传
 */
fun <T : BaseResponse> apiRequest(
    requestServer: suspend () -> T,
    fail: ((Exception) -> Unit)? = null,
    complete: (() -> Unit)? = null
) = liveData(Dispatchers.Main) {
    try {
        val data = withContext(Dispatchers.IO) {
            //进行网络请求 返回请求数据
            requestServer()
        }
        //分析后端返回的数据是否正在的请求成功
        data.checkResult({
            emit(data)
        }, {
            data.onRequestFail()
            fail?.invoke(Exception(data.getErrorMsg()))
        })
    } catch (e: Exception) {
        val errorMsg = AnalyzeNetException.analyze(e)
        if (HttpConfig.printLogEnable)
            Log.e(javaClass.simpleName, errorMsg)
        fail?.invoke(e)
    } finally {
        complete?.invoke()
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

