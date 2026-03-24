package com.f0x1d.logfox.feature.preferences.presentation.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import com.f0x1d.logfox.core.tea.EffectHandler
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.GetOpenCrashesOnStartupFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.GetWrapCrashLogLinesFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.SetOpenCrashesOnStartupUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.SetWrapCrashLogLinesUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.datetime.GetDateFormatFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.datetime.GetTimeFormatFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.datetime.SetDateFormatUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.datetime.SetTimeFormatUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetExportLogsInOriginalFormatFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetLogsDisplayLimitFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetLogsExpandedFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetLogsTextSizeFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetLogsUpdateIntervalFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetResumeLoggingWithBottomTouchFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetShowLogContentFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetShowLogDateFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetShowLogPackageFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetShowLogPidFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetShowLogTagFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetShowLogTidFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetShowLogTimeFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.GetShowLogUidFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.SetExportLogsInOriginalFormatUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.SetLogsDisplayLimitUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.SetLogsExpandedUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.SetLogsTextSizeUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.SetLogsUpdateIntervalUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.SetResumeLoggingWithBottomTouchUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.SetShowLogContentUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.SetShowLogDateUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.SetShowLogPackageUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.SetShowLogPidUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.SetShowLogTagUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.SetShowLogTidUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.SetShowLogTimeUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.logs.SetShowLogUidUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.ui.GetMonetEnabledFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.ui.GetNightThemeFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.ui.SetMonetEnabledUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.ui.SetNightThemeUseCase
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

// Private data classes to hold typed groups of combined flows
private data class ThemeAndDatePrefs(
    val nightTheme: Int,
    val monetEnabled: Boolean,
    val dateFormat: String,
    val timeFormat: String,
    val openCrashesOnStartup: Boolean,
)

private data class ShowLogFirstColumns(
    val showDate: Boolean,
    val showTime: Boolean,
    val showUid: Boolean,
    val showPid: Boolean,
    val showTid: Boolean,
)

private data class ShowLogSecondColumns(
    val showPackage: Boolean,
    val showTag: Boolean,
    val showContent: Boolean,
    val exportInOriginalFormat: Boolean,
    val wrapCrashLogLines: Boolean,
)

private data class LogsBehaviourPrefs(
    val updateInterval: Long,
    val textSize: Int,
    val displayLimit: Int,
    val expanded: Boolean,
    val resumeWithTouch: Boolean,
)

internal class PreferencesUIEffectHandler @Inject constructor(
    private val getNightThemeFlowUseCase: GetNightThemeFlowUseCase,
    private val setNightThemeUseCase: SetNightThemeUseCase,
    private val getMonetEnabledFlowUseCase: GetMonetEnabledFlowUseCase,
    private val setMonetEnabledUseCase: SetMonetEnabledUseCase,
    private val getDateFormatFlowUseCase: GetDateFormatFlowUseCase,
    private val setDateFormatUseCase: SetDateFormatUseCase,
    private val getTimeFormatFlowUseCase: GetTimeFormatFlowUseCase,
    private val setTimeFormatUseCase: SetTimeFormatUseCase,
    private val getOpenCrashesOnStartupFlowUseCase: GetOpenCrashesOnStartupFlowUseCase,
    private val setOpenCrashesOnStartupUseCase: SetOpenCrashesOnStartupUseCase,
    private val getShowLogDateFlowUseCase: GetShowLogDateFlowUseCase,
    private val setShowLogDateUseCase: SetShowLogDateUseCase,
    private val getShowLogTimeFlowUseCase: GetShowLogTimeFlowUseCase,
    private val setShowLogTimeUseCase: SetShowLogTimeUseCase,
    private val getShowLogUidFlowUseCase: GetShowLogUidFlowUseCase,
    private val setShowLogUidUseCase: SetShowLogUidUseCase,
    private val getShowLogPidFlowUseCase: GetShowLogPidFlowUseCase,
    private val setShowLogPidUseCase: SetShowLogPidUseCase,
    private val getShowLogTidFlowUseCase: GetShowLogTidFlowUseCase,
    private val setShowLogTidUseCase: SetShowLogTidUseCase,
    private val getShowLogPackageFlowUseCase: GetShowLogPackageFlowUseCase,
    private val setShowLogPackageUseCase: SetShowLogPackageUseCase,
    private val getShowLogTagFlowUseCase: GetShowLogTagFlowUseCase,
    private val setShowLogTagUseCase: SetShowLogTagUseCase,
    private val getShowLogContentFlowUseCase: GetShowLogContentFlowUseCase,
    private val setShowLogContentUseCase: SetShowLogContentUseCase,
    private val getExportLogsInOriginalFormatFlowUseCase: GetExportLogsInOriginalFormatFlowUseCase,
    private val setExportLogsInOriginalFormatUseCase: SetExportLogsInOriginalFormatUseCase,
    private val getWrapCrashLogLinesFlowUseCase: GetWrapCrashLogLinesFlowUseCase,
    private val setWrapCrashLogLinesUseCase: SetWrapCrashLogLinesUseCase,
    private val getLogsUpdateIntervalFlowUseCase: GetLogsUpdateIntervalFlowUseCase,
    private val setLogsUpdateIntervalUseCase: SetLogsUpdateIntervalUseCase,
    private val getLogsTextSizeFlowUseCase: GetLogsTextSizeFlowUseCase,
    private val setLogsTextSizeUseCase: SetLogsTextSizeUseCase,
    private val getLogsDisplayLimitFlowUseCase: GetLogsDisplayLimitFlowUseCase,
    private val setLogsDisplayLimitUseCase: SetLogsDisplayLimitUseCase,
    private val getLogsExpandedFlowUseCase: GetLogsExpandedFlowUseCase,
    private val setLogsExpandedUseCase: SetLogsExpandedUseCase,
    private val getResumeLoggingWithBottomTouchFlowUseCase: GetResumeLoggingWithBottomTouchFlowUseCase,
    private val setResumeLoggingWithBottomTouchUseCase: SetResumeLoggingWithBottomTouchUseCase,
) : EffectHandler<PreferencesUISideEffect, PreferencesUICommand> {

    override suspend fun handle(
        effect: PreferencesUISideEffect,
        onCommand: suspend (PreferencesUICommand) -> Unit,
    ) {
        when (effect) {
            is PreferencesUISideEffect.LoadPreferences -> {
                combine(
                    combine(
                        getNightThemeFlowUseCase(),
                        getMonetEnabledFlowUseCase(),
                        getDateFormatFlowUseCase(),
                        getTimeFormatFlowUseCase(),
                        getOpenCrashesOnStartupFlowUseCase(),
                    ) { nightTheme, monetEnabled, dateFormat, timeFormat, openCrashesOnStartup ->
                        ThemeAndDatePrefs(nightTheme, monetEnabled, dateFormat, timeFormat, openCrashesOnStartup)
                    },
                    combine(
                        getShowLogDateFlowUseCase(),
                        getShowLogTimeFlowUseCase(),
                        getShowLogUidFlowUseCase(),
                        getShowLogPidFlowUseCase(),
                        getShowLogTidFlowUseCase(),
                    ) { date, time, uid, pid, tid ->
                        ShowLogFirstColumns(date, time, uid, pid, tid)
                    },
                    combine(
                        getShowLogPackageFlowUseCase(),
                        getShowLogTagFlowUseCase(),
                        getShowLogContentFlowUseCase(),
                        getExportLogsInOriginalFormatFlowUseCase(),
                        getWrapCrashLogLinesFlowUseCase(),
                    ) { pkg, tag, content, exportOriginal, wrapLines ->
                        ShowLogSecondColumns(pkg, tag, content, exportOriginal, wrapLines)
                    },
                    combine(
                        getLogsUpdateIntervalFlowUseCase(),
                        getLogsTextSizeFlowUseCase(),
                        getLogsDisplayLimitFlowUseCase(),
                        getLogsExpandedFlowUseCase(),
                        getResumeLoggingWithBottomTouchFlowUseCase(),
                    ) { updateInterval, textSize, displayLimit, expanded, resumeWithTouch ->
                        LogsBehaviourPrefs(updateInterval, textSize, displayLimit, expanded, resumeWithTouch)
                    },
                ) { themeDate, showFirst, showSecond, logsBehaviour ->
                    PreferencesUICommand.PreferencesLoaded(
                        nightTheme = themeDate.nightTheme,
                        monetEnabled = themeDate.monetEnabled,
                        dateFormat = themeDate.dateFormat,
                        timeFormat = themeDate.timeFormat,
                        openCrashesOnStartup = themeDate.openCrashesOnStartup,
                        showLogDate = showFirst.showDate,
                        showLogTime = showFirst.showTime,
                        showLogUid = showFirst.showUid,
                        showLogPid = showFirst.showPid,
                        showLogTid = showFirst.showTid,
                        showLogPackage = showSecond.showPackage,
                        showLogTag = showSecond.showTag,
                        showLogContent = showSecond.showContent,
                        exportLogsInOriginalFormat = showSecond.exportInOriginalFormat,
                        wrapCrashLogLines = showSecond.wrapCrashLogLines,
                        logsUpdateInterval = logsBehaviour.updateInterval,
                        logsTextSize = logsBehaviour.textSize,
                        logsDisplayLimit = logsBehaviour.displayLimit,
                        logsExpanded = logsBehaviour.expanded,
                        resumeLogsWithTouch = logsBehaviour.resumeWithTouch,
                    )
                }.collect(onCommand)
            }

            is PreferencesUISideEffect.SaveNightTheme -> {
                val theme = if (effect.themeIndex == 0) {
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                } else {
                    effect.themeIndex
                }
                setNightThemeUseCase(theme)
                AppCompatDelegate.setDefaultNightMode(theme)
            }

            is PreferencesUISideEffect.SaveMonetEnabled -> setMonetEnabledUseCase(effect.enabled)

            is PreferencesUISideEffect.SaveDateFormat -> setDateFormatUseCase(effect.format)

            is PreferencesUISideEffect.SaveTimeFormat -> setTimeFormatUseCase(effect.format)

            is PreferencesUISideEffect.SaveOpenCrashesOnStartup -> setOpenCrashesOnStartupUseCase(effect.openOnStartup)

            is PreferencesUISideEffect.SaveLogsFormat -> when (effect.which) {
                0 -> setShowLogDateUseCase(effect.checked)
                1 -> setShowLogTimeUseCase(effect.checked)
                2 -> setShowLogUidUseCase(effect.checked)
                3 -> setShowLogPidUseCase(effect.checked)
                4 -> setShowLogTidUseCase(effect.checked)
                5 -> setShowLogPackageUseCase(effect.checked)
                6 -> setShowLogTagUseCase(effect.checked)
                7 -> setShowLogContentUseCase(effect.checked)
            }

            is PreferencesUISideEffect.SaveExportLogsInOriginalFormat ->
                setExportLogsInOriginalFormatUseCase(effect.inOriginalFormat)

            is PreferencesUISideEffect.SaveWrapCrashLogLines -> setWrapCrashLogLinesUseCase(effect.wrap)

            is PreferencesUISideEffect.SaveLogsUpdateInterval -> setLogsUpdateIntervalUseCase(effect.interval)

            is PreferencesUISideEffect.SaveLogsTextSize -> setLogsTextSizeUseCase(effect.size)

            is PreferencesUISideEffect.SaveLogsDisplayLimit -> setLogsDisplayLimitUseCase(effect.limit)

            is PreferencesUISideEffect.SaveLogsExpanded -> setLogsExpandedUseCase(effect.expanded)

            is PreferencesUISideEffect.SaveResumeLogsWithTouch ->
                setResumeLoggingWithBottomTouchUseCase(effect.resumeWithTouch)

            // UI side effects - handled by Fragment
            is PreferencesUISideEffect.RecreateActivity -> Unit
        }
    }
}
