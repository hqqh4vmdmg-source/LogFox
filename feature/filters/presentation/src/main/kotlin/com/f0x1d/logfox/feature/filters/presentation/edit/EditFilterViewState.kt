package com.f0x1d.logfox.feature.filters.presentation.edit

import androidx.compose.runtime.Immutable
import com.f0x1d.logfox.feature.filters.api.model.UserFilter

@Immutable
internal data class EditFilterViewState(
    val filter: UserFilter?,
    val including: Boolean,
    val enabled: Boolean,
    val enabledLogLevels: List<Boolean>,
    val uid: String?,
    val pid: String?,
    val tid: String?,
    val packageName: String?,
    val tag: String?,
    val content: String?,
)
