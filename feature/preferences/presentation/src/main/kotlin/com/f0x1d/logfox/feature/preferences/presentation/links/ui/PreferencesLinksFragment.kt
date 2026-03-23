package com.f0x1d.logfox.feature.preferences.presentation.links.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.f0x1d.logfox.core.ui.compose.BaseComposeFragment
import com.f0x1d.logfox.feature.preferences.presentation.links.PreferencesLinksSideEffect
import com.f0x1d.logfox.feature.preferences.presentation.links.PreferencesLinksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class PreferencesLinksFragment : BaseComposeFragment() {

    private val viewModel by viewModels<PreferencesLinksViewModel>()

    @Composable
    override fun Content() {
        LaunchedEffect(viewModel) {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is PreferencesLinksSideEffect.OpenUrl -> {
                        requireContext().startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse(sideEffect.url)).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            },
                        )
                    }
                }
            }
        }

        PreferencesLinksScreen(
            onBack = { findNavController().popBackStack() },
            onCommand = { viewModel.send(it) },
        )
    }
}
