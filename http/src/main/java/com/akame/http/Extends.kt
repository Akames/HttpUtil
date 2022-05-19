package com.akame.http

inline fun <T> ServerResult<T>.doSuccess(action: (ServerResult.Success<T>) -> Unit): ServerResult<T> {
    if (this is ServerResult.Success) {
        action.invoke(this)
    }
    return this
}

inline fun <T> ServerResult<T>.doError(action: (ServerResult.Error) -> Unit): ServerResult<T> {
    if (this is ServerResult.Error) {
        action.invoke(this)
    }
    return this
}

inline fun <T> ServerResult<T>.doLoading(action: (ServerResult.Loading) -> Unit): ServerResult<T> {
    if (this is ServerResult.Loading) {
        action.invoke(this)
    }
    return this
}

inline fun <T> ServerResult<T>.doComplete(action: (ServerResult.Complete) -> Unit): ServerResult<T> {
    if (this is ServerResult.Complete) {
        action.invoke(this)
    }
    return this
}

inline fun DownLoaderResult.doStart(action: (DownLoaderResult.Start) -> Unit): DownLoaderResult {
    if (this is DownLoaderResult.Start){
        action.invoke(this)
    }
    return this
}

inline fun DownLoaderResult.doComplete(action: (DownLoaderResult.Complete) -> Unit): DownLoaderResult {
    if (this is DownLoaderResult.Complete){
        action.invoke(this)
    }
    return this
}

inline fun DownLoaderResult.doSuccess(action: (DownLoaderResult.Success) -> Unit): DownLoaderResult {
    if (this is DownLoaderResult.Success){
        action.invoke(this)
    }
    return this
}

inline fun DownLoaderResult.doFail(action: (DownLoaderResult.Fail) -> Unit): DownLoaderResult {
    if (this is DownLoaderResult.Fail){
        action.invoke(this)
    }
    return this
}

inline fun DownLoaderResult.doProcess(action: (DownLoaderResult.Process) -> Unit): DownLoaderResult {
    if (this is DownLoaderResult.Process){
        action.invoke(this)
    }
    return this
}





