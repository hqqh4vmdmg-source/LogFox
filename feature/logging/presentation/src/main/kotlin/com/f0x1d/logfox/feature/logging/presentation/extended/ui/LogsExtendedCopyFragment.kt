package com.f0x1d.logfox.feature.logging.presentation.extended.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.f0x1d.logfox.core.ui.compose.BaseComposeFragment
import com.f0x1d.logfox.feature.logging.presentation.extended.LogsExtendedCopyViewModel
import com.f0x1d.logfox.feature.logging.presentation.extended.ui.compose.LogsExtendedCopyScreenContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class LogsExtendedCopyFragment : BaseComposeFragment() {

    private val viewModel by viewModels<LogsExtendedCopyViewModel>()

    @Composable
    override fun Content() {
        val state by viewModel.state.collectAsStateWithLifecycle()

        LogsExtendedCopyScreenContent(
            state = state,
            onNavigateBack = { findNavController().navigateUp() },
        )
    }
}
