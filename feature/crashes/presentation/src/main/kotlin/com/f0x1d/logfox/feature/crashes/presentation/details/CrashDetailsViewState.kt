package com.f0x1d.logfox.feature.crashes.presentation.details

import androidx.compose.runtime.Immutable
import com.f0x1d.logfox.feature.crashes.api.model.AppCrash

@Immutable
internal data class CrashDetailsViewState(
    val crash: AppCrash?,
    val crashLog: String?,
    val blacklisted: Boolean?,
    val wrapCrashLogLines: Boolean,
    val useSeparateNotificationsChannelsForCrashes: Boolean,
    val searchQuery: String,
    val searchMatchRanges: List<IntRange>,
)
