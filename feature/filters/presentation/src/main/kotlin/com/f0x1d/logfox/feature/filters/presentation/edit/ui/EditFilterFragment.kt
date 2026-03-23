package com.f0x1d.logfox.feature.filters.presentation.edit.ui

import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.f0x1d.logfox.core.ui.compose.BaseComposeFragment
import com.f0x1d.logfox.core.ui.icons.Icons
import com.f0x1d.logfox.feature.filters.presentation.edit.EditFilterCommand
import com.f0x1d.logfox.feature.filters.presentation.edit.EditFilterSideEffect
import com.f0x1d.logfox.feature.filters.presentation.edit.EditFilterViewModel
import com.f0x1d.logfox.feature.filters.presentation.edit.ui.compose.EditFilterScreenContent
import com.f0x1d.logfox.feature.logging.api.model.LogLevel
import com.f0x1d.logfox.feature.navigation.api.Directions
import com.f0x1d.logfox.feature.strings.Strings
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class EditFilterFragment : BaseComposeFragment() {

    private val viewModel by hiltNavGraphViewModels<EditFilterViewModel>(
        Directions.editFilterFragment,
    )

    private val exportFilterLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("application/json"),
    ) { uri ->
        uri?.let { viewModel.send(EditFilterCommand.Export(it)) }
    }

    @Composable
    override fun Content() {
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(viewModel) {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is EditFilterSideEffect.NavigateToAppPicker -> {
                        findNavController().navigate(Directions.action_editFilterFragment_to_appsPickerFragment)
                    }

                    is EditFilterSideEffect.Close -> {
                        findNavController().popBackStack()
                    }

                    // Business logic side effects are handled by EffectHandler
                    else -> Unit
                }
            }
        }

        val listener = remember {
            EditFilterScreenListener(
                onBackClick = { findNavController().popBackStack() },
                onExportClick = { exportFilterLauncher.launch("filter.json") },
                onIncludingClick = { viewModel.send(EditFilterCommand.ToggleIncluding) },
                onEnabledClick = { viewModel.send(EditFilterCommand.ToggleEnabled) },
                onLogLevelsClick = { showLogLevelsDialog() },
                onSelectAppClick = { viewModel.send(EditFilterCommand.SelectApp) },
                onSaveClick = { viewModel.send(EditFilterCommand.Save) },
                onUidChange = { viewModel.send(EditFilterCommand.UpdateUid(it)) },
                onPidChange = { viewModel.send(EditFilterCommand.UpdatePid(it)) },
                onTidChange = { viewModel.send(EditFilterCommand.UpdateTid(it)) },
                onPackageNameChange = { viewModel.send(EditFilterCommand.UpdatePackageName(it)) },
                onTagChange = { viewModel.send(EditFilterCommand.UpdateTag(it)) },
                onContentChange = { viewModel.send(EditFilterCommand.UpdateContent(it)) },
            )
        }

        EditFilterScreenContent(
            state = state,
            listener = listener,
        )
    }

    private fun showLogLevelsDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(Strings.log_levels)
            .setIcon(Icons.ic_dialog_list)
            .setMultiChoiceItems(
                LogLevel.entries.map { it.name }.toTypedArray(),
                viewModel.state.value.enabledLogLevels.toTypedArray().toBooleanArray(),
            ) { _, which, checked ->
                viewModel.send(EditFilterCommand.FilterLevel(which, checked))
            }
            .setPositiveButton(Strings.close, null)
            .show()
    }
}
