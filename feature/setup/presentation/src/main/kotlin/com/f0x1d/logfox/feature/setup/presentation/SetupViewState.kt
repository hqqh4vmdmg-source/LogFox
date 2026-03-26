package com.f0x1d.logfox.feature.setup.presentation

import androidx.compose.runtime.Immutable

@Immutable
internal data class SetupViewState(val showAdbDialog: Boolean, val adbCommand: String)
