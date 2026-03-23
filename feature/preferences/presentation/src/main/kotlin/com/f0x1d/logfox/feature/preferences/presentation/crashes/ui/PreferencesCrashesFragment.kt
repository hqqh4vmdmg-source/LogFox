package com.f0x1d.logfox.feature.preferences.presentation.crashes.ui

import androidx.compose.runtime.Composable
import androidx.navigation.fragment.findNavController
import com.f0x1d.logfox.core.ui.compose.BaseComposeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class PreferencesCrashesFragment : BaseComposeFragment() {

    @Composable
    override fun Content() {
        PreferencesCrashesScreen(
            onBack = { findNavController().popBackStack() },
        )
    }
}
