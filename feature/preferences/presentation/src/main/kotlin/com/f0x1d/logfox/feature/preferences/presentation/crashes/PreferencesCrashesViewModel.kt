package com.f0x1d.logfox.feature.preferences.presentation.crashes

import com.f0x1d.logfox.core.tea.BaseStoreViewModel
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.GetCollectingForCrashTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val CRASH_TYPE_JAVA = "JAVA"
private const val CRASH_TYPE_JNI = "JNI"
private const val CRASH_TYPE_ANR = "ANR"

@HiltViewModel
internal class PreferencesCrashesViewModel @Inject constructor(
    reducer: PreferencesCrashesReducer,
    effectHandler: PreferencesCrashesEffectHandler,
    viewStateMapper: PreferencesCrashesViewStateMapper,
    getCollectingForCrashTypeUseCase: GetCollectingForCrashTypeUseCase,
) : BaseStoreViewModel<PreferencesCrashesViewState, PreferencesCrashesState, PreferencesCrashesCommand, PreferencesCrashesSideEffect>(
    initialState = PreferencesCrashesState(
        collectJava = getCollectingForCrashTypeUseCase(CRASH_TYPE_JAVA),
        collectJni = getCollectingForCrashTypeUseCase(CRASH_TYPE_JNI),
        collectAnr = getCollectingForCrashTypeUseCase(CRASH_TYPE_ANR),
    ),
    reducer = reducer,
    effectHandlers = listOf(effectHandler),
    viewStateMapper = viewStateMapper,
    initialSideEffects = listOf(PreferencesCrashesSideEffect.LoadPreferences),
)
