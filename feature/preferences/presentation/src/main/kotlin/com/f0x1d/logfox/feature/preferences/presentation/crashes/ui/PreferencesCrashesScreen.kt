package com.f0x1d.logfox.feature.preferences.presentation.crashes.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.preference.PreferenceManager
import com.f0x1d.logfox.compose.designsystem.component.button.NavigationBackButton
import com.f0x1d.logfox.feature.preferences.presentation.ui.components.SettingsCategoryHeader
import com.f0x1d.logfox.feature.preferences.presentation.ui.components.SettingsSwitchRow
import com.f0x1d.logfox.feature.strings.Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PreferencesCrashesScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val prefs = remember(context) { PreferenceManager.getDefaultSharedPreferences(context) }

    var collectJava by remember { mutableStateOf(prefs.getBoolean("pref_collect_java", true)) }
    var collectJni by remember { mutableStateOf(prefs.getBoolean("pref_collect_jni", true)) }
    var collectAnr by remember { mutableStateOf(prefs.getBoolean("pref_collect_anr", true)) }

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
                    checked = collectJava,
                    onCheckedChange = { newValue ->
                        prefs.edit().putBoolean("pref_collect_java", newValue).apply()
                        collectJava = newValue
                    },
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.collect_jni_crashes),
                    checked = collectJni,
                    onCheckedChange = { newValue ->
                        prefs.edit().putBoolean("pref_collect_jni", newValue).apply()
                        collectJni = newValue
                    },
                )
            }
            item {
                SettingsSwitchRow(
                    title = stringResource(Strings.collect_anr),
                    checked = collectAnr,
                    onCheckedChange = { newValue ->
                        prefs.edit().putBoolean("pref_collect_anr", newValue).apply()
                        collectAnr = newValue
                    },
                )
            }
        }
    }
}
