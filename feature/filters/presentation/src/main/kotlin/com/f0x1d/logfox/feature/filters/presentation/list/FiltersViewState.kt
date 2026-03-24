package com.f0x1d.logfox.feature.filters.presentation.list

import androidx.compose.runtime.Immutable
import com.f0x1d.logfox.feature.filters.api.model.UserFilter

@Immutable
internal data class FiltersViewState(val filters: List<UserFilter>)
