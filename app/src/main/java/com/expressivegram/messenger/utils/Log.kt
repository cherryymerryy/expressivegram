package com.expressivegram.messenger.utils

import android.text.TextUtils
import android.util.Log

class Log {
    companion object {
        private const val DEFAULT_TAG: String = "[expressivegram]"
        private const val DEFAULT_MSG: String = ""

        fun e(tr: Throwable, tag: String? = null, msg: String? = null) {
            Log.e(tag ?: DEFAULT_TAG, msg ?: DEFAULT_MSG, tr)
        }

        fun e(tr: Exception, tag: String? = null, msg: String? = null) {
            Log.e(tag ?: DEFAULT_TAG, msg ?: DEFAULT_MSG, tr)
        }

        fun e(tr: String, tag: String? = null, msg: String? = null) {
            if (TextUtils.isEmpty(tr)) {
                e(Throwable("tr value Can't be empty!"))
                return
            }

            println("${tag ?: DEFAULT_TAG}: ${msg ?: DEFAULT_MSG} $tr")
        }
    }
}