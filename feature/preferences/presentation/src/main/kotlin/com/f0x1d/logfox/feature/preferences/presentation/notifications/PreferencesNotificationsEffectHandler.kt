package com.f0x1d.logfox.feature.preferences.presentation.notifications

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.f0x1d.logfox.core.context.hasNotificationsPermission
import com.f0x1d.logfox.core.tea.EffectHandler
import com.f0x1d.logfox.feature.preferences.api.data.CrashesSettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class PreferencesNotificationsEffectHandler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val crashesSettingsRepository: CrashesSettingsRepository,
) : EffectHandler<PreferencesNotificationsSideEffect, PreferencesNotificationsCommand> {

    private val prefs by lazy { PreferenceManager.getDefaultSharedPreferences(context) }

    override suspend fun handle(
        effect: PreferencesNotificationsSideEffect,
        onCommand: suspend (PreferencesNotificationsCommand) -> Unit,
    ) {
        when (effect) {
            is PreferencesNotificationsSideEffect.LoadPreferences -> {
                onCommand(
                    PreferencesNotificationsCommand.PreferencesLoaded(
                        useSeparateChannels = crashesSettingsRepository.useSeparateNotificationsChannelsForCrashes().value,
                        javaNotifications = crashesSettingsRepository.showingNotificationsFor(CRASH_TYPE_JAVA),
                        jniNotifications = crashesSettingsRepository.showingNotificationsFor(CRASH_TYPE_JNI),
                        anrNotifications = crashesSettingsRepository.showingNotificationsFor(CRASH_TYPE_ANR),
                    ),
                )
            }

            is PreferencesNotificationsSideEffect.CheckPermission -> {
                onCommand(
                    PreferencesNotificationsCommand.PermissionChecked(
                        hasPermission = context.hasNotificationsPermission(),
                    ),
                )
            }

            is PreferencesNotificationsSideEffect.SaveUseSeparateChannels -> {
                crashesSettingsRepository.useSeparateNotificationsChannelsForCrashes().set(effect.enabled)
            }

            is PreferencesNotificationsSideEffect.SaveJavaNotifications -> {
                prefs.edit { putBoolean(PREF_KEY_NOTIFICATIONS_JAVA, effect.enabled) }
            }

            is PreferencesNotificationsSideEffect.SaveJniNotifications -> {
                prefs.edit { putBoolean(PREF_KEY_NOTIFICATIONS_JNI, effect.enabled) }
            }

            is PreferencesNotificationsSideEffect.SaveAnrNotifications -> {
                prefs.edit { putBoolean(PREF_KEY_NOTIFICATIONS_ANR, effect.enabled) }
            }

            // UI side effects - handled by Fragment
            is PreferencesNotificationsSideEffect.OpenLoggingChannelSettings -> Unit
            is PreferencesNotificationsSideEffect.OpenAppNotificationSettings -> Unit
        }
    }

    private companion object {
        const val CRASH_TYPE_JAVA = "java"
        const val CRASH_TYPE_JNI = "jni"
        const val CRASH_TYPE_ANR = "anr"

        const val PREF_KEY_NOTIFICATIONS_JAVA = "pref_notifications_java"
        const val PREF_KEY_NOTIFICATIONS_JNI = "pref_notifications_jni"
        const val PREF_KEY_NOTIFICATIONS_ANR = "pref_notifications_anr"
    }
}
