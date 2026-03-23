package com.f0x1d.logfox.feature.preferences.presentation.crashes.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.f0x1d.logfox.compose.designsystem.component.button.NavigationBackButton
import com.f0x1d.logfox.feature.preferences.presentation.crashes.PreferencesCrashesCommand
import com.f0x1d.logfox.feature.preferences.presentation.crashes.PreferencesCrashesViewState
import com.f0x1d.logfox.feature.preferences.presentation.ui.components.SettingsCategoryHeader
import com.f0x1d.logfox.feature.preferences.presentation.ui.components.SettingsSwitchRow
import com.f0x1d.logfox.feature.strings.Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PreferencesCrashesScreen(
    state: PreferencesCrashesViewState,
    onBack: () -> Unit,
    onCommand: (PreferencesCrashesCommand) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(Strings.crashes)) },
                navigationIcon = { NavigationBackButton(onClick = onBack) },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues,
        ) {
            item {
                SettingsCategoryHeader(title = stringResource(Strings.crashes))
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.collect_java_crashes),
                    checked = state.collectJava,
                    onCheckedChange = { newValue ->
                        onCommand(PreferencesCrashesCommand.CollectJavaChanged(newValue))
                    },
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.collect_jni_crashes),
                    checked = state.collectJni,
                    onCheckedChange = { newValue ->
                        onCommand(PreferencesCrashesCommand.CollectJniChanged(newValue))
                    },
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.collect_anr),
                    checked = state.collectAnr,
                    onCheckedChange = { newValue ->
                        onCommand(PreferencesCrashesCommand.CollectAnrChanged(newValue))
                    },
                )
            }
        }
    }
}
