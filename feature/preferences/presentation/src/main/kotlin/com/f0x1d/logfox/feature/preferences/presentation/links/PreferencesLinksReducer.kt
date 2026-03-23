package com.f0x1d.logfox.feature.preferences.presentation.links

import com.f0x1d.logfox.core.tea.ReduceResult
import com.f0x1d.logfox.core.tea.Reducer
import com.f0x1d.logfox.core.tea.withSideEffects
import javax.inject.Inject

internal class PreferencesLinksReducer @Inject constructor() : Reducer<PreferencesLinksState, PreferencesLinksCommand, PreferencesLinksSideEffect> {

    override fun reduce(
        state: PreferencesLinksState,
        command: PreferencesLinksCommand,
    ): ReduceResult<PreferencesLinksState, PreferencesLinksSideEffect> = when (command) {
        is PreferencesLinksCommand.OpenUrl -> {
            state.withSideEffects(PreferencesLinksSideEffect.OpenUrl(command.url))
        }
    }
}
