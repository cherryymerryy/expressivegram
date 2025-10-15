package com.expressivegram.messenger.utils

import com.expressivegram.messenger.ApplicationLoader
import com.expressivegram.messenger.TELEGRAM_DATABASE_PATH
import com.expressivegram.messenger.TELEGRAM_FILES_PATH
import com.expressivegram.messenger.extensions.send
import org.drinkless.tdlib.TdApi
import java.io.File

class DownloadController {
    companion object {
        @Volatile
        private var instance: DownloadController? = null

        fun getInstance(): DownloadController {
            return instance ?: synchronized(this) {
                instance ?: DownloadController().also { instance = it }
            }
        }

        fun initialize() {
            getInstance()

            val databaseDir = File(ApplicationLoader.applicationContext.filesDir, TELEGRAM_DATABASE_PATH)
            if (!databaseDir.exists()) {
                databaseDir.mkdir()
            }

            val filesDir = File(ApplicationLoader.applicationContext.filesDir, TELEGRAM_FILES_PATH)
            if (!filesDir.exists()) {
                filesDir.mkdir()
            }
        }
    }

    fun downloadFile(fileId: Int, synchronous: Boolean = true) {
        TdUtility
            .getInstance()
            .getClient()
            .send(
                TdApi.DownloadFile(
                    fileId,
                    1,
                    0,
                    0,
                    synchronous
                )
            )
    }
}