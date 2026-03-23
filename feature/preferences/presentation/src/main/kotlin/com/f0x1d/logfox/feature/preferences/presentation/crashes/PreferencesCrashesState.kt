package com.f0x1d.logfox.feature.preferences.presentation.crashes

internal data class PreferencesCrashesState(
    val collectJava: Boolean,
    val collectJni: Boolean,
    val collectAnr: Boolean,
)
