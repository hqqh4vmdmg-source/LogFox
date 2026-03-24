package com.f0x1d.logfox.core.context

import android.os.Build

internal class Device {

    private val data = buildString {
        appendLine("SDK: ${Build.VERSION.SDK_INT}")
        appendLine("PRODUCT_NAME: ${Build.PRODUCT}")
        appendLine("DEVICE_NAME: ${Build.DEVICE}")
        appendLine("BOARD_NAME: ${Build.BOARD}")
        appendLine("SUPPORTED_ABIS: ${Build.SUPPORTED_ABIS.joinToString()}")
        appendLine("MANUFACTURER: ${Build.MANUFACTURER}")
        appendLine("BRAND: ${Build.BRAND}")
        append("MODEL: ${Build.MODEL}")
    }

    override fun toString() = data
}

val deviceData: String get() = Device().toString()
