package com.f0x1d.logfox.feature.preferences.presentation.service

import com.f0x1d.logfox.core.compat.isAtLeastAndroid13
import com.f0x1d.logfox.core.tea.ReduceResult
import com.f0x1d.logfox.core.tea.Reducer
import com.f0x1d.logfox.core.tea.noSideEffects
import com.f0x1d.logfox.core.tea.withSideEffects
import com.f0x1d.logfox.feature.terminals.api.base.TerminalType
import javax.inject.Inject

internal class PreferencesServiceReducer @Inject constructor() : Reducer<PreferencesServiceState, PreferencesServiceCommand, PreferencesServiceSideEffect> {

    override fun reduce(
        state: PreferencesServiceState,
        command: PreferencesServiceCommand,
    ): ReduceResult<PreferencesServiceState, PreferencesServiceSideEffect> = when (command) {
        is PreferencesServiceCommand.Load -> state.withSideEffects(
            PreferencesServiceSideEffect.LoadPreferences,
        )

        is PreferencesServiceCommand.PreferencesLoaded -> state.copy(
            selectedTerminalType = command.selectedTerminalType,
            terminalNames = command.terminalNames,
            fallbackToDefault = command.fallbackToDefault,
            stopLoggingOnBackExit = command.stopLoggingOnBackExit,
            startOnBoot = command.startOnBoot,
            showLogsFromAppLaunch = command.showLogsFromAppLaunch,
            includeDeviceInfo = command.includeDeviceInfo,
            includeAppInfo = command.includeAppInfo,
            exportLogsAsTxt = command.exportLogsAsTxt,
        ).noSideEffects()

        is PreferencesServiceCommand.TerminalSelected -> if (state.selectedTerminalType == command.type) {
            // Same terminal selected, just restart logging
            state.withSideEffects(PreferencesServiceSideEffect.RestartLogging)
        } else {
            // Different terminal, check if it's supported
            state.withSideEffects(PreferencesServiceSideEffect.CheckTerminalSupport(command.type))
        }

        is PreferencesServiceCommand.TerminalSupported -> state.copy(
            selectedTerminalType = command.type,
        ).withSideEffects(
            PreferencesServiceSideEffect.SaveTerminalType(command.type),
            PreferencesServiceSideEffect.ShowTerminalRestartDialog,
        )

        is PreferencesServiceCommand.TerminalNotSupported -> state.withSideEffects(
            PreferencesServiceSideEffect.ShowTerminalUnavailableToast,
        )

        is PreferencesServiceCommand.FallbackToDefaultChanged -> state.copy(
            fallbackToDefault = command.enabled,
        ).withSideEffects(PreferencesServiceSideEffect.SaveFallbackToDefault(command.enabled))

        is PreferencesServiceCommand.StartOnBootChanged -> {
            val newState = state.copy(startOnBoot = command.enabled)
            val isDefaultTerminal = state.selectedTerminalType == TerminalType.Default
            if (isAtLeastAndroid13 && command.enabled && isDefaultTerminal) {
                newState.withSideEffects(
                    PreferencesServiceSideEffect.SaveStartOnBoot(command.enabled),
                    PreferencesServiceSideEffect.ShowAndroid13WarningDialog,
                )
            } else {
                newState.withSideEffects(PreferencesServiceSideEffect.SaveStartOnBoot(command.enabled))
            }
        }

        is PreferencesServiceCommand.StopLoggingOnBackExitChanged -> state.copy(
            stopLoggingOnBackExit = command.enabled,
        ).withSideEffects(PreferencesServiceSideEffect.SaveStopLoggingOnBackExit(command.enabled))

        is PreferencesServiceCommand.ShowLogsFromAppLaunchChanged -> {
            val newState = state.copy(showLogsFromAppLaunch = command.enabled)
            if (!command.enabled) {
                newState.withSideEffects(
                    PreferencesServiceSideEffect.SaveShowLogsFromAppLaunch(command.enabled),
                    PreferencesServiceSideEffect.RestartLogging,
                )
            } else {
                newState.withSideEffects(
                    PreferencesServiceSideEffect.SaveShowLogsFromAppLaunch(command.enabled),
                )
            }
        }

        is PreferencesServiceCommand.IncludeDeviceInfoChanged -> state.copy(
            includeDeviceInfo = command.enabled,
        ).withSideEffects(PreferencesServiceSideEffect.SaveIncludeDeviceInfo(command.enabled))

        is PreferencesServiceCommand.IncludeAppInfoChanged -> state.copy(
            includeAppInfo = command.enabled,
        ).withSideEffects(PreferencesServiceSideEffect.SaveIncludeAppInfo(command.enabled))

        is PreferencesServiceCommand.ExportLogsAsTxtChanged -> state.copy(
            exportLogsAsTxt = command.enabled,
        ).withSideEffects(PreferencesServiceSideEffect.SaveExportLogsAsTxt(command.enabled))

        is PreferencesServiceCommand.ConfirmRestartLogging -> state.withSideEffects(
            PreferencesServiceSideEffect.RestartLogging,
        )
    }
}
