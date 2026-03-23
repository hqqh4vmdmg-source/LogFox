package com.f0x1d.logfox.feature.preferences.presentation.service.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.f0x1d.logfox.core.context.toast
import com.f0x1d.logfox.core.ui.compose.BaseComposeFragment
import com.f0x1d.logfox.core.ui.icons.Icons
import com.f0x1d.logfox.feature.preferences.presentation.service.PreferencesServiceCommand
import com.f0x1d.logfox.feature.preferences.presentation.service.PreferencesServiceSideEffect
import com.f0x1d.logfox.feature.preferences.presentation.service.PreferencesServiceViewModel
import com.f0x1d.logfox.feature.strings.Strings
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class PreferencesServiceFragment : BaseComposeFragment() {

    private val viewModel by viewModels<PreferencesServiceViewModel>()

    @Composable
    override fun Content() {
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(viewModel) {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is PreferencesServiceSideEffect.ShowTerminalRestartDialog ->
                        showTerminalRestartDialog()

                    is PreferencesServiceSideEffect.ShowTerminalUnavailableToast ->
                        requireContext().toast(Strings.terminal_unavailable)

                    is PreferencesServiceSideEffect.ShowAndroid13WarningDialog ->
                        showAndroid13WarningDialog()

                    else -> Unit
                }
            }
        }

        PreferencesServiceScreen(
            state = state,
            onBack = { findNavController().popBackStack() },
            onTerminalSelected = { viewModel.send(PreferencesServiceCommand.TerminalSelected(it)) },
            onFallbackToDefaultChanged = { viewModel.send(PreferencesServiceCommand.FallbackToDefaultChanged(it)) },
            onStartOnBootChanged = { viewModel.send(PreferencesServiceCommand.StartOnBootChanged(it)) },
            onStopLoggingOnBackExitChanged = { viewModel.send(PreferencesServiceCommand.StopLoggingOnBackExitChanged(it)) },
            onShowLogsFromAppLaunchChanged = { viewModel.send(PreferencesServiceCommand.ShowLogsFromAppLaunchChanged(it)) },
            onIncludeDeviceInfoChanged = { viewModel.send(PreferencesServiceCommand.IncludeDeviceInfoChanged(it)) },
            onIncludeAppInfoChanged = { viewModel.send(PreferencesServiceCommand.IncludeAppInfoChanged(it)) },
            onExportLogsAsTxtChanged = { viewModel.send(PreferencesServiceCommand.ExportLogsAsTxtChanged(it)) },
        )
    }

    private fun showTerminalRestartDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setIcon(Icons.ic_dialog_terminal)
            .setTitle(Strings.new_terminal_selected)
            .setMessage(Strings.new_terminal_selected_question)
            .setPositiveButton(Strings.yes) { _, _ ->
                viewModel.send(PreferencesServiceCommand.ConfirmRestartLogging)
            }
            .setNeutralButton(Strings.no, null)
            .show()
    }

    private fun showAndroid13WarningDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setIcon(Icons.ic_dialog_warning)
            .setTitle(Strings.warning)
            .setMessage(Strings.android13_start_on_boot_warning)
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }
}
