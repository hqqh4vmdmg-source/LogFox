package com.f0x1d.logfox.feature.filters.presentation.list.ui

import androidx.compose.runtime.Immutable
import com.f0x1d.logfox.feature.filters.api.model.UserFilter

@Immutable
internal data class FiltersScreenListener(
    val onBackClick: () -> Unit,
    val onFilterClick: (UserFilter) -> Unit,
    val onFilterDelete: (UserFilter) -> Unit,
    val onFilterChecked: (UserFilter, Boolean) -> Unit,
    val onAddClick: () -> Unit,
    val onClearClick: () -> Unit,
    val onImportClick: () -> Unit,
    val onExportAllClick: () -> Unit,
)

internal val MockFiltersScreenListener = FiltersScreenListener(
    onBackClick = { },
    onFilterClick = { },
    onFilterDelete = { },
    onFilterChecked = { _, _ -> },
    onAddClick = { },
    onClearClick = { },
    onImportClick = { },
    onExportAllClick = { },
)
