package com.expressivegram.messenger.utils

import org.drinkless.tdlib.TdApi

class UserConfig {
    private var currentUser: TdApi.User? = null
    private var currentUserFullInfo: TdApi.UserFullInfo? = null
    private var folders: List<TdApi.ChatFolderInfo>? = null
    private var initialized: Boolean = currentUser != null && currentUserFullInfo != null

    companion object {
        @Volatile
        private var instance: UserConfig? = null

        fun getInstance(): UserConfig {
            return instance ?: synchronized(this) {
                instance ?: UserConfig().also { instance = it }
            }
        }

        suspend fun initialize() {
            val util = getInstance()

            val client = TdUtility.getInstance().getClient()
            val me = client.execute(TdApi.GetMe())

            if (me != null) {
                util.setCurrentUser(me)

                val meFullInfo = client.execute(TdApi.GetUserFullInfo(me.id))

                if (meFullInfo != null) {
                    util.setCurrentUserFullInfo(meFullInfo)
                }
            }
        }
    }

    fun isInitialized(): Boolean {
        return initialized
    }

    fun getCurrentUser(): TdApi.User? {
        return currentUser
    }

    fun setCurrentUser(user: TdApi.User): TdApi.User? {
        currentUser = user
        return currentUser
    }

    fun getCurrentUserFullInfo(): TdApi.UserFullInfo? {
        return currentUserFullInfo
    }

    fun setCurrentUserFullInfo(user: TdApi.UserFullInfo): TdApi.UserFullInfo? {
        currentUserFullInfo = user
        return currentUserFullInfo
    }

    fun getFolders(): List<TdApi.ChatFolderInfo>? {
        return folders
    }

    fun setFolders(folders: List<TdApi.ChatFolderInfo>): List<TdApi.ChatFolderInfo>? {
        this.folders = folders
        return this.folders
    }
}