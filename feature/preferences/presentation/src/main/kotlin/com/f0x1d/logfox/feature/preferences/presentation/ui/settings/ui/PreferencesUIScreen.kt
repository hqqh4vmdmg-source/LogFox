package com.f0x1d.logfox.feature.preferences.presentation.ui.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.f0x1d.logfox.compose.designsystem.component.button.NavigationBackButton
import com.f0x1d.logfox.core.compat.monetAvailable
import com.f0x1d.logfox.feature.preferences.presentation.ui.components.SettingsCategoryHeader
import com.f0x1d.logfox.feature.preferences.presentation.ui.components.SettingsClickableRow
import com.f0x1d.logfox.feature.preferences.presentation.ui.components.SettingsSwitchRow
import com.f0x1d.logfox.feature.preferences.presentation.ui.settings.PreferencesUICommand
import com.f0x1d.logfox.feature.preferences.presentation.ui.settings.PreferencesUIViewState
import com.f0x1d.logfox.feature.strings.Strings

private enum class UIDialog {
    NIGHT_THEME,
    DATE_FORMAT,
    TIME_FORMAT,
    LOGS_FORMAT,
    LOGS_UPDATE_INTERVAL,
    LOGS_TEXT_SIZE,
    LOGS_DISPLAY_LIMIT,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PreferencesUIScreen(
    state: PreferencesUIViewState,
    onBack: () -> Unit,
    onCommand: (PreferencesUICommand) -> Unit,
) {
    var currentDialog by remember { mutableStateOf<UIDialog?>(null) }
    var dialogText by remember { mutableStateOf("") }

    val themeOptions = listOf(
        stringResource(Strings.follow_system),
        stringResource(Strings.light),
        stringResource(Strings.dark),
    )

    val logsFormatLabels = listOf(
        stringResource(Strings.date),
        stringResource(Strings.time),
        stringResource(Strings.uid),
        stringResource(Strings.pid),
        stringResource(Strings.tid),
        stringResource(Strings.package_name),
        stringResource(Strings.tag),
        stringResource(Strings.content),
    )

    val logsFormatChecked = remember(state) {
        mutableStateListOf(
            state.showLogDate,
            state.showLogTime,
            state.showLogUid,
            state.showLogPid,
            state.showLogTid,
            state.showLogPackage,
            state.showLogTag,
            state.showLogContent,
        )
    }

    when (currentDialog) {
        UIDialog.NIGHT_THEME -> {
            AlertDialog(
                onDismissRequest = { currentDialog = null },
                title = { Text(text = stringResource(Strings.night_theme)) },
                text = {
                    Column {
                        themeOptions.forEachIndexed { index, option ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                RadioButton(
                                    selected = state.nightTheme.coerceAtLeast(0) == index,
                                    onClick = {
                                        currentDialog = null
                                        onCommand(PreferencesUICommand.NightThemeChanged(index))
                                    },
                                )
                                Text(text = option)
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { currentDialog = null }) {
                        Text(text = stringResource(Strings.close))
                    }
                },
            )
        }

        UIDialog.DATE_FORMAT -> {
            AlertDialog(
                onDismissRequest = { currentDialog = null },
                title = { Text(text = stringResource(Strings.date_format)) },
                text = {
                    OutlinedTextField(
                        value = dialogText,
                        onValueChange = { dialogText = it },
                        label = { Text(text = stringResource(Strings.date_format)) },
                        singleLine = true,
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        currentDialog = null
                        onCommand(PreferencesUICommand.DateFormatChanged(dialogText.takeIf { it.isNotBlank() }))
                    }) {
                        Text(text = stringResource(android.R.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { currentDialog = null }) {
                        Text(text = stringResource(android.R.string.cancel))
                    }
                },
            )
        }

        UIDialog.TIME_FORMAT -> {
            AlertDialog(
                onDismissRequest = { currentDialog = null },
                title = { Text(text = stringResource(Strings.time_format)) },
                text = {
                    OutlinedTextField(
                        value = dialogText,
                        onValueChange = { dialogText = it },
                        label = { Text(text = stringResource(Strings.time_format)) },
                        singleLine = true,
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        currentDialog = null
                        onCommand(PreferencesUICommand.TimeFormatChanged(dialogText.takeIf { it.isNotBlank() }))
                    }) {
                        Text(text = stringResource(android.R.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { currentDialog = null }) {
                        Text(text = stringResource(android.R.string.cancel))
                    }
                },
            )
        }

        UIDialog.LOGS_FORMAT -> {
            AlertDialog(
                onDismissRequest = { currentDialog = null },
                title = { Text(text = stringResource(Strings.logs_format)) },
                text = {
                    Column {
                        logsFormatLabels.forEachIndexed { index, label ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Checkbox(
                                    checked = logsFormatChecked[index],
                                    onCheckedChange = { checked ->
                                        logsFormatChecked[index] = checked
                                        onCommand(PreferencesUICommand.LogsFormatChanged(index, checked))
                                    },
                                )
                                Text(text = label)
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { currentDialog = null }) {
                        Text(text = stringResource(Strings.close))
                    }
                },
            )
        }

        UIDialog.LOGS_UPDATE_INTERVAL -> {
            AlertDialog(
                onDismissRequest = { currentDialog = null },
                title = { Text(text = stringResource(Strings.logs_update_interval)) },
                text = {
                    OutlinedTextField(
                        value = dialogText,
                        onValueChange = { dialogText = it },
                        label = { Text(text = stringResource(Strings.in_ms)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        currentDialog = null
                        onCommand(PreferencesUICommand.LogsUpdateIntervalChanged(dialogText.toLongOrNull()))
                    }) {
                        Text(text = stringResource(android.R.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { currentDialog = null }) {
                        Text(text = stringResource(android.R.string.cancel))
                    }
                },
            )
        }

        UIDialog.LOGS_TEXT_SIZE -> {
            AlertDialog(
                onDismissRequest = { currentDialog = null },
                title = { Text(text = stringResource(Strings.logs_text_size)) },
                text = {
                    OutlinedTextField(
                        value = dialogText,
                        onValueChange = { dialogText = it },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        currentDialog = null
                        onCommand(PreferencesUICommand.LogsTextSizeChanged(dialogText.toIntOrNull()))
                    }) {
                        Text(text = stringResource(android.R.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { currentDialog = null }) {
                        Text(text = stringResource(android.R.string.cancel))
                    }
                },
            )
        }

        UIDialog.LOGS_DISPLAY_LIMIT -> {
            AlertDialog(
                onDismissRequest = { currentDialog = null },
                title = { Text(text = stringResource(Strings.logs_display_limit)) },
                text = {
                    OutlinedTextField(
                        value = dialogText,
                        onValueChange = { dialogText = it },
                        label = { Text(text = stringResource(Strings.lines)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        currentDialog = null
                        onCommand(PreferencesUICommand.LogsDisplayLimitChanged(dialogText.toIntOrNull()))
                    }) {
                        Text(text = stringResource(android.R.string.ok))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { currentDialog = null }) {
                        Text(text = stringResource(android.R.string.cancel))
                    }
                },
            )
        }

        null -> Unit
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(Strings.ui)) },
                navigationIcon = { NavigationBackButton(onClick = onBack) },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues,
        ) {
            item {
                SettingsCategoryHeader(title = stringResource(Strings.ui))
            }
            item {
                SettingsClickableRow(
                    title = stringResource(Strings.night_theme),
                    summary = themeOptions.getOrNull(state.nightTheme.coerceAtLeast(0))
                        ?: stringResource(Strings.follow_system),
                    onClick = {
                        currentDialog = UIDialog.NIGHT_THEME
                    },
                )
            }
            if (monetAvailable) {
                item {
                    SettingsSwitchRow(
                        title = stringResource(Strings.monet),
                        checked = state.monetEnabled,
                        onCheckedChange = { newValue ->
                            onCommand(PreferencesUICommand.MonetEnabledChanged(newValue))
                        },
                    )
                }
            }
            item {
                SettingsCategoryHeader(title = stringResource(Strings.date_time))
            }
            item {
                SettingsClickableRow(
                    title = stringResource(Strings.date_format),
                    summary = state.dateFormat,
                    onClick = {
                        dialogText = state.dateFormat
                        currentDialog = UIDialog.DATE_FORMAT
                    },
                )
            }
            item {
                SettingsClickableRow(
                    title = stringResource(Strings.time_format),
                    summary = state.timeFormat,
                    onClick = {
                        dialogText = state.timeFormat
                        currentDialog = UIDialog.TIME_FORMAT
                    },
                )
            }
            item {
                SettingsCategoryHeader(title = stringResource(Strings.logs))
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.open_crashes_page_on_startup),
                    checked = state.openCrashesOnStartup,
                    onCheckedChange = { newValue ->
                        onCommand(PreferencesUICommand.OpenCrashesOnStartupChanged(newValue))
                    },
                )
            }
            item {
                SettingsClickableRow(
                    title = stringResource(Strings.logs_format),
                    onClick = { currentDialog = UIDialog.LOGS_FORMAT },
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.export_logs_in_original_format),
                    checked = state.exportLogsInOriginalFormat,
                    onCheckedChange = { newValue ->
                        onCommand(PreferencesUICommand.ExportLogsInOriginalFormatChanged(newValue))
                    },
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.wrap_log_lines_in_details),
                    checked = state.wrapCrashLogLines,
                    onCheckedChange = { newValue ->
                        onCommand(PreferencesUICommand.WrapCrashLogLinesChanged(newValue))
                    },
                )
            }
            item {
                SettingsClickableRow(
                    title = stringResource(Strings.logs_update_interval),
                    summary = state.logsUpdateInterval.toString(),
                    onClick = {
                        dialogText = state.logsUpdateInterval.toString()
                        currentDialog = UIDialog.LOGS_UPDATE_INTERVAL
                    },
                )
            }
            item {
                SettingsClickableRow(
                    title = stringResource(Strings.logs_text_size),
                    summary = state.logsTextSize.toString(),
                    onClick = {
                        dialogText = state.logsTextSize.toString()
                        currentDialog = UIDialog.LOGS_TEXT_SIZE
                    },
                )
            }
            item {
                SettingsClickableRow(
                    title = stringResource(Strings.logs_display_limit),
                    summary = state.logsDisplayLimit.toString(),
                    onClick = {
                        dialogText = state.logsDisplayLimit.toString()
                        currentDialog = UIDialog.LOGS_DISPLAY_LIMIT
                    },
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.expanded_logs),
                    checked = state.logsExpanded,
                    onCheckedChange = { newValue ->
                        onCommand(PreferencesUICommand.LogsExpandedChanged(newValue))
                    },
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.resume_logging_with_bottom_edge_touch),
                    checked = state.resumeLogsWithTouch,
                    onCheckedChange = { newValue ->
                        onCommand(PreferencesUICommand.ResumeLogsWithTouchChanged(newValue))
                    },
                )
            }
        }
    }
}
