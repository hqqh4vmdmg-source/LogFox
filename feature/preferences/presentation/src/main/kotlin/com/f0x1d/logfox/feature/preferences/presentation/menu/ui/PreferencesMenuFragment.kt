package com.f0x1d.logfox.feature.preferences.presentation.menu.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.f0x1d.logfox.core.context.shareFileIntent
import com.f0x1d.logfox.core.ui.compose.BaseComposeFragment
import com.f0x1d.logfox.feature.navigation.api.Directions
import com.f0x1d.logfox.feature.preferences.presentation.menu.PreferencesMenuCommand
import com.f0x1d.logfox.feature.preferences.presentation.menu.PreferencesMenuSideEffect
import com.f0x1d.logfox.feature.preferences.presentation.menu.PreferencesMenuViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class PreferencesMenuFragment : BaseComposeFragment() {

    private val viewModel by viewModels<PreferencesMenuViewModel>()

    @Composable
    override fun Content() {
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(viewModel) {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is PreferencesMenuSideEffect.NavigateToUISettings ->
                        findNavController().navigate(Directions.action_settingsMenuFragment_to_settingsUIFragment)

                    is PreferencesMenuSideEffect.NavigateToServiceSettings ->
                        findNavController().navigate(Directions.action_settingsMenuFragment_to_settingsServiceFragment)

                    is PreferencesMenuSideEffect.NavigateToCrashesSettings ->
                        findNavController().navigate(Directions.action_settingsMenuFragment_to_settingsCrashesFragment)

                    is PreferencesMenuSideEffect.NavigateToNotificationsSettings ->
                        findNavController().navigate(Directions.action_settingsMenuFragment_to_settingsNotificationsFragment)

                    is PreferencesMenuSideEffect.NavigateToLinks ->
                        findNavController().navigate(Directions.action_settingsMenuFragment_to_settingsLinksFragment)

                    is PreferencesMenuSideEffect.ShareLogs ->
                        requireContext().shareFileIntent(sideEffect.file)
                }
            }
        }

        PreferencesMenuScreen(
            state = state,
            onUISettings = { viewModel.send(PreferencesMenuCommand.UISettingsClicked) },
            onServiceSettings = { viewModel.send(PreferencesMenuCommand.ServiceSettingsClicked) },
            onCrashesSettings = { viewModel.send(PreferencesMenuCommand.CrashesSettingsClicked) },
            onNotificationsSettings = { viewModel.send(PreferencesMenuCommand.NotificationsSettingsClicked) },
            onLinks = { viewModel.send(PreferencesMenuCommand.LinksClicked) },
            onShareLogs = { viewModel.send(PreferencesMenuCommand.ShareLogsClicked) },
        )
    }
}
