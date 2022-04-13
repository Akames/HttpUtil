package com.akame.http

import java.io.File

sealed class DownLoaderResult {
    object Start : DownLoaderResult()
    object Complete : DownLoaderResult()
    data class Success(val file: File) : DownLoaderResult()
    data class Fail(val message: String) : DownLoaderResult()
    data class Process(val process: Float) : DownLoaderResult()
}