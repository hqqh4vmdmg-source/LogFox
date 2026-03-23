package com.f0x1d.logfox.feature.preferences.presentation.crashes.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.f0x1d.logfox.core.ui.compose.BaseComposeFragment
import com.f0x1d.logfox.feature.preferences.presentation.crashes.PreferencesCrashesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class PreferencesCrashesFragment : BaseComposeFragment() {

    private val viewModel by viewModels<PreferencesCrashesViewModel>()

    @Composable
    override fun Content() {
        val state by viewModel.state.collectAsStateWithLifecycle()

        PreferencesCrashesScreen(
            state = state,
            onBack = { findNavController().popBackStack() },
            onCommand = { viewModel.send(it) },
        )
    }
}
