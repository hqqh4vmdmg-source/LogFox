package com.f0x1d.logfox.feature.crashes.presentation.appcrashes.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.f0x1d.logfox.core.ui.compose.BaseComposeFragment
import com.f0x1d.logfox.core.ui.dialog.showAreYouSureDeleteDialog
import com.f0x1d.logfox.feature.crashes.presentation.appcrashes.AppCrashesCommand
import com.f0x1d.logfox.feature.crashes.presentation.appcrashes.AppCrashesSideEffect
import com.f0x1d.logfox.feature.crashes.presentation.appcrashes.AppCrashesViewModel
import com.f0x1d.logfox.feature.crashes.presentation.appcrashes.ui.compose.AppCrashesScreenContent
import com.f0x1d.logfox.feature.navigation.api.Directions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class AppCrashesFragment : BaseComposeFragment() {

    private val viewModel by viewModels<AppCrashesViewModel>()

    @Composable
    override fun Content() {
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(viewModel) {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is AppCrashesSideEffect.NavigateToCrashDetails -> findNavController().navigate(
                        resId = Directions.action_appCrashesFragment_to_crashDetailsFragment,
                        args = bundleOf("crash_id" to sideEffect.crashId),
                    )

                    else -> Unit
                }
            }
        }

        val listener = remember(viewModel) {
            AppCrashesScreenListener(
                onBackClick = { findNavController().popBackStack() },
                onCrashClick = { item ->
                    viewModel.send(AppCrashesCommand.CrashClicked(item.lastCrashId))
                },
                onDeleteCrashClick = { item ->
                    showAreYouSureDeleteDialog {
                        viewModel.send(AppCrashesCommand.DeleteCrash(item.lastCrashId))
                    }
                },
            )
        }

        AppCrashesScreenContent(
            state = state,
            listener = listener,
        )
    }
}
