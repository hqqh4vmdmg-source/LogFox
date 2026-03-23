package com.f0x1d.logfox.feature.filters.presentation.edit.ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class EditFilterScreenListener(
    val onBackClick: () -> Unit,
    val onExportClick: () -> Unit,
    val onIncludingClick: () -> Unit,
    val onEnabledClick: () -> Unit,
    val onLogLevelsClick: () -> Unit,
    val onSelectAppClick: () -> Unit,
    val onSaveClick: () -> Unit,
    val onUidChange: (String) -> Unit,
    val onPidChange: (String) -> Unit,
    val onTidChange: (String) -> Unit,
    val onPackageNameChange: (String) -> Unit,
    val onTagChange: (String) -> Unit,
    val onContentChange: (String) -> Unit,
)

internal val MockEditFilterScreenListener = EditFilterScreenListener(
    onBackClick = { },
    onExportClick = { },
    onIncludingClick = { },
    onEnabledClick = { },
    onLogLevelsClick = { },
    onSelectAppClick = { },
    onSaveClick = { },
    onUidChange = { },
    onPidChange = { },
    onTidChange = { },
    onPackageNameChange = { },
    onTagChange = { },
    onContentChange = { },
)
