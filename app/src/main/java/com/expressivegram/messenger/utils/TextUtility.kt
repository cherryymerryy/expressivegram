package com.expressivegram.messenger.utils

fun getInitials(name: String): String {
    val parts = name.trim().split(" ")
    return when {
        parts.size >= 2 -> "${parts.first().firstOrNull()?.uppercase() ?: ""}${parts.last().firstOrNull()?.uppercase() ?: ""}"
        parts.first().isNotEmpty() -> parts.first().firstOrNull()?.uppercase() ?: ""
        else -> ""
    }
}