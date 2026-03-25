package com.f0x1d.logfox.feature.crashes.impl.data

internal data class AppInfo(
    val appName: String?,
    val packageName: String,
    val versionName: String?,
    val versionCode: Long?,
) {

    fun format(): String = buildString {
        appName?.let { appendLine("APP_NAME: $it") }
        appendLine("PACKAGE: $packageName")
        versionName?.let { appendLine("VERSION_NAME: $it") }
        versionCode?.let { append("VERSION_CODE: $it") }
    }.trimEnd()
}
