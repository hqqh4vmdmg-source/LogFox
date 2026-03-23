package com.f0x1d.logfox.feature.crashes.presentation.list.ui.compose

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.f0x1d.logfox.compose.base.preview.DayNightPreview
import com.f0x1d.logfox.compose.designsystem.component.placeholder.ListPlaceholder
import com.f0x1d.logfox.compose.designsystem.component.search.TopSearchBar
import com.f0x1d.logfox.compose.designsystem.theme.LogFoxTheme
import com.f0x1d.logfox.core.ui.icons.Icons
import com.f0x1d.logfox.feature.crashes.api.model.CrashType
import com.f0x1d.logfox.feature.crashes.presentation.common.model.AppCrashesCountItem
import com.f0x1d.logfox.feature.crashes.presentation.list.CrashesViewState
import com.f0x1d.logfox.feature.crashes.presentation.list.ui.CrashesScreenListener
import com.f0x1d.logfox.feature.crashes.presentation.list.ui.MockCrashesScreenListener
import com.f0x1d.logfox.feature.preferences.api.CrashesSort
import com.f0x1d.logfox.feature.strings.Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CrashesScreenContent(
    state: CrashesViewState,
    modifier: Modifier = Modifier,
    listener: CrashesScreenListener = MockCrashesScreenListener,
) {
    var isSearchActive by remember { mutableStateOf(false) }
    var showSortDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = isSearchActive) {
        isSearchActive = false
        listener.onQueryChange("")
    }

    if (showSortDialog) {
        SortDialog(
            currentSort = state.currentSort,
            sortInReversedOrder = state.sortInReversedOrder,
            onConfirm = { type, reversed ->
                listener.onSortConfirmed(type, reversed)
                showSortDialog = false
            },
            onDismiss = { showSortDialog = false },
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CrashesSearchBar(
                query = state.query ?: "",
                isSearchActive = isSearchActive,
                onActiveChange = { active ->
                    isSearchActive = active
                    if (!active) listener.onQueryChange("")
                },
                onQueryChange = listener.onQueryChange,
                onSortClick = { showSortDialog = true },
                onBlacklistClick = listener.onBlacklistClick,
                onClearClick = listener.onClearClick,
            ) {
                SearchedCrashesList(
                    items = state.searchedCrashes,
                    onCrashClick = listener.onSearchedCrashClick,
                    onDeleteCrashClick = listener.onDeleteSearchedCrashClick,
                )
            }
        },
        contentWindowInsets = WindowInsets.statusBars,
    ) { contentPadding ->
        if (state.crashes.isEmpty()) {
            LazyColumn(contentPadding = contentPadding) {
                item {
                    ListPlaceholder(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        iconResId = Icons.ic_bug,
                        text = { Text(text = stringResource(Strings.no_crashes)) },
                    )
                }
            }
        } else {
            CrashesList(
                items = state.crashes,
                contentPadding = contentPadding,
                onCrashClick = listener.onCrashClick,
                onDeleteCrashClick = listener.onDeleteCrashClick,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CrashesSearchBar(
    query: String,
    isSearchActive: Boolean,
    onActiveChange: (Boolean) -> Unit,
    onQueryChange: (String) -> Unit,
    onSortClick: () -> Unit,
    onBlacklistClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }

    TopSearchBar(
        modifier = modifier,
        query = query,
        onQueryChange = onQueryChange,
        onSearch = { },
        active = isSearchActive,
        onActiveChange = onActiveChange,
        placeholder = { Text(text = stringResource(Strings.crashes)) },
        trailingIcon = {
            if (!isSearchActive) {
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
                        text = { Text(text = stringResource(Strings.sort)) },
                        onClick = {
                            showMenu = false
                            onSortClick()
                        },
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(Strings.blacklist)) },
                        onClick = {
                            showMenu = false
                            onBlacklistClick()
                        },
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(Strings.clear)) },
                        onClick = {
                            showMenu = false
                            onClearClick()
                        },
                    )
                }
            }
        },
    ) {
        content()
    }
}

@Composable
private fun CrashesList(
    items: List<AppCrashesCountItem>,
    contentPadding: PaddingValues,
    onCrashClick: (AppCrashesCountItem) -> Unit,
    onDeleteCrashClick: (AppCrashesCountItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
    ) {
        itemsIndexed(
            items = items,
            key = { _, item -> item.packageName },
        ) { index, item ->
            CrashItem(
                modifier = Modifier.animateItem(),
                item = item,
                onCrashClick = onCrashClick,
                onDeleteCrashClick = onDeleteCrashClick,
            )

            if (index != items.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(
                        start = 80.dp,
                        end = 10.dp,
                    ),
                )
            }
        }
    }
}

@Composable
private fun SearchedCrashesList(
    items: List<AppCrashesCountItem>,
    onCrashClick: (AppCrashesCountItem) -> Unit,
    onDeleteCrashClick: (AppCrashesCountItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(
            items = items,
            key = { _, item -> item.lastCrashId },
        ) { index, item ->
            CrashItem(
                item = item,
                onCrashClick = onCrashClick,
                onDeleteCrashClick = onDeleteCrashClick,
            )

            if (index != items.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(
                        start = 80.dp,
                        end = 10.dp,
                    ),
                )
            }
        }
    }
}

@Composable
private fun CrashItem(
    item: AppCrashesCountItem,
    onCrashClick: (AppCrashesCountItem) -> Unit,
    onDeleteCrashClick: (AppCrashesCountItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clickable { onCrashClick(item) }
            .padding(vertical = 10.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        AsyncImage(
            model = remember(item.packageName) {
                try {
                    context.packageManager.getApplicationIcon(item.packageName)
                } catch (e: Exception) {
                    null
                }
            },
            contentDescription = null,
            modifier = Modifier.size(60.dp),
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        ) {
            Text(
                text = item.appName ?: item.packageName,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = "${item.crashType.readableName} · ${item.count} · ${item.formattedDate}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        IconButton(onClick = { onDeleteCrashClick(item) }) {
            Icon(
                painter = painterResource(Icons.ic_delete),
                tint = MaterialTheme.colorScheme.error,
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun SortDialog(
    currentSort: CrashesSort,
    sortInReversedOrder: Boolean,
    onConfirm: (CrashesSort, Boolean) -> Unit,
    onDismiss: () -> Unit,
) {
    var selectedSort by remember { mutableStateOf(currentSort) }
    var reversed by remember { mutableStateOf(sortInReversedOrder) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(Strings.sort)) },
        text = {
            Column {
                CrashesSort.entries.forEach { sort ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedSort = sort },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = sort == selectedSort,
                            onClick = { selectedSort = sort },
                        )
                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = stringResource(sort.titleRes),
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(text = stringResource(Strings.in_reversed_order))
                    Switch(
                        checked = reversed,
                        onCheckedChange = { reversed = it },
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedSort, reversed) }) {
                Text(text = stringResource(android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(android.R.string.cancel))
            }
        },
    )
}

internal val MockCrashesState = CrashesViewState(
    crashes = listOf(
        AppCrashesCountItem(
            lastCrashId = 1L,
            appName = "LogFox",
            packageName = "com.f0x1d.logfox",
            crashType = CrashType.JAVA,
            count = 3,
            formattedDate = "01/01/1970 00:00",
        ),
        AppCrashesCountItem(
            lastCrashId = 2L,
            appName = "Sense",
            packageName = "com.f0x1d.sense",
            crashType = CrashType.ANR,
            count = 1,
            formattedDate = "01/01/1970 00:00",
        ),
    ),
    searchedCrashes = emptyList(),
    currentSort = CrashesSort.NEW,
    sortInReversedOrder = false,
    query = null,
)

@DayNightPreview
@Composable
private fun CrashesScreenContentPreview() = LogFoxTheme {
    CrashesScreenContent(state = MockCrashesState)
}
