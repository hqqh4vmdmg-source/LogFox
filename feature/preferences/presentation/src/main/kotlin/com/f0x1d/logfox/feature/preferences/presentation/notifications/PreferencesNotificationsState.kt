package com.f0x1d.logfox.feature.preferences.presentation.notifications

internal data class PreferencesNotificationsState(
    val hasNotificationsPermission: Boolean,
    val notificationsChannelsAvailable: Boolean,
    val useSeparateChannels: Boolean = true,
    val javaNotifications: Boolean = true,
    val jniNotifications: Boolean = true,
    val anrNotifications: Boolean = true,
)
