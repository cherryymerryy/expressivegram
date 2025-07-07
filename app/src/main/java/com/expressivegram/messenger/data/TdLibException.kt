package com.expressivegram.messenger.data

import org.drinkless.tdlib.TdApi

data class TdLibException(val error: TdApi.Error) : Exception("TDLib error: ${error.code} ${error.message}")