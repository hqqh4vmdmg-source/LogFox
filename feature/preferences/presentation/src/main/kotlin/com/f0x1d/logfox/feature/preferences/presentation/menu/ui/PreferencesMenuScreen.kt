package com.f0x1d.logfox.feature.preferences.presentation.menu.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.f0x1d.logfox.core.ui.icons.Icons
import com.f0x1d.logfox.feature.preferences.presentation.menu.PreferencesMenuViewState
import com.f0x1d.logfox.feature.preferences.presentation.ui.components.SettingsCategoryHeader
import com.f0x1d.logfox.feature.preferences.presentation.ui.components.SettingsInfoRow
import com.f0x1d.logfox.feature.preferences.presentation.ui.components.SettingsNavigationRow
import com.f0x1d.logfox.feature.strings.Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PreferencesMenuScreen(
    state: PreferencesMenuViewState,
    onUISettings: () -> Unit,
    onServiceSettings: () -> Unit,
    onCrashesSettings: () -> Unit,
    onNotificationsSettings: () -> Unit,
    onLinks: () -> Unit,
    onShareLogs: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(Strings.settings)) },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues,
        ) {
            item {
                SettingsCategoryHeader(title = stringResource(Strings.settings))
            }
            item {
                SettingsNavigationRow(
                    title = stringResource(Strings.ui),
                    iconRes = Icons.ic_settings_ui,
                    onClick = onUISettings,
                )
            }
            item {
                SettingsNavigationRow(
                    title = stringResource(Strings.service),
                    iconRes = Icons.ic_settings_service,
                    onClick = onServiceSettings,
                )
            }
            item {
                SettingsNavigationRow(
                    title = stringResource(Strings.crashes),
                    iconRes = Icons.ic_settings_crashes,
                    onClick = onCrashesSettings,
                )
            }
            item {
                SettingsNavigationRow(
                    title = stringResource(Strings.notifications),
                    iconRes = Icons.ic_settings_notifications,
                    onClick = onNotificationsSettings,
                )
            }
            item {
                SettingsCategoryHeader(title = stringResource(Strings.about_app))
            }
            item {
                SettingsNavigationRow(
                    title = stringResource(Strings.links),
                    iconRes = Icons.ic_add_link,
                    onClick = onLinks,
                )
            }
            item {
                SettingsInfoRow(
                    title = "${state.versionName} (${state.versionCode})",
                    iconRes = Icons.ic_settings_info,
                )
            }
            if (state.isDebug) {
                item {
                    SettingsNavigationRow(
                        title = stringResource(Strings.share_logs),
                        iconRes = Icons.ic_archive,
                        onClick = onShareLogs,
                    )
                }
            }
        }
    }
}
