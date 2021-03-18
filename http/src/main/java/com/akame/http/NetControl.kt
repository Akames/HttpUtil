package com.akame.http

import android.util.Log
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
fun <T> ServerImpl.requestServer(
    requestServer: suspend CoroutineScope.() -> BaseResponse<T>,
    success: (T?) -> Unit,
    fail: ((String) -> Unit)? = null,
    complete: (() -> Unit)? = null
) {
    launchService(tryBlock = {
        val data = withContext(Dispatchers.IO) {
            //进行网络请求 返回请求数据
            requestServer()
        }
        //分析后端返回的数据是否正在的请求成功
        if (data.isRequestSuccess()) {
            success(data.getRequestData())
        } else {
            data.onRequestFail(data.getRequestData())
            fail?.invoke(data.getErrorMsg())
        }
    },
        catchBlock = {
            //解析错误
            filterMsg(it).apply {
                if (this.isNotEmpty()) {
                    fail?.invoke(this)
                }
            }
            if (HttpConfig.isDebug)
                Log.e(javaClass.simpleName, it)
        },
        finalBlock = { complete?.invoke() })
}


fun filterMsg(msg: String): String {
    return when {
        msg == "Job was cancelled" -> {
            ""
        }

        msg.contains("failed to connect") -> {
            "当前网络超时，请稍后再试！"
        }

        else -> {
            msg
        }
    }
}


