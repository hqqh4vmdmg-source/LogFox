package com.f0x1d.logfox.feature.crashes.presentation.details

import com.f0x1d.logfox.core.tea.EffectHandler
import com.f0x1d.logfox.feature.crashes.api.domain.CheckAppDisabledUseCase
import com.f0x1d.logfox.feature.crashes.api.domain.DeleteCrashUseCase
import com.f0x1d.logfox.feature.crashes.api.domain.ExportCrashToFileUseCase
import com.f0x1d.logfox.feature.crashes.api.domain.ExportCrashToZipUseCase
import com.f0x1d.logfox.feature.crashes.api.domain.GetCrashAndLogByIdFlowUseCase
import com.f0x1d.logfox.feature.crashes.presentation.details.di.CrashId
import com.f0x1d.logfox.feature.datetime.api.DateTimeFormatter
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.GetUseSeparateNotificationsChannelsForCrashesFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.GetWrapCrashLogLinesFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.SetWrapCrashLogLinesUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.service.GetExportLogsAsTxtUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

internal class CrashDetailsEffectHandler @Inject constructor(
    @CrashId private val crashId: Long,
    private val getCrashAndLogByIdFlowUseCase: GetCrashAndLogByIdFlowUseCase,
    private val deleteCrashUseCase: DeleteCrashUseCase,
    private val checkAppDisabledUseCase: CheckAppDisabledUseCase,
    private val exportCrashToFileUseCase: ExportCrashToFileUseCase,
    private val exportCrashToZipUseCase: ExportCrashToZipUseCase,
    private val getWrapCrashLogLinesFlowUseCase: GetWrapCrashLogLinesFlowUseCase,
    private val setWrapCrashLogLinesUseCase: SetWrapCrashLogLinesUseCase,
    private val getUseSeparateNotificationsChannelsForCrashesFlowUseCase: GetUseSeparateNotificationsChannelsForCrashesFlowUseCase,
    private val getExportLogsAsTxtUseCase: GetExportLogsAsTxtUseCase,
    private val dateTimeFormatter: DateTimeFormatter,
) : EffectHandler<CrashDetailsSideEffect, CrashDetailsCommand> {
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun handle(
        effect: CrashDetailsSideEffect,
        onCommand: suspend (CrashDetailsCommand) -> Unit,
    ) {
        when (effect) {
            is CrashDetailsSideEffect.LoadCrash -> getCrashAndLogByIdFlowUseCase(crashId)
                .collect { value ->
                    value?.let { (crash, crashLog) ->
                        onCommand(CrashDetailsCommand.CrashLoaded(crash, crashLog))
                    }
                }

            is CrashDetailsSideEffect.SetWrapCrashLogLines -> setWrapCrashLogLinesUseCase(effect.wrap)

            is CrashDetailsSideEffect.ObservePreferences -> combine(
                getWrapCrashLogLinesFlowUseCase(),
                getUseSeparateNotificationsChannelsForCrashesFlowUseCase(),
            ) { wrapCrashLogLines, useSeparateNotificationsChannelsForCrashes ->
                CrashDetailsCommand.PreferencesUpdated(
                    wrapCrashLogLines = wrapCrashLogLines,
                    useSeparateNotificationsChannelsForCrashes = useSeparateNotificationsChannelsForCrashes,
                )
            }.collect(onCommand)

            is CrashDetailsSideEffect.PrepareFileExport -> onCommand(
                CrashDetailsCommand.FileExportPickerReady(
                    exportFilename(
                        effect.packageName,
                        effect.dateAndTime,
                        if (getExportLogsAsTxtUseCase()) "txt" else "log",
                    ),
                ),
            )

            is CrashDetailsSideEffect.PrepareZipExport -> onCommand(
                CrashDetailsCommand.ZipExportPickerReady(
                    exportFilename(effect.packageName, effect.dateAndTime, "zip"),
                ),
            )

            is CrashDetailsSideEffect.ExportCrashToZip -> exportCrashToZipUseCase(crashId, effect.uri)

            is CrashDetailsSideEffect.ExportCrashToFile -> exportCrashToFileUseCase(crashId, effect.uri)

            is CrashDetailsSideEffect.ChangeBlacklist -> checkAppDisabledUseCase(effect.appCrash.packageName)

            is CrashDetailsSideEffect.DeleteCrash -> deleteCrashUseCase(effect.appCrash.id)

            // UI side effects - handled by Fragment
            is CrashDetailsSideEffect.OpenAppInfo,
            is CrashDetailsSideEffect.OpenNotificationSettings,
            is CrashDetailsSideEffect.ConfirmBlacklist,
            is CrashDetailsSideEffect.ConfirmDelete,
            is CrashDetailsSideEffect.CopyText,
            is CrashDetailsSideEffect.ShareCrashLog,
            is CrashDetailsSideEffect.Close,
            is CrashDetailsSideEffect.LaunchFileExportPicker,
            is CrashDetailsSideEffect.LaunchZipExportPicker -> Unit
        }
    }

    private fun exportFilename(packageName: String, dateAndTime: Long, extension: String): String {
        val pkg = packageName.replace(".", "-")
        val formattedDate = dateTimeFormatter.formatForExport(dateAndTime)
        return "crash-$pkg-$formattedDate.$extension"
    }
}
