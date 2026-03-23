package com.f0x1d.logfox.feature.preferences.presentation.notifications

internal sealed interface PreferencesNotificationsSideEffect {
    // Business logic side effects - handled by EffectHandler
    data object LoadPreferences : PreferencesNotificationsSideEffect
    data object CheckPermission : PreferencesNotificationsSideEffect
    data class SaveUseSeparateChannels(val enabled: Boolean) : PreferencesNotificationsSideEffect
    data class SaveJavaNotifications(val enabled: Boolean) : PreferencesNotificationsSideEffect
    data class SaveJniNotifications(val enabled: Boolean) : PreferencesNotificationsSideEffect
    data class SaveAnrNotifications(val enabled: Boolean) : PreferencesNotificationsSideEffect

    // UI side effects - handled by Fragment
    data object OpenLoggingChannelSettings : PreferencesNotificationsSideEffect
    data object OpenAppNotificationSettings : PreferencesNotificationsSideEffect
}
