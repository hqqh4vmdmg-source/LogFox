package com.f0x1d.logfox.feature.crashes.presentation.list.ui

import androidx.compose.runtime.Immutable
import com.f0x1d.logfox.feature.crashes.presentation.common.model.AppCrashesCountItem
import com.f0x1d.logfox.feature.preferences.api.CrashesSort

@Immutable
internal data class CrashesScreenListener(
    val onCrashClick: (AppCrashesCountItem) -> Unit,
    val onDeleteCrashClick: (AppCrashesCountItem) -> Unit,
    val onSearchedCrashClick: (AppCrashesCountItem) -> Unit,
    val onDeleteSearchedCrashClick: (AppCrashesCountItem) -> Unit,
    val onQueryChange: (String) -> Unit,
    val onSortConfirmed: (sortType: CrashesSort, sortInReversedOrder: Boolean) -> Unit,
    val onBlacklistClick: () -> Unit,
    val onClearClick: () -> Unit,
)

internal val MockCrashesScreenListener = CrashesScreenListener(
    onCrashClick = { },
    onDeleteCrashClick = { },
    onSearchedCrashClick = { },
    onDeleteSearchedCrashClick = { },
    onQueryChange = { },
    onSortConfirmed = { _, _ -> },
    onBlacklistClick = { },
    onClearClick = { },
)
