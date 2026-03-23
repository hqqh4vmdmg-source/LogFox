package com.f0x1d.logfox.feature.preferences.presentation.ui.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.f0x1d.logfox.core.ui.compose.BaseComposeFragment
import com.f0x1d.logfox.feature.preferences.presentation.ui.settings.PreferencesUISideEffect
import com.f0x1d.logfox.feature.preferences.presentation.ui.settings.PreferencesUIViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class PreferencesUIFragment : BaseComposeFragment() {

    private val viewModel by viewModels<PreferencesUIViewModel>()

    @Composable
    override fun Content() {
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(viewModel) {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is PreferencesUISideEffect.RecreateActivity -> requireActivity().recreate()
                    else -> Unit
                }
            }
        }

        PreferencesUIScreen(
            state = state,
            onBack = { findNavController().popBackStack() },
            onCommand = { viewModel.send(it) },
        )
    }
}
