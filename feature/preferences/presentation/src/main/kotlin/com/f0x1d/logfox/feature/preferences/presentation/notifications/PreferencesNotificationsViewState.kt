package com.f0x1d.logfox.feature.preferences.presentation.notifications

internal data class PreferencesNotificationsViewState(
    val hasNotificationsPermission: Boolean,
    val notificationsChannelsAvailable: Boolean,
    val useSeparateChannels: Boolean,
    val javaNotifications: Boolean,
    val jniNotifications: Boolean,
    val anrNotifications: Boolean,
)
