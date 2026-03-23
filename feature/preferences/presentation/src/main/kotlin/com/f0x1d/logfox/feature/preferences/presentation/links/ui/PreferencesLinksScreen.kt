package com.f0x1d.logfox.feature.preferences.presentation.links.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.f0x1d.logfox.compose.designsystem.component.button.NavigationBackButton
import com.f0x1d.logfox.core.ui.icons.Icons
import com.f0x1d.logfox.feature.preferences.presentation.ui.components.SettingsCategoryHeader
import com.f0x1d.logfox.feature.preferences.presentation.ui.components.SettingsLinkRow
import com.f0x1d.logfox.feature.strings.Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PreferencesLinksScreen(onBack: () -> Unit) {
    val context = LocalContext.current

    fun openUrl(url: String) {
        context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(Strings.links)) },
                navigationIcon = { NavigationBackButton(onClick = onBack) },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = paddingValues,
        ) {
            item {
                SettingsCategoryHeader(title = stringResource(Strings.about_app))
            }
            item {
                SettingsLinkRow(
                    title = stringResource(Strings.developer),
                    iconRes = Icons.ic_settings_person,
                    onClick = { openUrl("https://t.me/f0x1d") },
                )
            }
            item {
                SettingsLinkRow(
                    title = stringResource(Strings.source_code),
                    iconRes = Icons.ic_settings_code,
                    onClick = { openUrl("https://github.com/F0x1d/LogFox") },
                )
            }
            item {
                SettingsCategoryHeader(title = stringResource(Strings.telegram))
            }
            item {
                SettingsLinkRow(
                    title = stringResource(Strings.releases),
                    iconRes = Icons.ic_settings_releases,
                    onClick = { openUrl("https://t.me/f0x1dsshit") },
                )
            }
            item {
                SettingsLinkRow(
                    title = stringResource(Strings.alpha_builds),
                    iconRes = Icons.ic_settings_handyman,
                    onClick = { openUrl("https://t.me/f0x1dsshit_ci") },
                )
            }
        }
    }
}
