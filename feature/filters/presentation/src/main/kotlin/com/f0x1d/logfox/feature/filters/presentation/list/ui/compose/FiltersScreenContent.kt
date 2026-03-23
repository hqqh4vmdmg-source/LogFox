package com.f0x1d.logfox.feature.filters.presentation.list.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.f0x1d.logfox.compose.designsystem.component.button.NavigationBackButton
import com.f0x1d.logfox.compose.designsystem.component.placeholder.ListPlaceholder
import com.f0x1d.logfox.core.ui.icons.Icons
import com.f0x1d.logfox.feature.filters.api.model.UserFilter
import com.f0x1d.logfox.feature.filters.presentation.list.FiltersViewState
import com.f0x1d.logfox.feature.filters.presentation.list.ui.FiltersScreenListener
import com.f0x1d.logfox.feature.filters.presentation.list.ui.MockFiltersScreenListener
import com.f0x1d.logfox.feature.strings.Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FiltersScreenContent(
    modifier: Modifier = Modifier,
    state: FiltersViewState,
    listener: FiltersScreenListener = MockFiltersScreenListener,
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state = topAppBarState)

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FiltersTopBar(
                onBackClick = listener.onBackClick,
                onClearClick = listener.onClearClick,
                onImportClick = listener.onImportClick,
                onExportAllClick = listener.onExportAllClick,
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = listener.onAddClick) {
                Icon(
                    painter = painterResource(Icons.ic_add),
                    contentDescription = null,
                )
            }
        },
        contentWindowInsets = WindowInsets.statusBars,
    ) { contentPadding ->
        FiltersListContent(
            state = state,
            listener = listener,
            contentPadding = contentPadding,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FiltersTopBar(
    onBackClick: () -> Unit,
    onClearClick: () -> Unit,
    onImportClick: () -> Unit,
    onExportAllClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { Text(text = stringResource(Strings.filters)) },
        navigationIcon = { NavigationBackButton(onClick = onBackClick) },
        actions = {
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
                    text = { Text(text = stringResource(Strings.clear)) },
                    onClick = {
                        showMenu = false
                        onClearClick()
                    },
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(Strings.str_import)) },
                    onClick = {
                        showMenu = false
                        onImportClick()
                    },
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(Strings.export_all)) },
                    onClick = {
                        showMenu = false
                        onExportAllClick()
                    },
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@Composable
private fun FiltersListContent(
    state: FiltersViewState,
    listener: FiltersScreenListener,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    if (state.filters.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center,
        ) {
            ListPlaceholder(
                iconResId = Icons.ic_filter,
                text = { Text(text = stringResource(Strings.no_filters)) },
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = contentPadding,
        ) {
            itemsIndexed(
                items = state.filters,
                key = { _, item -> item.id },
            ) { index, item ->
                FilterItem(
                    modifier = Modifier.animateItem(),
                    item = item,
                    onFilterClick = listener.onFilterClick,
                    onFilterDelete = listener.onFilterDelete,
                    onFilterChecked = listener.onFilterChecked,
                )

                if (index != state.filters.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 10.dp,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterItem(
    item: UserFilter,
    onFilterClick: (UserFilter) -> Unit,
    onFilterDelete: (UserFilter) -> Unit,
    onFilterChecked: (UserFilter, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onFilterClick(item) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(if (item.including) Strings.including else Strings.excluding),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (item.allowedLevels.isNotEmpty()) {
                FilterFieldText(
                    prefix = stringResource(Strings.log_levels),
                    value = item.allowedLevels.joinToString { it.letter },
                )
            }
            item.uid?.takeIf { it.isNotEmpty() }?.let {
                FilterFieldText(prefix = stringResource(Strings.uid), value = it)
            }
            item.pid?.takeIf { it.isNotEmpty() }?.let {
                FilterFieldText(prefix = stringResource(Strings.pid), value = it)
            }
            item.tid?.takeIf { it.isNotEmpty() }?.let {
                FilterFieldText(prefix = stringResource(Strings.tid), value = it)
            }
            item.packageName?.takeIf { it.isNotEmpty() }?.let {
                FilterFieldText(prefix = stringResource(Strings.package_name), value = it)
            }
            item.tag?.takeIf { it.isNotEmpty() }?.let {
                FilterFieldText(prefix = stringResource(Strings.tag), value = it)
            }
            item.content?.takeIf { it.isNotEmpty() }?.let {
                FilterFieldText(prefix = stringResource(Strings.content_contains), value = it)
            }
        }

        Checkbox(
            checked = item.enabled,
            onCheckedChange = { onFilterChecked(item, it) },
        )

        IconButton(onClick = { onFilterDelete(item) }) {
            Icon(
                painter = painterResource(Icons.ic_delete),
                tint = MaterialTheme.colorScheme.error,
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun FilterFieldText(
    prefix: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("$prefix:")
            }
            append(" $value")
        },
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}
