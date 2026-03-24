package com.f0x1d.logfox.feature.crashes.presentation.appcrashes

import androidx.compose.runtime.Immutable
import com.f0x1d.logfox.feature.crashes.presentation.common.model.AppCrashesCountItem

@Immutable
internal data class AppCrashesViewState(
    val packageName: String,
    val appName: String?,
    val crashes: List<AppCrashesCountItem>,
)
