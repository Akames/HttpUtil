package com.akame.http

import com.google.gson.JsonParseException
import retrofit2.HttpException
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

internal object AnalyzeNetException {
    fun analyze(e: Exception): String {
        return when (e) {
            is HttpException -> "netError"

            is JsonParseException -> "parseError"

            is ConnectException, is SocketException, is UnknownHostException -> "connectError"

            is SocketTimeoutException -> "timeoutError"

            is SSLHandshakeException -> "SSLHandError"

            else -> e.message ?: "unKnowError"
        }
    }
}