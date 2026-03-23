package com.f0x1d.logfox.feature.preferences.presentation.service

import com.f0x1d.logfox.feature.terminals.api.base.TerminalType

internal sealed interface PreferencesServiceCommand {
    data object Load : PreferencesServiceCommand

    data class TerminalSelected(val type: TerminalType) : PreferencesServiceCommand
    data class FallbackToDefaultChanged(val enabled: Boolean) : PreferencesServiceCommand
    data class StartOnBootChanged(val enabled: Boolean) : PreferencesServiceCommand
    data class StopLoggingOnBackExitChanged(val enabled: Boolean) : PreferencesServiceCommand
    data class ShowLogsFromAppLaunchChanged(val enabled: Boolean) : PreferencesServiceCommand
    data class IncludeDeviceInfoChanged(val enabled: Boolean) : PreferencesServiceCommand
    data class IncludeAppInfoChanged(val enabled: Boolean) : PreferencesServiceCommand
    data class ExportLogsAsTxtChanged(val enabled: Boolean) : PreferencesServiceCommand
    data object ConfirmRestartLogging : PreferencesServiceCommand

    // Commands from effect handler
    data class PreferencesLoaded(
        val selectedTerminalType: TerminalType,
        val terminalNames: List<String>,
        val fallbackToDefault: Boolean,
        val stopLoggingOnBackExit: Boolean,
        val startOnBoot: Boolean,
        val showLogsFromAppLaunch: Boolean,
        val includeDeviceInfo: Boolean,
        val includeAppInfo: Boolean,
        val exportLogsAsTxt: Boolean,
    ) : PreferencesServiceCommand

    data class TerminalSupported(val type: TerminalType) : PreferencesServiceCommand
    data object TerminalNotSupported : PreferencesServiceCommand
}
