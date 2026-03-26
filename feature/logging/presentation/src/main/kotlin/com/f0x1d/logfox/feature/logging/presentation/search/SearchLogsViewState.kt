package com.f0x1d.logfox.feature.logging.presentation.search

import androidx.compose.runtime.Immutable

@Immutable
internal data class SearchLogsViewState(
    val query: String?,
    val caseSensitive: Boolean,
)
