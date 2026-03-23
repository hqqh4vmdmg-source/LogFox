package com.f0x1d.logfox.feature.preferences.presentation.crashes

import com.f0x1d.logfox.core.tea.BaseStoreViewModel
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.GetCollectingForCrashTypeUseCase
import com.f0x1d.logfox.feature.preferences.presentation.CrashTypeNames
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class PreferencesCrashesViewModel @Inject constructor(
    reducer: PreferencesCrashesReducer,
    effectHandler: PreferencesCrashesEffectHandler,
    viewStateMapper: PreferencesCrashesViewStateMapper,
    getCollectingForCrashTypeUseCase: GetCollectingForCrashTypeUseCase,
) : BaseStoreViewModel<PreferencesCrashesViewState, PreferencesCrashesState, PreferencesCrashesCommand, PreferencesCrashesSideEffect>(
    initialState = PreferencesCrashesState(
        collectJava = getCollectingForCrashTypeUseCase(CrashTypeNames.JAVA),
        collectJni = getCollectingForCrashTypeUseCase(CrashTypeNames.JNI),
        collectAnr = getCollectingForCrashTypeUseCase(CrashTypeNames.ANR),
    ),
    reducer = reducer,
    effectHandlers = listOf(effectHandler),
    viewStateMapper = viewStateMapper,
    initialSideEffects = listOf(PreferencesCrashesSideEffect.LoadPreferences),
)
