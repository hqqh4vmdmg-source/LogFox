package com.f0x1d.logfox.feature.logging.presentation.list.ui.compose

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.f0x1d.logfox.compose.designsystem.component.placeholder.ListPlaceholder
import com.f0x1d.logfox.core.ui.icons.Icons
import com.f0x1d.logfox.feature.logging.api.model.LogLevel
import com.f0x1d.logfox.feature.logging.presentation.list.LogsViewState
import com.f0x1d.logfox.feature.logging.presentation.list.model.LogLineItem
import com.f0x1d.logfox.feature.logging.presentation.list.ui.LogsScreenListener
import com.f0x1d.logfox.feature.logging.presentation.view.loglevel.backgroundColorId
import com.f0x1d.logfox.feature.logging.presentation.view.loglevel.foregroundColorId
import com.f0x1d.logfox.feature.strings.Plurals
import com.f0x1d.logfox.feature.strings.Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LogsScreenContent(
    modifier: Modifier = Modifier,
    state: LogsViewState,
    listener: LogsScreenListener = LogsScreenListener(),
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state = topAppBarState)
    val listState = rememberLazyListState()
    val currentListener by rememberUpdatedState(listener)
    val currentState by rememberUpdatedState(state)

    BackHandler(enabled = state.selecting) {
        listener.onClearSelection()
    }

    LaunchedEffect(state.logs, state.paused) {
        if (!state.paused && state.logs.isNotEmpty()) {
            listState.scrollToItem(state.logs.lastIndex)
        }
    }

    LaunchedEffect(listState) {
        var wasScrolling = false
        snapshotFlow {
            Triple(
                listState.isScrollInProgress,
                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index,
                listState.layoutInfo.totalItemsCount,
            )
        }.collect { (isScrolling, lastVisible, total) ->
            if (isScrolling && !wasScrolling) {
                currentListener.onScrollStarted()
            } else if (!isScrolling && wasScrolling) {
                val isAtBottom = total > 0 && lastVisible != null && lastVisible >= total - 1
                currentListener.onScrollEnded(isAtBottom)
            }
            wasScrolling = isScrolling
        }
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LogsTopBar(
                state = state,
                listener = listener,
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            if (state.paused) {
                FloatingActionButton(onClick = listener.onScrollFabClick) {
                    Icon(
                        painter = painterResource(Icons.ic_arrow_drop_down),
                        contentDescription = stringResource(Strings.scroll_to_bottom),
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets.statusBars,
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
            ) {
                items(state.logs, key = { it.logLineId }) { item ->
                    LogItem(item = item, listener = listener)
                }
            }

            if (state.logs.isEmpty()) {
                val hasFilter = !state.query.isNullOrEmpty() || state.filters.isNotEmpty()
                ListPlaceholder(
                    modifier = Modifier.align(Alignment.Center),
                    iconResId = Icons.ic_bug,
                    text = {
                        Text(
                            text = stringResource(
                                if (hasFilter) Strings.all_logs_were_filtered_out
                                else Strings.waiting_for_logs,
                            ),
                        )
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LogsTopBar(
    state: LogsViewState,
    listener: LogsScreenListener,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val subtitle = buildString {
        if (state.query != null) {
            append(state.query)
            if (state.filters.isNotEmpty()) append(", ")
        }
        if (state.filters.isNotEmpty()) {
            append(pluralStringResource(Plurals.filters_count, state.filters.size, state.filters.size))
        }
    }

    TopAppBar(
        title = {
            androidx.compose.foundation.layout.Column(
                modifier = Modifier.combinedClickable(onClick = listener.onToolbarClick),
            ) {
                Text(
                    text = if (state.selecting) {
                        pluralStringResource(Plurals.selected_count, state.selectedCount, state.selectedCount)
                    } else {
                        stringResource(Strings.app_name)
                    },
                )
                if (!state.selecting && subtitle.isNotEmpty()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        },
        navigationIcon = {
            if (state.selecting) {
                IconButton(onClick = listener.onClearSelection) {
                    Icon(
                        painter = painterResource(Icons.ic_clear),
                        contentDescription = null,
                    )
                }
            }
        },
        actions = {
            if (!state.selecting) {
                IconButton(onClick = listener.onPauseResumeClick) {
                    Icon(
                        painter = painterResource(if (state.paused) Icons.ic_play else Icons.ic_pause),
                        contentDescription = stringResource(if (state.paused) Strings.resume else Strings.pause),
                    )
                }
                IconButton(onClick = listener.onOpenSearch) {
                    Icon(
                        painter = painterResource(Icons.ic_search),
                        contentDescription = stringResource(Strings.search),
                    )
                }
                IconButton(onClick = listener.onOpenFilters) {
                    Icon(
                        painter = painterResource(Icons.ic_filter),
                        contentDescription = stringResource(Strings.filters),
                    )
                }
                var showMenu by remember { mutableStateOf(false) }
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(
                        painter = painterResource(Icons.ic_menu_overflow),
                        contentDescription = null,
                    )
                }
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(
                        text = { Text(stringResource(Strings.clear)) },
                        onClick = { showMenu = false; listener.onClearLogs() },
                        leadingIcon = { Icon(painter = painterResource(Icons.ic_clear_all), contentDescription = null) },
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(Strings.restart_logging)) },
                        onClick = { showMenu = false; listener.onRestartLogging() },
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(Strings.exit)) },
                        onClick = { showMenu = false; listener.onKillService() },
                    )
                }
            } else {
                IconButton(onClick = { listener.onSelectAll() }) {
                    Icon(
                        painter = painterResource(Icons.ic_select_all),
                        contentDescription = stringResource(Strings.select_all),
                    )
                }
                var showMenu by remember { mutableStateOf(false) }
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(
                        painter = painterResource(Icons.ic_menu_overflow),
                        contentDescription = null,
                    )
                }
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(
                        text = { Text(stringResource(android.R.string.copy)) },
                        onClick = { showMenu = false; listener.onCopySelected() },
                        leadingIcon = { Icon(painter = painterResource(Icons.ic_copy), contentDescription = null) },
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(Strings.extended_copy)) },
                        onClick = { showMenu = false; listener.onExtendedCopy() },
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(Strings.to_recording)) },
                        onClick = { showMenu = false; listener.onSelectedToRecording() },
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(Strings.export)) },
                        onClick = { showMenu = false; listener.onExportSelected() },
                        leadingIcon = { Icon(painter = painterResource(Icons.ic_export), contentDescription = null) },
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LogItem(
    item: LogLineItem,
    listener: LogsScreenListener,
    modifier: Modifier = Modifier,
) {
    var showMenu by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                if (item.selected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
            )
            .combinedClickable(
                onClick = { listener.onItemClick(item.logLineId) },
                onLongClick = { showMenu = true },
            ),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            LogLevelBadge(level = item.level, textSize = item.textSize)
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 3.dp, vertical = 3.dp),
                text = item.displayText.toString(),
                fontSize = TextUnit(item.textSize, TextUnitType.Sp),
                maxLines = if (item.expanded) Int.MAX_VALUE else 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = FontFamily.Monospace,
            )
        }

        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
            DropdownMenuItem(
                text = { Text(stringResource(Strings.select)) },
                onClick = { showMenu = false; listener.onSelectClick(item.logLineId) },
                leadingIcon = { Icon(painter = painterResource(Icons.ic_select), contentDescription = null) },
            )
            DropdownMenuItem(
                text = { Text(stringResource(android.R.string.copy)) },
                onClick = { showMenu = false; listener.onCopyClick(item.logLineId) },
                leadingIcon = { Icon(painter = painterResource(Icons.ic_copy), contentDescription = null) },
            )
            DropdownMenuItem(
                text = { Text(stringResource(Strings.create_filter)) },
                onClick = { showMenu = false; listener.onCreateFilterClick(item.logLineId) },
                leadingIcon = { Icon(painter = painterResource(Icons.ic_filter), contentDescription = null) },
            )
        }
    }
}

@Composable
private fun LogLevelBadge(
    level: LogLevel,
    textSize: Float,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(colorResource(level.backgroundColorId))
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .wrapContentHeight(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = level.letter,
            color = colorResource(level.foregroundColorId),
            fontSize = TextUnit(textSize, TextUnitType.Sp),
            fontFamily = FontFamily.Monospace,
        )
    }
}
