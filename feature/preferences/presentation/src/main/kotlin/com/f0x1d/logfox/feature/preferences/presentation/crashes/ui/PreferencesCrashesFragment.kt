package com.f0x1d.logfox.feature.preferences.presentation.crashes.ui

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.f0x1d.logfox.core.ui.compose.BaseComposeFragment
import com.f0x1d.logfox.feature.preferences.presentation.crashes.PreferencesCrashesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class PreferencesCrashesFragment : BaseComposeFragment() {

    private val viewModel by viewModels<PreferencesCrashesViewModel>()

    @Composable
    override fun Content() {
        PreferencesCrashesScreen(
            onBack = { findNavController().popBackStack() },
        )
    }
}
