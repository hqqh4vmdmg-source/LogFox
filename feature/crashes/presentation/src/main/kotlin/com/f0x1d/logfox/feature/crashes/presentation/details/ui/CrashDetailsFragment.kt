package com.f0x1d.logfox.feature.crashes.presentation.details.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.f0x1d.logfox.core.context.shareIntent
import com.f0x1d.logfox.core.copy.copyText
import com.f0x1d.logfox.core.ui.compose.BaseComposeFragment
import com.f0x1d.logfox.feature.crashes.presentation.details.CrashDetailsCommand
import com.f0x1d.logfox.feature.crashes.presentation.details.CrashDetailsSideEffect
import com.f0x1d.logfox.feature.crashes.presentation.details.CrashDetailsViewModel
import com.f0x1d.logfox.feature.crashes.presentation.details.ui.compose.CrashDetailsScreenContent
import com.f0x1d.logfox.feature.strings.Strings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class CrashDetailsFragment : BaseComposeFragment() {

    private val viewModel by viewModels<CrashDetailsViewModel>()

    private val zipCrashLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("application/zip"),
    ) { uri ->
        uri?.let { viewModel.send(CrashDetailsCommand.ExportCrashToZip(it)) }
    }

    // no plain because android will append .txt itself
    private val exportCrashLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("text/*"),
    ) { uri ->
        uri?.let { viewModel.send(CrashDetailsCommand.ExportCrashToFile(it)) }
    }

    @SuppressLint("InlinedApi")
    @Composable
    override fun Content() {
        val state by viewModel.state.collectAsStateWithLifecycle()
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        var showBlacklistDialog by remember { mutableStateOf(false) }
        var showDeleteDialog by remember { mutableStateOf(false) }

        LaunchedEffect(viewModel) {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is CrashDetailsSideEffect.ConfirmBlacklist -> showBlacklistDialog = true

                    is CrashDetailsSideEffect.ConfirmDelete -> showDeleteDialog = true

                    is CrashDetailsSideEffect.OpenAppInfo -> startActivity(
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", sideEffect.packageName, null)
                        },
                    )

                    is CrashDetailsSideEffect.OpenNotificationSettings -> startActivity(
                        Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                            putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                            putExtra(Settings.EXTRA_CHANNEL_ID, sideEffect.channelId)
                        },
                    )

                    is CrashDetailsSideEffect.CopyText -> {
                        requireContext().copyText(sideEffect.text)
                        scope.launch {
                            snackbarHostState.showSnackbar(getString(Strings.text_copied))
                        }
                    }

                    is CrashDetailsSideEffect.ShareCrashLog -> {
                        requireContext().shareIntent(sideEffect.text)
                    }

                    is CrashDetailsSideEffect.Close -> {
                        findNavController().popBackStack()
                    }

                    is CrashDetailsSideEffect.LaunchFileExportPicker -> {
                        exportCrashLauncher.launch(sideEffect.filename)
                    }

                    is CrashDetailsSideEffect.LaunchZipExportPicker -> {
                        zipCrashLauncher.launch(sideEffect.filename)
                    }

                    else -> Unit
                }
            }
        }

        if (showBlacklistDialog) {
            AlertDialog(
                onDismissRequest = { showBlacklistDialog = false },
                title = { Text(text = stringResource(Strings.blacklist)) },
                text = { Text(text = stringResource(Strings.warning_blacklist)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showBlacklistDialog = false
                            viewModel.send(CrashDetailsCommand.ConfirmBlacklist)
                        },
                    ) {
                        Text(text = stringResource(Strings.yes))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showBlacklistDialog = false }) {
                        Text(text = stringResource(Strings.no))
                    }
                },
            )
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text(text = stringResource(Strings.delete)) },
                text = { Text(text = stringResource(Strings.delete_warning)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            viewModel.send(CrashDetailsCommand.ConfirmDelete)
                        },
                    ) {
                        Text(text = stringResource(Strings.yes))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text(text = stringResource(Strings.no))
                    }
                },
            )
        }

        val listener = remember(viewModel) {
            CrashDetailsScreenListener(
                onBackClick = { findNavController().popBackStack() },
                onWrapLinesClick = { viewModel.send(CrashDetailsCommand.WrapLinesClicked) },
                onInfoClick = { viewModel.send(CrashDetailsCommand.OpenAppInfoClicked) },
                onNotificationsClick = { viewModel.send(CrashDetailsCommand.OpenNotificationSettingsClicked) },
                onBlacklistClick = { viewModel.send(CrashDetailsCommand.BlacklistClicked) },
                onDeleteClick = { viewModel.send(CrashDetailsCommand.DeleteClicked) },
                onSearchQueryChange = { query -> viewModel.send(CrashDetailsCommand.SearchInLog(query)) },
                onCopyClick = { viewModel.send(CrashDetailsCommand.CopyCrashLog) },
                onShareClick = { viewModel.send(CrashDetailsCommand.ShareCrashLog) },
                onExportClick = { viewModel.send(CrashDetailsCommand.ExportCrashToFileClicked) },
                onZipClick = { viewModel.send(CrashDetailsCommand.ExportCrashToZipClicked) },
            )
        }

        CrashDetailsScreenContent(
            state = state,
            listener = listener,
            snackbarHostState = snackbarHostState,
        )
    }
}
