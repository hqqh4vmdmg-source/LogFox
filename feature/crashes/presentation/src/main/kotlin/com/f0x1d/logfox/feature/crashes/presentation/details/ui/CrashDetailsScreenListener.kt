package com.f0x1d.logfox.feature.crashes.presentation.details.ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class CrashDetailsScreenListener(
    val onBackClick: () -> Unit,
    val onWrapLinesClick: () -> Unit,
    val onInfoClick: () -> Unit,
    val onNotificationsClick: () -> Unit,
    val onBlacklistClick: () -> Unit,
    val onDeleteClick: () -> Unit,
    val onSearchQueryChange: (String) -> Unit,
    val onCopyClick: () -> Unit,
    val onShareClick: () -> Unit,
    val onExportClick: () -> Unit,
    val onZipClick: () -> Unit,
)

internal val MockCrashDetailsScreenListener = CrashDetailsScreenListener(
    onBackClick = { },
    onWrapLinesClick = { },
    onInfoClick = { },
    onNotificationsClick = { },
    onBlacklistClick = { },
    onDeleteClick = { },
    onSearchQueryChange = { },
    onCopyClick = { },
    onShareClick = { },
    onExportClick = { },
    onZipClick = { },
)
