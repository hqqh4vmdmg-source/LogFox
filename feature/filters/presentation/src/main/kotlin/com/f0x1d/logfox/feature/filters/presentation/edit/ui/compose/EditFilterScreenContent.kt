package com.f0x1d.logfox.feature.filters.presentation.edit.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.f0x1d.logfox.compose.designsystem.component.button.NavigationBackButton
import com.f0x1d.logfox.core.ui.icons.Icons
import com.f0x1d.logfox.feature.filters.presentation.edit.EditFilterViewState
import com.f0x1d.logfox.feature.filters.presentation.edit.ui.EditFilterScreenListener
import com.f0x1d.logfox.feature.filters.presentation.edit.ui.MockEditFilterScreenListener
import com.f0x1d.logfox.feature.strings.Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditFilterScreenContent(
    modifier: Modifier = Modifier,
    state: EditFilterViewState,
    listener: EditFilterScreenListener = MockEditFilterScreenListener,
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state = topAppBarState)

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            EditFilterTopBar(
                showExport = state.filter != null,
                onBackClick = listener.onBackClick,
                onExportClick = listener.onExportClick,
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.imePadding(),
                onClick = listener.onSaveClick,
            ) {
                Icon(
                    painter = painterResource(Icons.ic_save),
                    contentDescription = null,
                )
            }
        },
        contentWindowInsets = WindowInsets.statusBars,
    ) { contentPadding ->
        EditFilterForm(
            modifier = Modifier
                .padding(contentPadding)
                .imePadding(),
            state = state,
            listener = listener,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditFilterTopBar(
    showExport: Boolean,
    onBackClick: () -> Unit,
    onExportClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { Text(text = stringResource(Strings.filter)) },
        navigationIcon = { NavigationBackButton(onClick = onBackClick) },
        actions = {
            if (showExport) {
                var showMenu by remember { mutableStateOf(false) }
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(
                        painter = painterResource(Icons.ic_menu_overflow),
                        contentDescription = null,
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(Strings.export)) },
                        onClick = {
                            showMenu = false
                            onExportClick()
                        },
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@Composable
private fun EditFilterForm(
    state: EditFilterViewState,
    listener: EditFilterScreenListener,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        EnabledButton(
            enabled = state.enabled,
            onClick = listener.onEnabledClick,
        )

        IncludingButton(
            including = state.including,
            onClick = listener.onIncludingClick,
        )

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = listener.onLogLevelsClick,
        ) {
            Icon(
                painter = painterResource(Icons.ic_alert),
                contentDescription = null,
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(Strings.log_levels),
            )
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.uid.orEmpty(),
            onValueChange = listener.onUidChange,
            label = { Text(text = stringResource(Strings.uid)) },
            singleLine = true,
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.pid.orEmpty(),
            onValueChange = listener.onPidChange,
            label = { Text(text = stringResource(Strings.pid)) },
            singleLine = true,
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.tid.orEmpty(),
            onValueChange = listener.onTidChange,
            label = { Text(text = stringResource(Strings.tid)) },
            singleLine = true,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = state.packageName.orEmpty(),
                onValueChange = listener.onPackageNameChange,
                label = { Text(text = stringResource(Strings.package_name)) },
                singleLine = true,
            )

            IconButton(onClick = listener.onSelectAppClick) {
                Icon(
                    painter = painterResource(Icons.ic_android),
                    contentDescription = stringResource(Strings.select),
                )
            }
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.tag.orEmpty(),
            onValueChange = listener.onTagChange,
            label = { Text(text = stringResource(Strings.tag)) },
            singleLine = true,
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.content.orEmpty(),
            onValueChange = listener.onContentChange,
            label = { Text(text = stringResource(Strings.content_contains)) },
            singleLine = true,
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Strings.empty_fields_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun EnabledButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val color = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error

    OutlinedButton(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        border = androidx.compose.foundation.BorderStroke(2.dp, color),
    ) {
        Icon(
            painter = painterResource(if (enabled) Icons.ic_eye else Icons.ic_block),
            contentDescription = null,
            tint = color,
        )
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = stringResource(if (enabled) Strings.enabled else Strings.disabled),
            color = color,
        )
    }
}

@Composable
private fun IncludingButton(
    including: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val color = if (including) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error

    OutlinedButton(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        border = androidx.compose.foundation.BorderStroke(2.dp, color),
    ) {
        Icon(
            painter = painterResource(if (including) Icons.ic_add else Icons.ic_clear),
            contentDescription = null,
            tint = color,
        )
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = stringResource(if (including) Strings.including else Strings.excluding),
            color = color,
        )
    }
}
