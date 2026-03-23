package com.f0x1d.logfox.feature.preferences.presentation.notifications

import com.f0x1d.logfox.core.tea.ReduceResult
import com.f0x1d.logfox.core.tea.Reducer
import com.f0x1d.logfox.core.tea.noSideEffects
import com.f0x1d.logfox.core.tea.withSideEffects
import javax.inject.Inject

internal class PreferencesNotificationsReducer @Inject constructor() : Reducer<PreferencesNotificationsState, PreferencesNotificationsCommand, PreferencesNotificationsSideEffect> {

    override fun reduce(
        state: PreferencesNotificationsState,
        command: PreferencesNotificationsCommand,
    ): ReduceResult<PreferencesNotificationsState, PreferencesNotificationsSideEffect> = when (command) {
        is PreferencesNotificationsCommand.Load -> {
            state.withSideEffects(PreferencesNotificationsSideEffect.LoadPreferences)
        }

        is PreferencesNotificationsCommand.CheckPermission -> {
            state.withSideEffects(PreferencesNotificationsSideEffect.CheckPermission)
        }

        is PreferencesNotificationsCommand.PermissionChecked -> {
            state.copy(hasNotificationsPermission = command.hasPermission).noSideEffects()
        }

        is PreferencesNotificationsCommand.PreferencesLoaded -> {
            state.copy(
                useSeparateChannels = command.useSeparateChannels,
                javaNotifications = command.javaNotifications,
                jniNotifications = command.jniNotifications,
                anrNotifications = command.anrNotifications,
            ).noSideEffects()
        }

        is PreferencesNotificationsCommand.OpenLoggingNotificationSettings -> {
            state.withSideEffects(PreferencesNotificationsSideEffect.OpenLoggingChannelSettings)
        }

        is PreferencesNotificationsCommand.OpenNotificationsPermissionSettings -> {
            state.withSideEffects(
                PreferencesNotificationsSideEffect.OpenAppNotificationSettings,
            )
        }

        is PreferencesNotificationsCommand.UseSeparateChannelsChanged -> {
            state.copy(useSeparateChannels = command.enabled).withSideEffects(
                PreferencesNotificationsSideEffect.SaveUseSeparateChannels(command.enabled),
            )
        }

        is PreferencesNotificationsCommand.JavaNotificationsChanged -> {
            state.copy(javaNotifications = command.enabled).withSideEffects(
                PreferencesNotificationsSideEffect.SaveJavaNotifications(command.enabled),
            )
        }

        is PreferencesNotificationsCommand.JniNotificationsChanged -> {
            state.copy(jniNotifications = command.enabled).withSideEffects(
                PreferencesNotificationsSideEffect.SaveJniNotifications(command.enabled),
            )
        }

        is PreferencesNotificationsCommand.AnrNotificationsChanged -> {
            state.copy(anrNotifications = command.enabled).withSideEffects(
                PreferencesNotificationsSideEffect.SaveAnrNotifications(command.enabled),
            )
        }
    }
}
