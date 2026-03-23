package com.f0x1d.logfox.feature.logging.presentation.list.ui

import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.f0x1d.logfox.core.copy.copyText
import com.f0x1d.logfox.core.ui.compose.BaseComposeFragment
import com.f0x1d.logfox.feature.logging.presentation.list.LogsCommand
import com.f0x1d.logfox.feature.logging.presentation.list.LogsSideEffect
import com.f0x1d.logfox.feature.logging.presentation.list.LogsViewModel
import com.f0x1d.logfox.feature.logging.presentation.list.ui.compose.LogsScreenContent
import com.f0x1d.logfox.feature.navigation.api.Directions
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState

@AndroidEntryPoint
internal class LogsFragment : BaseComposeFragment() {

    private val viewModel by viewModels<LogsViewModel>()

    private val exportLogsLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("text/*"),
    ) { uri ->
        uri?.let { viewModel.send(LogsCommand.ExportSelectedTo(it)) }
    }

    @Composable
    override fun Content() {
        val state by viewModel.state.collectAsStateWithLifecycle()
        val stateRef = rememberUpdatedState(state)

        LaunchedEffect(viewModel) {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is LogsSideEffect.NavigateToRecordings ->
                        findNavController().navigate(Directions.action_global_recordingsFragment)

                    is LogsSideEffect.NavigateToSearch ->
                        findNavController().navigate(Directions.action_logsFragment_to_searchBottomSheet)

                    is LogsSideEffect.OpenFilters ->
                        findNavController().navigate(Directions.action_logsFragment_to_filtersFragment)

                    is LogsSideEffect.NavigateToExtendedCopy ->
                        findNavController().navigate(Directions.action_logsFragment_to_logsExtendedCopyFragment)

                    is LogsSideEffect.OpenEditFilter ->
                        findNavController().navigate(
                            resId = Directions.action_filtersFragment_to_editFilterFragment,
                            args = bundleOf("filter_id" to sideEffect.filterId),
                        )

                    is LogsSideEffect.OpenEditFilterFromLogLine ->
                        findNavController().navigate(
                            resId = Directions.action_filtersFragment_to_editFilterFragment,
                            args = bundleOf(
                                "log_uid" to sideEffect.uid,
                                "log_pid" to sideEffect.pid,
                                "log_tid" to sideEffect.tid,
                                "log_package_name" to sideEffect.packageName,
                                "log_tag" to sideEffect.tag,
                                "log_content" to sideEffect.content,
                                "log_level" to sideEffect.level.ordinal,
                            ),
                        )

                    is LogsSideEffect.CopyText ->
                        requireContext().copyText(sideEffect.text)

                    is LogsSideEffect.LaunchExportPicker ->
                        exportLogsLauncher.launch(sideEffect.filename)

                    else -> Unit
                }
            }
        }

        val listener = remember {
            LogsScreenListener(
                onItemClick = { viewModel.send(LogsCommand.ItemClicked(it)) },
                onSelectClick = { viewModel.send(LogsCommand.SelectLine(it, true)) },
                onCopyClick = { viewModel.send(LogsCommand.CopyLog(it)) },
                onCreateFilterClick = { viewModel.send(LogsCommand.CreateFilterFromLog(it)) },
                onPauseResumeClick = { viewModel.send(LogsCommand.SwitchState) },
                onSelectAll = {
                    val visibleIds = stateRef.value.logs.mapTo(mutableSetOf()) { it.logLineId }
                    viewModel.send(LogsCommand.SelectAll(visibleIds))
                },
                onClearSelection = { viewModel.send(LogsCommand.ClearSelection) },
                onOpenSearch = { viewModel.send(LogsCommand.OpenSearch) },
                onOpenFilters = { viewModel.send(LogsCommand.OpenFiltersScreen) },
                onCopySelected = { viewModel.send(LogsCommand.CopySelectedLogs) },
                onExtendedCopy = { viewModel.send(LogsCommand.OpenExtendedCopy) },
                onSelectedToRecording = { viewModel.send(LogsCommand.SelectedToRecording) },
                onExportSelected = { viewModel.send(LogsCommand.ExportSelectedClicked) },
                onClearLogs = { viewModel.send(LogsCommand.ClearLogs) },
                onRestartLogging = { viewModel.send(LogsCommand.RestartLogging) },
                onKillService = { viewModel.send(LogsCommand.KillService) },
                onToolbarClick = { viewModel.send(LogsCommand.ToolbarClicked) },
                onScrollStarted = { viewModel.send(LogsCommand.Pause) },
                onScrollEnded = { isAtBottom ->
                    val currentState = stateRef.value
                    if (currentState.paused && isAtBottom && currentState.resumeLoggingWithBottomTouch) {
                        viewModel.send(LogsCommand.Resume)
                    } else if (!isAtBottom) {
                        viewModel.send(LogsCommand.Pause)
                    }
                },
                onScrollFabClick = {
                    viewModel.send(LogsCommand.Resume)
                },
            )
        }

        LogsScreenContent(state = state, listener = listener)
    }
}
