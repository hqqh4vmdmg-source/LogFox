package com.f0x1d.logfox.feature.preferences.presentation.notifications

import com.f0x1d.logfox.core.compat.notificationsChannelsAvailable
import com.f0x1d.logfox.core.tea.BaseStoreViewModel
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.GetShowingNotificationsForCrashTypeUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.GetUseSeparateNotificationsChannelsForCrashesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val CRASH_TYPE_JAVA = "java"
private const val CRASH_TYPE_JNI = "jni"
private const val CRASH_TYPE_ANR = "anr"

@HiltViewModel
internal class PreferencesNotificationsViewModel @Inject constructor(
    reducer: PreferencesNotificationsReducer,
    effectHandler: PreferencesNotificationsEffectHandler,
    viewStateMapper: PreferencesNotificationsViewStateMapper,
    getUseSeparateNotificationsChannelsForCrashesUseCase: GetUseSeparateNotificationsChannelsForCrashesUseCase,
    getShowingNotificationsForCrashTypeUseCase: GetShowingNotificationsForCrashTypeUseCase,
) : BaseStoreViewModel<PreferencesNotificationsViewState, PreferencesNotificationsState, PreferencesNotificationsCommand, PreferencesNotificationsSideEffect>(
    initialState = PreferencesNotificationsState(
        hasNotificationsPermission = true,
        notificationsChannelsAvailable = notificationsChannelsAvailable,
        useSeparateChannels = getUseSeparateNotificationsChannelsForCrashesUseCase(),
        javaNotifications = getShowingNotificationsForCrashTypeUseCase(CRASH_TYPE_JAVA),
        jniNotifications = getShowingNotificationsForCrashTypeUseCase(CRASH_TYPE_JNI),
        anrNotifications = getShowingNotificationsForCrashTypeUseCase(CRASH_TYPE_ANR),
    ),
    reducer = reducer,
    effectHandlers = listOf(effectHandler),
    viewStateMapper = viewStateMapper,
    initialSideEffects = listOf(PreferencesNotificationsSideEffect.LoadPreferences),
)
