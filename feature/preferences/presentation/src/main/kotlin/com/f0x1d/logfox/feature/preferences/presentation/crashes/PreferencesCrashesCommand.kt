package com.f0x1d.logfox.feature.preferences.presentation.crashes

internal sealed interface PreferencesCrashesCommand {
    data object Load : PreferencesCrashesCommand

    data class CollectJavaChanged(val collect: Boolean) : PreferencesCrashesCommand

    data class CollectJniChanged(val collect: Boolean) : PreferencesCrashesCommand

    data class CollectAnrChanged(val collect: Boolean) : PreferencesCrashesCommand

    // Commands from effect handler
    data class PreferencesLoaded(
        val collectJava: Boolean,
        val collectJni: Boolean,
        val collectAnr: Boolean,
    ) : PreferencesCrashesCommand
}

