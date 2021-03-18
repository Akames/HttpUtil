package com.akame.http

import com.google.gson.JsonParseException
import retrofit2.HttpException
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

object AnalyzeNetException {
    fun analyze(e: Exception): String {
        return when (e) {
            is HttpException -> "网络错误"

            is JsonParseException -> "解析错误"

            is ConnectException, is SocketException, is UnknownHostException -> "连接失败"

            is SocketTimeoutException -> "连接超时"

            is SSLHandshakeException -> "证书验证失败"

            else -> e.message ?: "未知错误"
        }
    }
}