package com.f0x1d.logfox.feature.preferences.presentation.notifications.ui

import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.f0x1d.logfox.core.ui.compose.BaseComposeFragment
import com.f0x1d.logfox.feature.notifications.api.LOGGING_STATUS_CHANNEL_ID
import com.f0x1d.logfox.feature.preferences.presentation.notifications.PreferencesNotificationsCommand
import com.f0x1d.logfox.feature.preferences.presentation.notifications.PreferencesNotificationsSideEffect
import com.f0x1d.logfox.feature.preferences.presentation.notifications.PreferencesNotificationsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class PreferencesNotificationsFragment : BaseComposeFragment() {

    private val viewModel by viewModels<PreferencesNotificationsViewModel>()

    @Composable
    override fun Content() {
        val state by viewModel.state.collectAsStateWithLifecycle()
        val lifecycleOwner = LocalLifecycleOwner.current

        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    viewModel.send(PreferencesNotificationsCommand.CheckPermission)
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
        }

        LaunchedEffect(viewModel) {
            viewModel.sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is PreferencesNotificationsSideEffect.OpenLoggingChannelSettings ->
                        startActivity(
                            Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                                putExtra(Settings.EXTRA_CHANNEL_ID, LOGGING_STATUS_CHANNEL_ID)
                            },
                        )

                    is PreferencesNotificationsSideEffect.OpenAppNotificationSettings ->
                        startActivity(
                            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                            },
                        )

                    else -> Unit
                }
            }
        }

        PreferencesNotificationsScreen(
            state = state,
            onBack = { findNavController().popBackStack() },
            onLoggingNotificationClick = {
                viewModel.send(PreferencesNotificationsCommand.OpenLoggingNotificationSettings)
            },
            onNotificationsPermissionClick = {
                viewModel.send(PreferencesNotificationsCommand.OpenNotificationsPermissionSettings)
            },
            onUseSeparateChannelsChanged = {
                viewModel.send(PreferencesNotificationsCommand.UseSeparateChannelsChanged(it))
            },
            onJavaNotificationsChanged = {
                viewModel.send(PreferencesNotificationsCommand.JavaNotificationsChanged(it))
            },
            onJniNotificationsChanged = { viewModel.send(PreferencesNotificationsCommand.JniNotificationsChanged(it)) },
            onAnrNotificationsChanged = { viewModel.send(PreferencesNotificationsCommand.AnrNotificationsChanged(it)) },
        )
    }
}
