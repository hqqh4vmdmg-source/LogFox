package com.f0x1d.logfox.feature.preferences.presentation.service.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.f0x1d.logfox.compose.designsystem.component.button.NavigationBackButton
import com.f0x1d.logfox.feature.preferences.presentation.service.PreferencesServiceViewState
import com.f0x1d.logfox.feature.preferences.presentation.ui.components.SettingsCategoryHeader
import com.f0x1d.logfox.feature.preferences.presentation.ui.components.SettingsClickableRow
import com.f0x1d.logfox.feature.preferences.presentation.ui.components.SettingsSwitchRow
import com.f0x1d.logfox.feature.strings.Strings
import com.f0x1d.logfox.feature.terminals.api.base.TerminalType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PreferencesServiceScreen(
    state: PreferencesServiceViewState,
    onBack: () -> Unit,
    onTerminalSelected: (TerminalType) -> Unit,
    onFallbackToDefaultChanged: (Boolean) -> Unit,
    onStartOnBootChanged: (Boolean) -> Unit,
    onStopLoggingOnBackExitChanged: (Boolean) -> Unit,
    onShowLogsFromAppLaunchChanged: (Boolean) -> Unit,
    onIncludeDeviceInfoChanged: (Boolean) -> Unit,
    onIncludeAppInfoChanged: (Boolean) -> Unit,
    onExportLogsAsTxtChanged: (Boolean) -> Unit,
) {
    var showTerminalDialog by remember { mutableStateOf(false) }

    if (showTerminalDialog && state.terminalNames.isNotEmpty()) {
        val selectedIndex = TerminalType.entries.indexOf(state.selectedTerminalType)
        AlertDialog(
            onDismissRequest = { showTerminalDialog = false },
            title = { Text(text = stringResource(Strings.terminal)) },
            text = {
                LazyColumn {
                    state.terminalNames.forEachIndexed { index, name ->
                        item(key = index) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showTerminalDialog = false
                                        onTerminalSelected(TerminalType.entries[index])
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                RadioButton(
                                    selected = index == selectedIndex,
                                    onClick = {
                                        showTerminalDialog = false
                                        onTerminalSelected(TerminalType.entries[index])
                                    },
                                )
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTerminalDialog = false }) {
                    Text(text = stringResource(Strings.close))
                }
            },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(Strings.service)) },
                navigationIcon = { NavigationBackButton(onClick = onBack) },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues,
        ) {
            item {
                SettingsCategoryHeader(title = stringResource(Strings.terminal))
            }
            item {
                SettingsClickableRow(
                    title = stringResource(Strings.terminal),
                    summary = state.terminalNames.getOrNull(
                        TerminalType.entries.indexOf(state.selectedTerminalType)
                    ),
                    onClick = { showTerminalDialog = true },
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.fallback_to_default_terminal),
                    subtitle = stringResource(Strings.fallback_to_default_terminal_desc),
                    checked = state.fallbackToDefault,
                    onCheckedChange = onFallbackToDefaultChanged,
                )
            }
            item {
                SettingsCategoryHeader(title = stringResource(Strings.service))
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.start_on_boot),
                    checked = state.startOnBoot,
                    onCheckedChange = onStartOnBootChanged,
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.stop_logging_on_back_exit),
                    checked = state.stopLoggingOnBackExit,
                    onCheckedChange = onStopLoggingOnBackExitChanged,
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.show_logs_from_app_launch),
                    checked = state.showLogsFromAppLaunch,
                    onCheckedChange = onShowLogsFromAppLaunchChanged,
                )
            }
            item {
                SettingsCategoryHeader(title = stringResource(Strings.exports))
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.include_device_information_in_archives),
                    checked = state.includeDeviceInfo,
                    onCheckedChange = onIncludeDeviceInfoChanged,
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.include_app_information_in_exports),
                    checked = state.includeAppInfo,
                    onCheckedChange = onIncludeAppInfoChanged,
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.export_logs_as_txt),
                    checked = state.exportLogsAsTxt,
                    onCheckedChange = onExportLogsAsTxtChanged,
                )
            }
        }
    }
}
