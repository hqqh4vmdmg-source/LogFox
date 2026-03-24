package com.f0x1d.logfox.feature.preferences.presentation.crashes

import com.f0x1d.logfox.core.tea.EffectHandler
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.GetCollectingForCrashTypeFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.SetCollectingForCrashTypeUseCase
import com.f0x1d.logfox.feature.preferences.presentation.CrashTypeNames
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

internal class PreferencesCrashesEffectHandler @Inject constructor(
    private val getCollectingForCrashTypeFlowUseCase: GetCollectingForCrashTypeFlowUseCase,
    private val setCollectingForCrashTypeUseCase: SetCollectingForCrashTypeUseCase,
) : EffectHandler<PreferencesCrashesSideEffect, PreferencesCrashesCommand> {

    override suspend fun handle(
        effect: PreferencesCrashesSideEffect,
        onCommand: suspend (PreferencesCrashesCommand) -> Unit,
    ) {
        when (effect) {
            is PreferencesCrashesSideEffect.LoadPreferences -> {
                combine(
                    getCollectingForCrashTypeFlowUseCase(CrashTypeNames.JAVA),
                    getCollectingForCrashTypeFlowUseCase(CrashTypeNames.JNI),
                    getCollectingForCrashTypeFlowUseCase(CrashTypeNames.ANR),
                ) { collectJava, collectJni, collectAnr ->
                    PreferencesCrashesCommand.PreferencesLoaded(
                        collectJava = collectJava,
                        collectJni = collectJni,
                        collectAnr = collectAnr,
                    )
                }.collect(onCommand)
            }

            is PreferencesCrashesSideEffect.SaveCollectJava -> {
                setCollectingForCrashTypeUseCase(CrashTypeNames.JAVA, effect.collect)
            }

            is PreferencesCrashesSideEffect.SaveCollectJni -> {
                setCollectingForCrashTypeUseCase(CrashTypeNames.JNI, effect.collect)
            }

            is PreferencesCrashesSideEffect.SaveCollectAnr -> {
                setCollectingForCrashTypeUseCase(CrashTypeNames.ANR, effect.collect)
            }
        }
    }
}
