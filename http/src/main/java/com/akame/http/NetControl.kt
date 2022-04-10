package com.akame.http

import androidx.lifecycle.liveData
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.*

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
        fail?.invoke(Exception(errorMsg))
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


suspend fun apiDownload(
    requestServer: suspend () -> ResponseBody,
    savePath: String,
    downloadCallback: DownloadCallback
) {
    downloadCallback.onStart()
    try {
        val responseBody = withContext(Dispatchers.IO) {
            requestServer.invoke()
        }
        val inputStream = responseBody.byteStream()
        val totalLength = responseBody.contentLength()
        writFile(inputStream, getDownFile(savePath), totalLength, downloadCallback)
    } catch (e: Exception) {
        downloadCallback.onFail(e.message ?: "")
    } finally {
        downloadCallback.onComplete()
    }
}

private fun getDownFile(path: String): File {
    val file = File(path)
    if (!file.exists()) {
        if (file.parentFile?.exists() == false) {
            file.parentFile?.mkdir()
        }
        file.createNewFile()
    }
    return file
}

private suspend fun writFile(
    inputStream: InputStream,
    file: File,
    totalLength: Long,
    callback: DownloadCallback
) {
    withContext(Dispatchers.IO) {
        inputStream.use {
            BufferedOutputStream(FileOutputStream(file)).use {
                val byte = ByteArray(1024)
                var len = 0
                var currentLen = 0f
                while ((inputStream.read(byte, 0, 1024).also { len = it }) != -1) {
                    it.write(byte, 0, len)
                    currentLen += len
                    withContext(Dispatchers.Main) {
                        callback.onProcess(currentLen / totalLength)
                    }
                }
                withContext(Dispatchers.Main) {
                    callback.onSuccess(file)
                }
            }
        }
    }
}

interface DownloadCallback {
    fun onStart()

    fun onComplete()

    fun onSuccess(file: File)

    fun onFail(msg: String)

    fun onProcess(process: Float)
}

