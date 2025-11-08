package com.expressivegram.messenger.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtility {
    fun getDateFromUnix(timestamp: Int): Date {
        return Date(timestamp.toLong())
    }

    fun getFormattedDateFromUnix(timestamp: Int, formatPattern: String = "dd/M/yyyy hh:mm:ss a"): String {
        val date = getDateFromUnix(timestamp)
        val dateFormat = SimpleDateFormat(formatPattern, Locale.getDefault())
        return dateFormat.format(date)
    }

    fun getFormattedDate(date: Date, formatPattern: String = "dd/M/yyyy hh:mm:ss a"): String {
        val dateFormat = SimpleDateFormat(formatPattern, Locale.getDefault())
        return dateFormat.format(date)
    }
}