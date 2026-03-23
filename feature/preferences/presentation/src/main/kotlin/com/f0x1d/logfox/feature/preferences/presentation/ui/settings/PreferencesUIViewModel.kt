package com.f0x1d.logfox.feature.preferences.presentation.ui.settings

import com.f0x1d.logfox.core.tea.BaseStoreViewModel
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.GetOpenCrashesOnStartupUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.GetWrapCrashLogLinesUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.datetime.GetDateFormatUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.datetime.GetTimeFormatUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetExportLogsInOriginalFormatUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetLogsDisplayLimitUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetLogsExpandedUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetLogsTextSizeUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetLogsUpdateIntervalUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetResumeLoggingWithBottomTouchUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetShowLogContentUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetShowLogDateUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetShowLogPackageUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetShowLogPidUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetShowLogTagUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetShowLogTidUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetShowLogTimeUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetShowLogUidUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.ui.GetMonetEnabledUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.ui.GetNightThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class PreferencesUIViewModel @Inject constructor(
    reducer: PreferencesUIReducer,
    effectHandler: PreferencesUIEffectHandler,
    viewStateMapper: PreferencesUIViewStateMapper,
    getNightThemeUseCase: GetNightThemeUseCase,
    getMonetEnabledUseCase: GetMonetEnabledUseCase,
    getDateFormatUseCase: GetDateFormatUseCase,
    getTimeFormatUseCase: GetTimeFormatUseCase,
    getOpenCrashesOnStartupUseCase: GetOpenCrashesOnStartupUseCase,
    getShowLogDateUseCase: GetShowLogDateUseCase,
    getShowLogTimeUseCase: GetShowLogTimeUseCase,
    getShowLogUidUseCase: GetShowLogUidUseCase,
    getShowLogPidUseCase: GetShowLogPidUseCase,
    getShowLogTidUseCase: GetShowLogTidUseCase,
    getShowLogPackageUseCase: GetShowLogPackageUseCase,
    getShowLogTagUseCase: GetShowLogTagUseCase,
    getShowLogContentUseCase: GetShowLogContentUseCase,
    getExportLogsInOriginalFormatUseCase: GetExportLogsInOriginalFormatUseCase,
    getWrapCrashLogLinesUseCase: GetWrapCrashLogLinesUseCase,
    getLogsUpdateIntervalUseCase: GetLogsUpdateIntervalUseCase,
    getLogsTextSizeUseCase: GetLogsTextSizeUseCase,
    getLogsDisplayLimitUseCase: GetLogsDisplayLimitUseCase,
    getLogsExpandedUseCase: GetLogsExpandedUseCase,
    getResumeLoggingWithBottomTouchUseCase: GetResumeLoggingWithBottomTouchUseCase,
) : BaseStoreViewModel<PreferencesUIViewState, PreferencesUIState, PreferencesUICommand, PreferencesUISideEffect>(
    initialState = PreferencesUIState(
        nightTheme = getNightThemeUseCase(),
        monetEnabled = getMonetEnabledUseCase(),
        dateFormat = getDateFormatUseCase(),
        timeFormat = getTimeFormatUseCase(),
        openCrashesOnStartup = getOpenCrashesOnStartupUseCase(),
        showLogDate = getShowLogDateUseCase(),
        showLogTime = getShowLogTimeUseCase(),
        showLogUid = getShowLogUidUseCase(),
        showLogPid = getShowLogPidUseCase(),
        showLogTid = getShowLogTidUseCase(),
        showLogPackage = getShowLogPackageUseCase(),
        showLogTag = getShowLogTagUseCase(),
        showLogContent = getShowLogContentUseCase(),
        exportLogsInOriginalFormat = getExportLogsInOriginalFormatUseCase(),
        wrapCrashLogLines = getWrapCrashLogLinesUseCase(),
        logsUpdateInterval = getLogsUpdateIntervalUseCase(),
        logsTextSize = getLogsTextSizeUseCase(),
        logsDisplayLimit = getLogsDisplayLimitUseCase(),
        logsExpanded = getLogsExpandedUseCase(),
        resumeLogsWithTouch = getResumeLoggingWithBottomTouchUseCase(),
    ),
    reducer = reducer,
    effectHandlers = listOf(effectHandler),
    viewStateMapper = viewStateMapper,
    initialSideEffects = listOf(PreferencesUISideEffect.LoadPreferences),
)
