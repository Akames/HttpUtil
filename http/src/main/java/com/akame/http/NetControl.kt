package com.akame.http

import android.util.Log
import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 进行网络请求
 * @param requestServer  请求对象
 * @param success 网络请求成功。这个成功仅仅代表网络请求后台有响应了，并不代表接口逻辑真正的的成功，如果遇到断网 或者参数错误 会走下面的fail
 * @param fail 网络请求失败，比如断网 参数错误，地址错误等等
 * @param complete 请求完成 不论成功和失败 最终都会回调用 用来处理一些资源回收等 可以不传
 */
//fun <T : BaseResponse> apiRequest(
//    requestServer: suspend () -> T,
//) = liveData(Dispatchers.Main) {
//    try {
//        emit(Result.loading())
//        val data = withContext(Dispatchers.IO) {
//            //进行网络请求 返回请求数据
//            requestServer()
//        }
//        //分析后端返回的数据是否正在的请求成功
//        if (data.isRequestSuccess()) {
//            emit(Result.success(data))
//        } else {
//            data.onRequestFail()
//            emit(Result.error(Exception(data.getErrorMsg())))
//        }
//    } catch (e: Exception) {
//        val errorMsg = AnalyzeNetException.analyze(e)
//        if (HttpConfig.printLogEnable)
//            Log.e(javaClass.simpleName, errorMsg)
//        emit(Result.error(e))
//    } finally {
//        emit(Result.complete())
//    }
//}


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
        if (data.isRequestSuccess()) {
            emit(data)
        } else {
            data.onRequestFail()
            fail?.invoke(Exception(data.getErrorMsg()))
        }
    } catch (e: Exception) {
        val errorMsg = AnalyzeNetException.analyze(e)
        if (HttpConfig.printLogEnable)
            Log.e(javaClass.simpleName, errorMsg)
        fail?.invoke(e)
    } finally {
        complete?.invoke()
    }
}
