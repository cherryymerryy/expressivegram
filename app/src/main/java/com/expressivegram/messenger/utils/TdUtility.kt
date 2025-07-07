package com.expressivegram.messenger.utils

import android.os.Build
import com.expressivegram.messenger.ApplicationLoader
import com.expressivegram.messenger.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import org.drinkless.tdlib.TdApi.LogStreamFile
import org.drinkless.tdlib.TdApi.SetLogStream
import org.drinkless.tdlib.TdApi.SetLogVerbosityLevel
import java.io.File
import java.io.IOError
import java.io.IOException
import java.util.Locale


class TdUtility private constructor() {
    private lateinit var client: Client

    private val _updates = MutableSharedFlow<TdApi.Object>(replay = 1)
    val updates = _updates.asSharedFlow()

    private fun createClient() {
        Client.setLogMessageHandler(0, null)

        try {
            val logs = File(ApplicationLoader.applicationContext.cacheDir, "tdlib")
            if (!logs.exists()) logs.mkdir()

            Client.execute(SetLogVerbosityLevel(0))
            Client.execute(
                SetLogStream(
                    LogStreamFile(
                        "${logs.absolutePath}/tdlib.log",
                        (1 shl 27).toLong(),
                        false
                    )
                )
            )
        } catch (_: Client.ExecutionException) {
            throw IOError(IOException("Write access to the current directory is required"))
        }

        val updateHandler = Client.ResultHandler { update ->
            CoroutineScope(Dispatchers.Default).launch {
                _updates.emit(update)
            }
        }

        client = Client.create(updateHandler, null, null)
    }

    fun getClient(): Client {
        if (!::client.isInitialized) {
            throw IllegalStateException("Client is not initialized. Call LibUtility.initialize() first.")
        }
        return client
    }

    companion object {
        @Volatile
        private var instance: TdUtility? = null

        fun getInstance(): TdUtility {
            return instance ?: synchronized(this) {
                instance ?: TdUtility().also { instance = it }
            }
        }

        suspend fun initialize(): TdApi.AuthorizationState {
            val util = getInstance()
            if (util::client.isInitialized) {
                return util.getClient().execute(TdApi.GetAuthorizationState())
            }

            synchronized(this) {
                if (!util::client.isInitialized) {
                    util.createClient()
                }
            }

            val databaseDir = File(ApplicationLoader.Companion.applicationContext.filesDir, "data")
            val filesDir = File(ApplicationLoader.Companion.applicationContext.filesDir, "td_files")

            val params = TdApi.SetTdlibParameters().apply {
                apiHash = BuildConfig.APP_HASH
                apiId = BuildConfig.APP_ID
                applicationVersion = "${BuildConfig.VERSION_NAME}-${if (BuildConfig.DEBUG) "debug" else "prod"}"
                systemVersion = "Android ${Build.VERSION.SDK_INT}"
                deviceModel = "${Build.BRAND} ${Build.MODEL}"
                databaseDirectory = databaseDir.absolutePath
                filesDirectory = filesDir.absolutePath
                systemLanguageCode = Locale.getDefault().language
                useChatInfoDatabase = true
                useFileDatabase = true
                useSecretChats = true
                useMessageDatabase = true
                useTestDc = false
            }
            util.client.send(params, null)

            val firstUpdate = util.updates.first { it is TdApi.UpdateAuthorizationState } as TdApi.UpdateAuthorizationState
            return firstUpdate.authorizationState
        }
    }
}