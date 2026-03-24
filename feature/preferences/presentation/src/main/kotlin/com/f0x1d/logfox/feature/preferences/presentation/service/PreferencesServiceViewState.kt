package com.f0x1d.logfox.feature.preferences.presentation.service

import androidx.compose.runtime.Immutable
import com.f0x1d.logfox.feature.terminals.api.base.TerminalType

@Immutable
internal data class PreferencesServiceViewState(
    val selectedTerminalType: TerminalType,
    val terminalNames: List<String>,
    val fallbackToDefault: Boolean,
    val stopLoggingOnBackExit: Boolean,
    val startOnBoot: Boolean,
    val showLogsFromAppLaunch: Boolean,
    val includeDeviceInfo: Boolean,
    val includeAppInfo: Boolean,
    val exportLogsAsTxt: Boolean,
)
