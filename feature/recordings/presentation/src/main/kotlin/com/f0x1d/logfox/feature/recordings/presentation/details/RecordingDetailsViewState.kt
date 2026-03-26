package com.f0x1d.logfox.feature.recordings.presentation.details

import androidx.compose.runtime.Immutable
import com.f0x1d.logfox.feature.recordings.presentation.model.LogRecordingItem

@Immutable
internal data class RecordingDetailsViewState(
    val recordingItem: LogRecordingItem?,
    val currentTitle: String?,
)
