package com.f0x1d.logfox.feature.preferences.presentation.notifications.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.preference.PreferenceManager
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
) {
    val context = LocalContext.current
    val prefs = remember(context) { PreferenceManager.getDefaultSharedPreferences(context) }

    var useSeparateChannels by remember { mutableStateOf(prefs.getBoolean("pref_notifications_use_separate_channels", true)) }
    var javaNotifications by remember { mutableStateOf(prefs.getBoolean("pref_notifications_java", true)) }
    var jniNotifications by remember { mutableStateOf(prefs.getBoolean("pref_notifications_jni", true)) }
    var anrNotifications by remember { mutableStateOf(prefs.getBoolean("pref_notifications_anr", true)) }

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
                    checked = useSeparateChannels,
                    onCheckedChange = { newValue ->
                        prefs.edit().putBoolean("pref_notifications_use_separate_channels", newValue).apply()
                        useSeparateChannels = newValue
                    },
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.show_java_crashes_notifications),
                    checked = javaNotifications,
                    onCheckedChange = { newValue ->
                        prefs.edit().putBoolean("pref_notifications_java", newValue).apply()
                        javaNotifications = newValue
                    },
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.show_jni_crashes_notifications),
                    checked = jniNotifications,
                    onCheckedChange = { newValue ->
                        prefs.edit().putBoolean("pref_notifications_jni", newValue).apply()
                        jniNotifications = newValue
                    },
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.show_anr_notifications),
                    checked = anrNotifications,
                    onCheckedChange = { newValue ->
                        prefs.edit().putBoolean("pref_notifications_anr", newValue).apply()
                        anrNotifications = newValue
                    },
                )
            }
        }
    }
}
