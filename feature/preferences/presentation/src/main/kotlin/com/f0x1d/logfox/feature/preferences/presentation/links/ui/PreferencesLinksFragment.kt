package com.f0x1d.logfox.feature.preferences.presentation.links.ui

import androidx.compose.runtime.Composable
import androidx.navigation.fragment.findNavController
import com.f0x1d.logfox.core.ui.compose.BaseComposeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class PreferencesLinksFragment : BaseComposeFragment() {

    @Composable
    override fun Content() {
        PreferencesLinksScreen(
            onBack = { findNavController().popBackStack() },
        )
    }
}
