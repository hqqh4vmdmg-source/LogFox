package com.f0x1d.logfox.feature.preferences.presentation.notifications

import android.content.Context
import com.f0x1d.logfox.core.context.hasNotificationsPermission
import com.f0x1d.logfox.core.tea.EffectHandler
import com.f0x1d.logfox.feature.preferences.api.data.CrashesSettingsRepository
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.SetShowingNotificationsForCrashTypeUseCase
import com.f0x1d.logfox.feature.preferences.presentation.CrashTypeNames
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class PreferencesNotificationsEffectHandler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val crashesSettingsRepository: CrashesSettingsRepository,
    private val setShowingNotificationsForCrashTypeUseCase: SetShowingNotificationsForCrashTypeUseCase,
) : EffectHandler<PreferencesNotificationsSideEffect, PreferencesNotificationsCommand> {

    override suspend fun handle(
        effect: PreferencesNotificationsSideEffect,
        onCommand: suspend (PreferencesNotificationsCommand) -> Unit,
    ) {
        when (effect) {
            is PreferencesNotificationsSideEffect.LoadPreferences -> onCommand(
                PreferencesNotificationsCommand.PreferencesLoaded(
                    useSeparateChannels = crashesSettingsRepository.useSeparateNotificationsChannelsForCrashes().value,
                    javaNotifications = crashesSettingsRepository.showingNotificationsFor(CrashTypeNames.JAVA),
                    jniNotifications = crashesSettingsRepository.showingNotificationsFor(CrashTypeNames.JNI),
                    anrNotifications = crashesSettingsRepository.showingNotificationsFor(CrashTypeNames.ANR),
                ),
            )

            is PreferencesNotificationsSideEffect.CheckPermission -> onCommand(
                PreferencesNotificationsCommand.PermissionChecked(
                    hasPermission = context.hasNotificationsPermission(),
                ),
            )

            is PreferencesNotificationsSideEffect.SaveUseSeparateChannels ->
                crashesSettingsRepository.useSeparateNotificationsChannelsForCrashes().set(effect.enabled)

            is PreferencesNotificationsSideEffect.SaveJavaNotifications ->
                setShowingNotificationsForCrashTypeUseCase(CrashTypeNames.JAVA, effect.enabled)

            is PreferencesNotificationsSideEffect.SaveJniNotifications ->
                setShowingNotificationsForCrashTypeUseCase(CrashTypeNames.JNI, effect.enabled)

            is PreferencesNotificationsSideEffect.SaveAnrNotifications ->
                setShowingNotificationsForCrashTypeUseCase(CrashTypeNames.ANR, effect.enabled)

            // UI side effects - handled by Fragment
            is PreferencesNotificationsSideEffect.OpenLoggingChannelSettings -> Unit
            is PreferencesNotificationsSideEffect.OpenAppNotificationSettings -> Unit
        }
    }
}
