package com.f0x1d.logfox.feature.preferences.presentation.crashes

internal sealed interface PreferencesCrashesSideEffect {
    data object LoadPreferences : PreferencesCrashesSideEffect
    data class SaveCollectJava(val collect: Boolean) : PreferencesCrashesSideEffect
    data class SaveCollectJni(val collect: Boolean) : PreferencesCrashesSideEffect
    data class SaveCollectAnr(val collect: Boolean) : PreferencesCrashesSideEffect
}

