package com.f0x1d.logfox.feature.logging.presentation.list.ui

internal data class LogsScreenListener(
    val onItemClick: (Long) -> Unit = {},
    val onSelectClick: (Long) -> Unit = {},
    val onCopyClick: (Long) -> Unit = {},
    val onCreateFilterClick: (Long) -> Unit = {},
    val onPauseResumeClick: () -> Unit = {},
    val onSelectAll: () -> Unit = {},
    val onClearSelection: () -> Unit = {},
    val onOpenSearch: () -> Unit = {},
    val onOpenFilters: () -> Unit = {},
    val onCopySelected: () -> Unit = {},
    val onExtendedCopy: () -> Unit = {},
    val onSelectedToRecording: () -> Unit = {},
    val onExportSelected: () -> Unit = {},
    val onClearLogs: () -> Unit = {},
    val onRestartLogging: () -> Unit = {},
    val onKillService: () -> Unit = {},
    val onToolbarClick: () -> Unit = {},
    val onScrollStarted: () -> Unit = {},
    val onScrollEnded: (isAtBottom: Boolean) -> Unit = {},
    val onScrollFabClick: () -> Unit = {},
)
