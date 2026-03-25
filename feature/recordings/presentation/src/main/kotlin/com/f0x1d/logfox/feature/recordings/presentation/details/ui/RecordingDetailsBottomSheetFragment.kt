package com.f0x1d.logfox.feature.recordings.presentation.details.ui

import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.f0x1d.logfox.core.context.shareFileIntent
import com.f0x1d.logfox.core.ui.compose.BaseComposeBottomSheetFragment
import com.f0x1d.logfox.feature.recordings.presentation.details.RecordingDetailsCommand
import com.f0x1d.logfox.feature.recordings.presentation.details.RecordingDetailsSideEffect
import com.f0x1d.logfox.feature.recordings.presentation.details.RecordingDetailsViewModel
import com.f0x1d.logfox.feature.recordings.presentation.details.ui.compose.RecordingDetailsContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class RecordingDetailsBottomSheetFragment : BaseComposeBottomSheetFragment() {

    private val viewModel by viewModels<RecordingDetailsViewModel>()

    // no plain because android will append .txt itself
    private val logExportLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("text/*"),
    ) { uri ->
        uri?.let { viewModel.send(RecordingDetailsCommand.ExportFile(it)) }
    }

    private val zipLogLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("application/zip"),
    ) { uri ->
        uri?.let { viewModel.send(RecordingDetailsCommand.ExportZipFile(it)) }
    }

    @Composable
    override fun Content() {
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.sideEffects.collect { effect ->
                when (effect) {
                    is RecordingDetailsSideEffect.LaunchFileExportPicker ->
                        logExportLauncher.launch(effect.filename)

                    is RecordingDetailsSideEffect.LaunchZipExportPicker ->
                        zipLogLauncher.launch(effect.filename)

                    is RecordingDetailsSideEffect.ShareFile ->
                        requireContext().shareFileIntent(effect.file)

                    else -> Unit
                }
            }
        }

        RecordingDetailsContent(
            state = state,
            onTitleChanged = { viewModel.send(RecordingDetailsCommand.UpdateTitle(it)) },
            onExportClick = { viewModel.send(RecordingDetailsCommand.ExportFileClicked) },
            onShareClick = { viewModel.send(RecordingDetailsCommand.ShareRecording) },
            onZipClick = { viewModel.send(RecordingDetailsCommand.ExportZipClicked) },
        )
    }
}
