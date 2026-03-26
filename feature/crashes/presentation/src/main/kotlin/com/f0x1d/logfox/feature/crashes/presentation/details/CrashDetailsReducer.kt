package com.f0x1d.logfox.feature.crashes.presentation.details

import com.f0x1d.logfox.core.tea.ReduceResult
import com.f0x1d.logfox.core.tea.Reducer
import com.f0x1d.logfox.core.tea.noSideEffects
import com.f0x1d.logfox.core.tea.withSideEffects
import com.f0x1d.logfox.feature.crashes.api.data.notificationChannelId
import java.util.Locale
import javax.inject.Inject

internal class CrashDetailsReducer @Inject constructor() :
    Reducer<CrashDetailsState, CrashDetailsCommand, CrashDetailsSideEffect> {

    override fun reduce(
        state: CrashDetailsState,
        command: CrashDetailsCommand,
    ): ReduceResult<CrashDetailsState, CrashDetailsSideEffect> = when (command) {
        is CrashDetailsCommand.CrashLoaded -> state.copy(
            crash = command.crash,
            crashLog = command.crashLog,
        ).noSideEffects()

        is CrashDetailsCommand.BlacklistStatusLoaded -> state.copy(
            blacklisted = command.blacklisted,
        ).noSideEffects()

        is CrashDetailsCommand.WrapLinesClicked -> state.withSideEffects(
            CrashDetailsSideEffect.SetWrapCrashLogLines(wrap = !state.wrapCrashLogLines),
        )

        is CrashDetailsCommand.PreferencesUpdated -> state.copy(
            wrapCrashLogLines = command.wrapCrashLogLines,
            useSeparateNotificationsChannelsForCrashes = command.useSeparateNotificationsChannelsForCrashes,
        ).noSideEffects()

        is CrashDetailsCommand.OpenAppInfoClicked -> state.crash?.let { appCrash ->
            state.withSideEffects(CrashDetailsSideEffect.OpenAppInfo(appCrash.packageName))
        } ?: state.noSideEffects()

        is CrashDetailsCommand.OpenNotificationSettingsClicked -> state.crash?.let { appCrash ->
            state.withSideEffects(
                CrashDetailsSideEffect.OpenNotificationSettings(appCrash.notificationChannelId),
            )
        } ?: state.noSideEffects()

        is CrashDetailsCommand.BlacklistClicked -> state.crash?.let { appCrash ->
            if (state.blacklisted == false) {
                state.withSideEffects(CrashDetailsSideEffect.ConfirmBlacklist)
            } else {
                state.withSideEffects(CrashDetailsSideEffect.ChangeBlacklist(appCrash))
            }
        } ?: state.noSideEffects()

        is CrashDetailsCommand.ConfirmBlacklist -> state.crash?.let { appCrash ->
            state.withSideEffects(CrashDetailsSideEffect.ChangeBlacklist(appCrash))
        } ?: state.noSideEffects()

        is CrashDetailsCommand.DeleteClicked -> state.withSideEffects(CrashDetailsSideEffect.ConfirmDelete)

        is CrashDetailsCommand.ConfirmDelete -> state.crash?.let { appCrash ->
            state.withSideEffects(
                CrashDetailsSideEffect.DeleteCrash(appCrash),
                CrashDetailsSideEffect.Close,
            )
        } ?: state.noSideEffects()

        is CrashDetailsCommand.ExportCrashToFileClicked -> {
            val appCrash = state.crash
            if (appCrash != null && state.crashLog != null) {
                state.withSideEffects(
                    CrashDetailsSideEffect.PrepareFileExport(
                        packageName = appCrash.packageName,
                        dateAndTime = appCrash.dateAndTime,
                    ),
                )
            } else {
                state.noSideEffects()
            }
        }

        is CrashDetailsCommand.ExportCrashToZipClicked -> state.crash?.let { appCrash ->
            state.withSideEffects(
                CrashDetailsSideEffect.PrepareZipExport(
                    packageName = appCrash.packageName,
                    dateAndTime = appCrash.dateAndTime,
                ),
            )
        } ?: state.noSideEffects()

        is CrashDetailsCommand.FileExportPickerReady -> state.withSideEffects(
            CrashDetailsSideEffect.LaunchFileExportPicker(filename = command.filename),
        )

        is CrashDetailsCommand.ZipExportPickerReady -> state.withSideEffects(
            CrashDetailsSideEffect.LaunchZipExportPicker(filename = command.filename),
        )

        is CrashDetailsCommand.ExportCrashToFile -> state.withSideEffects(
            CrashDetailsSideEffect.ExportCrashToFile(uri = command.uri),
        )

        is CrashDetailsCommand.ExportCrashToZip -> state.withSideEffects(
            CrashDetailsSideEffect.ExportCrashToZip(uri = command.uri),
        )

        is CrashDetailsCommand.CopyCrashLog -> state.crashLog?.let { crashLog ->
            state.withSideEffects(CrashDetailsSideEffect.CopyText(crashLog))
        } ?: state.noSideEffects()

        is CrashDetailsCommand.ShareCrashLog -> state.crashLog?.let { crashLog ->
            state.withSideEffects(CrashDetailsSideEffect.ShareCrashLog(crashLog))
        } ?: state.noSideEffects()

        is CrashDetailsCommand.SearchInLog -> {
            val query = command.query
            val crashLog = state.crashLog.orEmpty()
            val ranges = if (query.isNotEmpty()) {
                val lowerLog = crashLog.lowercase(Locale.ENGLISH)
                val lowerQuery = query.lowercase(Locale.ENGLISH)
                buildList {
                    var index = 0
                    while (true) {
                        index = lowerLog.indexOf(lowerQuery, index)
                        if (index == -1) break
                        add(index until index + lowerQuery.length)
                        index += lowerQuery.length
                    }
                }
            } else {
                emptyList()
            }
            state.copy(searchQuery = query, searchMatchRanges = ranges).noSideEffects()
        }
    }
}
