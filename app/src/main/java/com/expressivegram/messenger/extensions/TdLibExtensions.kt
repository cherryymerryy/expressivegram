package com.expressivegram.messenger.extensions

import com.expressivegram.messenger.data.TdLibException
import com.expressivegram.messenger.utils.Log
import kotlinx.coroutines.suspendCancellableCoroutine
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun <T : TdApi.Object> Client.execute(query: TdApi.Function<T>): T {
    return suspendCancellableCoroutine { continuation ->
        this.send(query) { result ->
            if (!continuation.isActive) return@send

            @Suppress("UNCHECKED_CAST")
            when (result) {
                is TdApi.Error -> {
                    continuation.resumeWithException(TdLibException(result))
                }
                else -> {
                    try {
                        continuation.resume(result as T)
                    } catch (e: ClassCastException) {
                        continuation.resumeWithException(
                            IllegalStateException("Unexpected result type: ${result.javaClass.simpleName}", e)
                        )
                    }
                }
            }
        }
    }
}

fun <T: TdApi.Object> Client.send(query: TdApi.Function<T>) {
    this.send(query) { Log.e("Sent $query") }
}