package com.expressivegram.messenger.extensions

import org.drinkless.tdlib.TdApi

fun TdApi.ChatPermissions.canSendAnyFiles(): Boolean {
    return canSendAudios || canSendDocuments || canSendPhotos || canSendVideos
}

fun TdApi.ChatAdministratorRights.canInteractWithMessages(): Boolean {
    return canPostMessages || canEditMessages
}