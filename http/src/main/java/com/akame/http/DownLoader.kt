package com.akame.http

import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun apiDownload(
    savePath: String,
    requestServer: suspend () -> ResponseBody
) = liveData(Dispatchers.Main) {
    emit(DownLoaderResult.Start)
    try {
        val file = getFile(savePath)
        withContext(Dispatchers.IO) {
            val responseBody = requestServer.invoke()
            val inputStream = responseBody.byteStream()
            val totalLength = responseBody.contentLength()
            downLoadFile(inputStream, file, totalLength) {
                launch(Dispatchers.Main) {
                    emit(DownLoaderResult.Process(it))
                }
            }
        }
        emit(DownLoaderResult.Success(file))
    } catch (e: Exception) {
        val errorMsg = AnalyzeNetException.analyze(e)
        emit(DownLoaderResult.Fail(errorMsg))
    } finally {
        emit(DownLoaderResult.Complete)
    }
}

private inline fun downLoadFile(
    inputStream: InputStream,
    file: File,
    totalLength: Long,
    onProcess: (Float) -> Unit,
) {
    inputStream.use {
        BufferedOutputStream(FileOutputStream(file)).use {
            val byte = ByteArray(HttpClientManager.DOWNLOAD_SPEED)
            var len: Int
            var currentLen = 0f
            while ((inputStream.read(byte, 0, byte.size).also { len = it }) != -1) {
                it.write(byte, 0, len)
                currentLen += len
                onProcess.invoke(currentLen / totalLength)
            }
        }
    }
}

private fun getFile(path: String): File {
    val file = File(path)
    if (!file.exists()) {
        if (file.parentFile?.exists() == false) {
            file.parentFile?.mkdir()
        }
        file.createNewFile()
    }
    return file
}


