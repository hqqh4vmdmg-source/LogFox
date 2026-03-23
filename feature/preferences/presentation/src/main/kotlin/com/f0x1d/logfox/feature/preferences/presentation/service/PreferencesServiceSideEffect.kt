package com.f0x1d.logfox.feature.preferences.presentation.service

import com.f0x1d.logfox.feature.terminals.api.base.TerminalType

internal sealed interface PreferencesServiceSideEffect {
    // Business logic side effects - handled by EffectHandler
    data object LoadPreferences : PreferencesServiceSideEffect
    data class CheckTerminalSupport(val type: TerminalType) : PreferencesServiceSideEffect
    data class SaveTerminalType(val type: TerminalType) : PreferencesServiceSideEffect
    data class SaveFallbackToDefault(val enabled: Boolean) : PreferencesServiceSideEffect
    data class SaveStartOnBoot(val enabled: Boolean) : PreferencesServiceSideEffect
    data class SaveStopLoggingOnBackExit(val enabled: Boolean) : PreferencesServiceSideEffect
    data class SaveShowLogsFromAppLaunch(val enabled: Boolean) : PreferencesServiceSideEffect
    data class SaveIncludeDeviceInfo(val enabled: Boolean) : PreferencesServiceSideEffect
    data class SaveIncludeAppInfo(val enabled: Boolean) : PreferencesServiceSideEffect
    data class SaveExportLogsAsTxt(val enabled: Boolean) : PreferencesServiceSideEffect
    data object RestartLogging : PreferencesServiceSideEffect

    // UI side effects - handled by Fragment
    data object ShowTerminalRestartDialog : PreferencesServiceSideEffect
    data object ShowTerminalUnavailableToast : PreferencesServiceSideEffect
    data object ShowAndroid13WarningDialog : PreferencesServiceSideEffect
}
