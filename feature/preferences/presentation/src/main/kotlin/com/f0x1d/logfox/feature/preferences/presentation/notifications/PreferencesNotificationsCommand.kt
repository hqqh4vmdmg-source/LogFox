package com.f0x1d.logfox.feature.preferences.presentation.notifications

internal sealed interface PreferencesNotificationsCommand {
    data object Load : PreferencesNotificationsCommand
    data object CheckPermission : PreferencesNotificationsCommand
    data object OpenLoggingNotificationSettings : PreferencesNotificationsCommand
    data object OpenNotificationsPermissionSettings : PreferencesNotificationsCommand
    data class UseSeparateChannelsChanged(val enabled: Boolean) : PreferencesNotificationsCommand
    data class JavaNotificationsChanged(val enabled: Boolean) : PreferencesNotificationsCommand
    data class JniNotificationsChanged(val enabled: Boolean) : PreferencesNotificationsCommand
    data class AnrNotificationsChanged(val enabled: Boolean) : PreferencesNotificationsCommand

    // Commands from effect handler
    data class PermissionChecked(val hasPermission: Boolean) : PreferencesNotificationsCommand
    data class PreferencesLoaded(
        val useSeparateChannels: Boolean,
        val javaNotifications: Boolean,
        val jniNotifications: Boolean,
        val anrNotifications: Boolean,
    ) : PreferencesNotificationsCommand
}
