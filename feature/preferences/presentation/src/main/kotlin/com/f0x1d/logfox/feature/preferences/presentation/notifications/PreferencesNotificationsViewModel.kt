package com.f0x1d.logfox.feature.preferences.presentation.notifications

import com.f0x1d.logfox.core.compat.notificationsChannelsAvailable
import com.f0x1d.logfox.core.tea.BaseStoreViewModel
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.GetShowingNotificationsForCrashTypeUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.GetUseSeparateNotificationsChannelsForCrashesUseCase
import com.f0x1d.logfox.feature.preferences.presentation.CrashTypeNames
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

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
        javaNotifications = getShowingNotificationsForCrashTypeUseCase(CrashTypeNames.JAVA),
        jniNotifications = getShowingNotificationsForCrashTypeUseCase(CrashTypeNames.JNI),
        anrNotifications = getShowingNotificationsForCrashTypeUseCase(CrashTypeNames.ANR),
    ),
    reducer = reducer,
    effectHandlers = listOf(effectHandler),
    viewStateMapper = viewStateMapper,
    initialSideEffects = listOf(PreferencesNotificationsSideEffect.LoadPreferences),
)
