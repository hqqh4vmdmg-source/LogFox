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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.preference.PreferenceManager
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
    onStartOnBootChanged: (Boolean) -> Unit,
    onShowLogsFromAppLaunchChanged: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    val prefs = remember(context) { PreferenceManager.getDefaultSharedPreferences(context) }

    var fallbackToDefault by remember { mutableStateOf(prefs.getBoolean("pref_fallback_to_default_terminal", true)) }
    var stopLoggingOnBackExit by remember { mutableStateOf(prefs.getBoolean("pref_stop_logging_on_back_exit", false)) }
    var startOnBoot by remember { mutableStateOf(prefs.getBoolean("pref_start_on_boot", true)) }
    var showLogsFromAppLaunch by remember { mutableStateOf(prefs.getBoolean("pref_show_logs_from_app_launch", true)) }
    var includeDeviceInfo by remember { mutableStateOf(prefs.getBoolean("pref_include_device_info_in_archives", true)) }
    var includeAppInfo by remember { mutableStateOf(prefs.getBoolean("pref_include_app_info_in_exports", true)) }
    var exportLogsAsTxt by remember { mutableStateOf(prefs.getBoolean("pref_export_logs_as_txt", false)) }

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
                    checked = fallbackToDefault,
                    onCheckedChange = { newValue ->
                        prefs.edit().putBoolean("pref_fallback_to_default_terminal", newValue).apply()
                        fallbackToDefault = newValue
                    },
                )
            }
            item {
                SettingsCategoryHeader(title = stringResource(Strings.service))
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.start_on_boot),
                    checked = startOnBoot,
                    onCheckedChange = { newValue ->
                        prefs.edit().putBoolean("pref_start_on_boot", newValue).apply()
                        startOnBoot = newValue
                        onStartOnBootChanged(newValue)
                    },
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.stop_logging_on_back_exit),
                    checked = stopLoggingOnBackExit,
                    onCheckedChange = { newValue ->
                        prefs.edit().putBoolean("pref_stop_logging_on_back_exit", newValue).apply()
                        stopLoggingOnBackExit = newValue
                    },
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.show_logs_from_app_launch),
                    checked = showLogsFromAppLaunch,
                    onCheckedChange = { newValue ->
                        prefs.edit().putBoolean("pref_show_logs_from_app_launch", newValue).apply()
                        showLogsFromAppLaunch = newValue
                        onShowLogsFromAppLaunchChanged(newValue)
                    },
                )
            }
            item {
                SettingsCategoryHeader(title = stringResource(Strings.exports))
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.include_device_information_in_archives),
                    checked = includeDeviceInfo,
                    onCheckedChange = { newValue ->
                        prefs.edit().putBoolean("pref_include_device_info_in_archives", newValue).apply()
                        includeDeviceInfo = newValue
                    },
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.include_app_information_in_exports),
                    checked = includeAppInfo,
                    onCheckedChange = { newValue ->
                        prefs.edit().putBoolean("pref_include_app_info_in_exports", newValue).apply()
                        includeAppInfo = newValue
                    },
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.export_logs_as_txt),
                    checked = exportLogsAsTxt,
                    onCheckedChange = { newValue ->
                        prefs.edit().putBoolean("pref_export_logs_as_txt", newValue).apply()
                        exportLogsAsTxt = newValue
                    },
                )
            }
        }
    }
}
