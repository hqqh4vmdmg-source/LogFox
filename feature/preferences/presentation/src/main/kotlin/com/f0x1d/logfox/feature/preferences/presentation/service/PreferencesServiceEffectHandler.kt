package com.f0x1d.logfox.feature.preferences.presentation.service

import android.content.Context
import com.f0x1d.logfox.core.tea.EffectHandler
import com.f0x1d.logfox.feature.logging.api.presentation.LoggingServiceDelegate
import com.f0x1d.logfox.feature.preferences.api.data.ServiceSettingsRepository
import com.f0x1d.logfox.feature.preferences.api.data.TerminalSettingsRepository
import com.f0x1d.logfox.feature.preferences.api.domain.terminal.GetSelectedTerminalTypeFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.terminal.SetSelectedTerminalTypeUseCase
import com.f0x1d.logfox.feature.terminals.api.base.Terminal
import com.f0x1d.logfox.feature.terminals.api.base.TerminalType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class PreferencesServiceEffectHandler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getSelectedTerminalTypeFlowUseCase: GetSelectedTerminalTypeFlowUseCase,
    private val setSelectedTerminalTypeUseCase: SetSelectedTerminalTypeUseCase,
    private val terminals: Map<TerminalType, @JvmSuppressWildcards Terminal>,
    private val loggingServiceDelegate: LoggingServiceDelegate,
    private val serviceSettingsRepository: ServiceSettingsRepository,
    private val terminalSettingsRepository: TerminalSettingsRepository,
) : EffectHandler<PreferencesServiceSideEffect, PreferencesServiceCommand> {

    override suspend fun handle(
        effect: PreferencesServiceSideEffect,
        onCommand: suspend (PreferencesServiceCommand) -> Unit,
    ) {
        when (effect) {
            is PreferencesServiceSideEffect.LoadPreferences -> {
                getSelectedTerminalTypeFlowUseCase().collect { selectedType ->
                    onCommand(
                        PreferencesServiceCommand.PreferencesLoaded(
                            selectedTerminalType = selectedType,
                            terminalNames = TerminalType.entries.map { type ->
                                context.getString(terminals.getValue(type).title)
                            },
                            fallbackToDefault = terminalSettingsRepository.fallbackToDefaultTerminal().value,
                            stopLoggingOnBackExit = serviceSettingsRepository.stopLoggingOnBackExit().value,
                            startOnBoot = serviceSettingsRepository.startOnBoot().value,
                            showLogsFromAppLaunch = serviceSettingsRepository.showLogsFromAppLaunch().value,
                            includeDeviceInfo = serviceSettingsRepository.includeDeviceInfoInArchives().value,
                            includeAppInfo = serviceSettingsRepository.includeAppInfoInExports().value,
                            exportLogsAsTxt = serviceSettingsRepository.exportLogsAsTxt().value,
                        ),
                    )
                }
            }

            is PreferencesServiceSideEffect.CheckTerminalSupport -> onCommand(
                if (terminals.getValue(effect.type).isSupported()) {
                    PreferencesServiceCommand.TerminalSupported(effect.type)
                } else {
                    PreferencesServiceCommand.TerminalNotSupported
                },
            )

            is PreferencesServiceSideEffect.SaveTerminalType -> setSelectedTerminalTypeUseCase(effect.type)

            is PreferencesServiceSideEffect.SaveFallbackToDefault ->
                terminalSettingsRepository.fallbackToDefaultTerminal().set(effect.enabled)

            is PreferencesServiceSideEffect.SaveStartOnBoot ->
                serviceSettingsRepository.startOnBoot().set(effect.enabled)

            is PreferencesServiceSideEffect.SaveStopLoggingOnBackExit ->
                serviceSettingsRepository.stopLoggingOnBackExit().set(effect.enabled)

            is PreferencesServiceSideEffect.SaveShowLogsFromAppLaunch ->
                serviceSettingsRepository.showLogsFromAppLaunch().set(effect.enabled)

            is PreferencesServiceSideEffect.SaveIncludeDeviceInfo ->
                serviceSettingsRepository.includeDeviceInfoInArchives().set(effect.enabled)

            is PreferencesServiceSideEffect.SaveIncludeAppInfo ->
                serviceSettingsRepository.includeAppInfoInExports().set(effect.enabled)

            is PreferencesServiceSideEffect.SaveExportLogsAsTxt ->
                serviceSettingsRepository.exportLogsAsTxt().set(effect.enabled)

            is PreferencesServiceSideEffect.RestartLogging -> loggingServiceDelegate.restartLogging()

            // UI side effects - handled by Fragment
            is PreferencesServiceSideEffect.ShowTerminalRestartDialog -> Unit
            is PreferencesServiceSideEffect.ShowTerminalUnavailableToast -> Unit
            is PreferencesServiceSideEffect.ShowAndroid13WarningDialog -> Unit
        }
    }
}
