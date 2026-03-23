package com.f0x1d.logfox.feature.filters.presentation.list.ui

import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.f0x1d.logfox.core.ui.compose.BaseComposeFragment
import com.f0x1d.logfox.core.ui.dialog.showAreYouSureClearDialog
import com.f0x1d.logfox.core.ui.dialog.showAreYouSureDeleteDialog
import com.f0x1d.logfox.feature.filters.presentation.list.FiltersCommand
import com.f0x1d.logfox.feature.filters.presentation.list.FiltersSideEffect
import com.f0x1d.logfox.feature.filters.presentation.list.FiltersViewModel
import com.f0x1d.logfox.feature.filters.presentation.list.ui.compose.FiltersScreenContent
import com.f0x1d.logfox.feature.navigation.api.Directions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class FiltersFragment : BaseComposeFragment() {

    private val viewModel by viewModels<FiltersViewModel>()

    private val importFiltersLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument(),
    ) { uri ->
        uri?.let { viewModel.send(FiltersCommand.Import(it)) }
    }

    private val exportFiltersLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("application/json"),
    ) { uri ->
        uri?.let { viewModel.send(FiltersCommand.ExportAll(it)) }
    }

    @Composable
    override fun Content() {
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(viewModel) {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is FiltersSideEffect.NavigateToEditFilter -> {
                        findNavController().navigate(
                            resId = Directions.action_filtersFragment_to_editFilterFragment,
                            args = bundleOf("filter_id" to sideEffect.filterId),
                        )
                    }

                    is FiltersSideEffect.NavigateToCreateFilter -> {
                        findNavController().navigate(Directions.action_filtersFragment_to_editFilterFragment)
                    }

                    // Business logic side effects - handled by EffectHandler
                    else -> Unit
                }
            }
        }

        val listener = remember {
            FiltersScreenListener(
                onBackClick = { findNavController().popBackStack() },
                onFilterClick = { viewModel.send(FiltersCommand.OpenFilter(it.id)) },
                onFilterDelete = { filter ->
                    showAreYouSureDeleteDialog {
                        viewModel.send(FiltersCommand.Delete(filter))
                    }
                },
                onFilterChecked = { filter, checked ->
                    viewModel.send(FiltersCommand.Switch(filter, checked))
                },
                onAddClick = { viewModel.send(FiltersCommand.CreateNewFilter) },
                onClearClick = {
                    showAreYouSureClearDialog {
                        viewModel.send(FiltersCommand.ClearAll)
                    }
                },
                onImportClick = {
                    importFiltersLauncher.launch(arrayOf("application/json", "*/*"))
                },
                onExportAllClick = {
                    exportFiltersLauncher.launch("filters.json")
                },
            )
        }

        FiltersScreenContent(
            state = state,
            listener = listener,
        )
    }
}
