package com.f0x1d.logfox.feature.preferences.presentation.notifications.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.f0x1d.logfox.compose.designsystem.component.button.NavigationBackButton
import com.f0x1d.logfox.feature.preferences.presentation.notifications.PreferencesNotificationsViewState
import com.f0x1d.logfox.feature.preferences.presentation.ui.components.SettingsCategoryHeader
import com.f0x1d.logfox.feature.preferences.presentation.ui.components.SettingsClickableRow
import com.f0x1d.logfox.feature.preferences.presentation.ui.components.SettingsSwitchRow
import com.f0x1d.logfox.feature.strings.Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PreferencesNotificationsScreen(
    state: PreferencesNotificationsViewState,
    onBack: () -> Unit,
    onLoggingNotificationClick: () -> Unit,
    onNotificationsPermissionClick: () -> Unit,
    onUseSeparateChannelsChanged: (Boolean) -> Unit,
    onJavaNotificationsChanged: (Boolean) -> Unit,
    onJniNotificationsChanged: (Boolean) -> Unit,
    onAnrNotificationsChanged: (Boolean) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(Strings.notifications)) },
                navigationIcon = { NavigationBackButton(onClick = onBack) },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues,
        ) {
            if (!state.hasNotificationsPermission) {
                item {
                    SettingsClickableRow(
                        title = stringResource(Strings.no_notification_permission),
                        summary = stringResource(Strings.notification_permission_is_required),
                        onClick = onNotificationsPermissionClick,
                    )
                }
            }
            if (state.notificationsChannelsAvailable) {
                item {
                    SettingsCategoryHeader(title = stringResource(Strings.logging))
                }
                item {
                    SettingsClickableRow(
                        title = stringResource(Strings.logging_notification),
                        onClick = onLoggingNotificationClick,
                    )
                }
            }
            item {
                SettingsCategoryHeader(title = stringResource(Strings.crashes))
            }
            if (state.notificationsChannelsAvailable) {
                item {
                    SettingsClickableRow(
                        title = stringResource(Strings.per_app_notifications_settings),
                        onClick = {},
                        enabled = false,
                    )
                }
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.use_separate_channels_for_crashes),
                    checked = state.useSeparateChannels,
                    onCheckedChange = onUseSeparateChannelsChanged,
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.show_java_crashes_notifications),
                    checked = state.javaNotifications,
                    onCheckedChange = onJavaNotificationsChanged,
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.show_jni_crashes_notifications),
                    checked = state.jniNotifications,
                    onCheckedChange = onJniNotificationsChanged,
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.show_anr_notifications),
                    checked = state.anrNotifications,
                    onCheckedChange = onAnrNotificationsChanged,
                )
            }
        }
    }
}
