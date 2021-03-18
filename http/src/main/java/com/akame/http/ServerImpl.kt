package com.akame.http

import kotlinx.coroutines.CoroutineScope

interface ServerImpl {
    fun launchService(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: (String) -> Unit = {},
        finalBlock: () -> Unit = {}
    )
}