package com.f0x1d.logfox.feature.crashes.presentation.appcrashes.ui

import androidx.compose.runtime.Immutable
import com.f0x1d.logfox.feature.crashes.presentation.common.model.AppCrashesCountItem

@Immutable
internal data class AppCrashesScreenListener(
    val onBackClick: () -> Unit,
    val onCrashClick: (AppCrashesCountItem) -> Unit,
    val onDeleteCrashClick: (AppCrashesCountItem) -> Unit,
)

internal val MockAppCrashesScreenListener = AppCrashesScreenListener(
    onBackClick = { },
    onCrashClick = { },
    onDeleteCrashClick = { },
)
