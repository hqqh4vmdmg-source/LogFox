package com.f0x1d.logfox.feature.preferences.presentation.crashes

import com.f0x1d.logfox.core.tea.ReduceResult
import com.f0x1d.logfox.core.tea.Reducer
import com.f0x1d.logfox.core.tea.noSideEffects
import com.f0x1d.logfox.core.tea.withSideEffects
import javax.inject.Inject

internal class PreferencesCrashesReducer @Inject constructor() : Reducer<PreferencesCrashesState, PreferencesCrashesCommand, PreferencesCrashesSideEffect> {

    override fun reduce(
        state: PreferencesCrashesState,
        command: PreferencesCrashesCommand,
    ): ReduceResult<PreferencesCrashesState, PreferencesCrashesSideEffect> = when (command) {
        is PreferencesCrashesCommand.Load -> state.withSideEffects(
            PreferencesCrashesSideEffect.LoadPreferences,
        )

        is PreferencesCrashesCommand.PreferencesLoaded -> state.copy(
            collectJava = command.collectJava,
            collectJni = command.collectJni,
            collectAnr = command.collectAnr,
        ).noSideEffects()

        is PreferencesCrashesCommand.CollectJavaChanged -> state.copy(collectJava = command.collect)
            .withSideEffects(PreferencesCrashesSideEffect.SaveCollectJava(command.collect))

        is PreferencesCrashesCommand.CollectJniChanged -> state.copy(collectJni = command.collect)
            .withSideEffects(PreferencesCrashesSideEffect.SaveCollectJni(command.collect))

        is PreferencesCrashesCommand.CollectAnrChanged -> state.copy(collectAnr = command.collect)
            .withSideEffects(PreferencesCrashesSideEffect.SaveCollectAnr(command.collect))
    }
}
