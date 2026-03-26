package com.f0x1d.logfox.feature.preferences.presentation.menu

import androidx.compose.runtime.Immutable

@Immutable
internal data class PreferencesMenuViewState(
    val versionName: String,
    val versionCode: Int,
    val isDebug: Boolean,
)
