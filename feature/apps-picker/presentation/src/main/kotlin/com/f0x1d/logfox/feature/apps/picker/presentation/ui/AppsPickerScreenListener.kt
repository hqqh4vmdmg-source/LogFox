package com.f0x1d.logfox.feature.apps.picker.presentation.ui

import androidx.compose.runtime.Immutable
import com.f0x1d.logfox.feature.apps.picker.api.InstalledApp

@Immutable
data class AppsPickerScreenListener(
    val onBackClicked: () -> Unit,
    val onAppClicked: (InstalledApp) -> Unit,
    val onAppChecked: (InstalledApp, Boolean) -> Unit,
    val onSearchActiveChanged: (Boolean) -> Unit,
    val onQueryChanged: (String) -> Unit,
)

internal val MockAppsPickerScreenListener = AppsPickerScreenListener(
    onBackClicked = { },
    onAppClicked = { },
    onAppChecked = { _, _ -> },
    onSearchActiveChanged = { },
    onQueryChanged = { },
)
