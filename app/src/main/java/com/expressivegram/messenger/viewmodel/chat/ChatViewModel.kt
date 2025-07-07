package com.expressivegram.messenger.viewmodel.chat

import androidx.lifecycle.ViewModel
import com.expressivegram.messenger.utils.TdUtility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.drinkless.tdlib.TdApi
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ChatViewModel : ViewModel() {
    suspend fun downloadFileIfNeeded(fileInfo: TdApi.File): String? {
        return withContext(Dispatchers.IO) {
            val localFile = File(fileInfo.local.path)
            if (localFile.exists()) {
                return@withContext localFile.path
            }

            suspendCoroutine { continuation ->
                val client = TdUtility.getInstance().getClient()
                client.send(
                    TdApi.DownloadFile().apply {
                        fileId = fileInfo.id
                        priority = 1
                        synchronous = false
                    }
                ) { result ->
                    when (result.constructor) {
                        TdApi.Ok.CONSTRUCTOR -> {
                            continuation.resume(localFile.path)
                        }
                        else -> {
                            continuation.resume(null)
                        }
                    }
                }
            }
        }
    }
}