package com.f0x1d.logfox.feature.crashes.presentation.list.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.os.bundleOf
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.f0x1d.logfox.core.ui.compose.BaseComposeFragment
import com.f0x1d.logfox.core.ui.dialog.showAreYouSureClearDialog
import com.f0x1d.logfox.core.ui.dialog.showAreYouSureDeleteDialog
import com.f0x1d.logfox.feature.crashes.presentation.list.CrashesCommand
import com.f0x1d.logfox.feature.crashes.presentation.list.CrashesSideEffect
import com.f0x1d.logfox.feature.crashes.presentation.list.CrashesViewModel
import com.f0x1d.logfox.feature.crashes.presentation.list.ui.compose.CrashesScreenContent
import com.f0x1d.logfox.feature.navigation.api.Directions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class CrashesFragment : BaseComposeFragment() {

    private val viewModel by hiltNavGraphViewModels<CrashesViewModel>(Directions.crashesFragment)

    @Composable
    override fun Content() {
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(viewModel) {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is CrashesSideEffect.NavigateToCrashDetails -> {
                        findNavController().navigate(
                            resId = Directions.action_crashesFragment_to_crashDetailsFragment,
                            args = bundleOf("crash_id" to sideEffect.crashId),
                        )
                    }

                    is CrashesSideEffect.NavigateToAppCrashes -> {
                        findNavController().navigate(
                            resId = Directions.action_crashesFragment_to_appCrashesFragment,
                            args = bundleOf(
                                "package_name" to sideEffect.packageName,
                                "app_name" to sideEffect.appName,
                            ),
                        )
                    }

                    is CrashesSideEffect.NavigateToBlacklist ->
                        findNavController().navigate(Directions.action_crashesFragment_to_appsPickerFragment)

                    else -> Unit
                }
            }
        }

        val listener = remember(viewModel) {
            CrashesScreenListener(
                onCrashClick = { item ->
                    viewModel.send(
                        CrashesCommand.CrashClicked(
                            crashId = item.lastCrashId,
                            count = item.count,
                            packageName = item.packageName,
                            appName = item.appName,
                        ),
                    )
                },
                onDeleteCrashClick = { item ->
                    showAreYouSureDeleteDialog {
                        viewModel.send(CrashesCommand.DeleteCrashesByPackageName(item.packageName))
                    }
                },
                onSearchedCrashClick = { item ->
                    viewModel.send(CrashesCommand.SearchedCrashClicked(item.lastCrashId))
                },
                onDeleteSearchedCrashClick = { item ->
                    showAreYouSureDeleteDialog {
                        viewModel.send(CrashesCommand.DeleteCrash(item.lastCrashId))
                    }
                },
                onQueryChange = { query ->
                    viewModel.send(CrashesCommand.UpdateQuery(query))
                },
                onSortConfirmed = { sortType, sortInReversedOrder ->
                    viewModel.send(CrashesCommand.UpdateSort(sortType, sortInReversedOrder))
                },
                onBlacklistClick = {
                    viewModel.send(CrashesCommand.OpenBlacklist)
                },
                onClearClick = {
                    showAreYouSureClearDialog {
                        viewModel.send(CrashesCommand.ClearCrashes)
                    }
                },
            )
        }

        CrashesScreenContent(
            state = state,
            listener = listener,
        )
    }
}
