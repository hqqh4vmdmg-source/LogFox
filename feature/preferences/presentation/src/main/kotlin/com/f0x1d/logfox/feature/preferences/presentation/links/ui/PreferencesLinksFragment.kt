package com.f0x1d.logfox.feature.preferences.presentation.links.ui

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.f0x1d.logfox.core.ui.compose.BaseComposeFragment
import com.f0x1d.logfox.feature.preferences.presentation.links.PreferencesLinksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class PreferencesLinksFragment : BaseComposeFragment() {

    private val viewModel by viewModels<PreferencesLinksViewModel>()

    @Composable
    override fun Content() {
        PreferencesLinksScreen(
            onBack = { findNavController().popBackStack() },
        )
    }
}
