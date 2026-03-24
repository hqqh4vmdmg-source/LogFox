package com.f0x1d.logfox.core.context

import android.os.Build

internal class Device {

    private val data = mapOf<String, String>(
        "SDK" to Build.VERSION.SDK_INT.toString(),
        "PRODUCT_NAME" to Build.PRODUCT,
        "DEVICE_NAME" to Build.DEVICE,
        "BOARD_NAME" to Build.BOARD,
        "SUPPORTED_ABIS" to Build.SUPPORTED_ABIS.joinToString(),
        "MANUFACTURER" to Build.MANUFACTURER,
        "BRAND" to Build.BRAND,
        "MODEL" to Build.MODEL,
    ).entries.joinToString(separator = "\n") { "${it.key}: ${it.value}" }

    override fun toString() = data
}

val deviceData: String get() = Device().toString()
