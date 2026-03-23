package com.f0x1d.logfox.feature.preferences.presentation.service

import com.f0x1d.logfox.feature.terminals.api.base.TerminalType

internal data class PreferencesServiceState(
    val selectedTerminalType: TerminalType,
    val terminalNames: List<String>,
    val fallbackToDefault: Boolean = true,
    val stopLoggingOnBackExit: Boolean = false,
    val startOnBoot: Boolean = true,
    val showLogsFromAppLaunch: Boolean = true,
    val includeDeviceInfo: Boolean = true,
    val includeAppInfo: Boolean = true,
    val exportLogsAsTxt: Boolean = false,
)
