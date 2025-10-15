package com.expressivegram.messenger.utils

import android.os.Build
import com.expressivegram.messenger.ApplicationLoader
import com.expressivegram.messenger.BuildConfig
import com.expressivegram.messenger.TELEGRAM_DATABASE_PATH
import com.expressivegram.messenger.TELEGRAM_FILES_PATH
import com.expressivegram.messenger.TELEGRAM_PATH
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
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
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var client: Client

    private val _updates = MutableSharedFlow<TdApi.Object>()
    val updates = _updates.asSharedFlow()

    private val _authState = MutableStateFlow<TdApi.AuthorizationState?>(null)
    val authState = _authState.asStateFlow()

    init {
        scope.launch {
            _updates
                .filterIsInstance<TdApi.UpdateAuthorizationState>()
                .map { it.authorizationState }
                .collect { newState ->
                    Log.e("Received new auth state: ${newState.javaClass.simpleName}", "TdUtility")
                    _authState.value = newState
                }
        }
    }

    private fun createClient() {
        Client.setLogMessageHandler(0, null)

        try {
            val logs = File(ApplicationLoader.applicationContext.cacheDir, TELEGRAM_PATH)
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
            scope.launch {
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

        fun initialize() {
            val util = getInstance()
            if (util::client.isInitialized) {
                util.getClient().send(TdApi.GetAuthorizationState(), null)
                return
            }

            synchronized(this) {
                if (!util::client.isInitialized) {
                    Log.e("TdUtility", "Creating new TDLib client.")
                    util.createClient()
                }
            }

            val databaseDir = File(ApplicationLoader.applicationContext.filesDir, TELEGRAM_DATABASE_PATH)
            val filesDir = File(ApplicationLoader.applicationContext.filesDir, TELEGRAM_FILES_PATH)

            val params = TdApi.SetTdlibParameters().apply {
                apiHash = BuildConfig.APP_HASH
                apiId = BuildConfig.APP_ID
                applicationVersion = "(${BuildConfig.VERSION_CODE}) ${BuildConfig.VERSION_NAME}-${if (BuildConfig.DEBUG) "debug" else "prod"}"
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
        }
    }
}