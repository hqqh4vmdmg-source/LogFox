package com.f0x1d.logfox.feature.preferences.presentation.crashes

import com.f0x1d.logfox.core.tea.EffectHandler
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.GetCollectingForCrashTypeFlowUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.crashes.SetCollectingForCrashTypeUseCase
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

private const val CRASH_TYPE_JAVA = "JAVA"
private const val CRASH_TYPE_JNI = "JNI"
private const val CRASH_TYPE_ANR = "ANR"

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
                    getCollectingForCrashTypeFlowUseCase(CRASH_TYPE_JAVA),
                    getCollectingForCrashTypeFlowUseCase(CRASH_TYPE_JNI),
                    getCollectingForCrashTypeFlowUseCase(CRASH_TYPE_ANR),
                ) { collectJava, collectJni, collectAnr ->
                    PreferencesCrashesCommand.PreferencesLoaded(
                        collectJava = collectJava,
                        collectJni = collectJni,
                        collectAnr = collectAnr,
                    )
                }.collect { command ->
                    onCommand(command)
                }
            }

            is PreferencesCrashesSideEffect.SaveCollectJava -> {
                setCollectingForCrashTypeUseCase(CRASH_TYPE_JAVA, effect.collect)
            }

            is PreferencesCrashesSideEffect.SaveCollectJni -> {
                setCollectingForCrashTypeUseCase(CRASH_TYPE_JNI, effect.collect)
            }

            is PreferencesCrashesSideEffect.SaveCollectAnr -> {
                setCollectingForCrashTypeUseCase(CRASH_TYPE_ANR, effect.collect)
            }
        }
    }
}
