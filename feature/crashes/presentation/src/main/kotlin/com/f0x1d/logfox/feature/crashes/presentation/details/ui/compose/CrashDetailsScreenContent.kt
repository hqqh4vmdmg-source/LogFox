package com.f0x1d.logfox.feature.crashes.presentation.details.ui.compose

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.f0x1d.logfox.compose.base.preview.DayNightPreview
import com.f0x1d.logfox.compose.designsystem.component.button.NavigationBackButton
import com.f0x1d.logfox.compose.designsystem.component.button.VerticalButton
import com.f0x1d.logfox.compose.designsystem.theme.LogFoxTheme
import com.f0x1d.logfox.core.compat.notificationsChannelsAvailable
import com.f0x1d.logfox.core.ui.icons.Icons
import com.f0x1d.logfox.feature.crashes.presentation.details.CrashDetailsViewState
import com.f0x1d.logfox.feature.crashes.presentation.details.ui.CrashDetailsScreenListener
import com.f0x1d.logfox.feature.crashes.presentation.details.ui.MockCrashDetailsScreenListener
import com.f0x1d.logfox.feature.strings.Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CrashDetailsScreenContent(
    state: CrashDetailsViewState,
    modifier: Modifier = Modifier,
    listener: CrashDetailsScreenListener = MockCrashDetailsScreenListener,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    var isSearchActive by remember { mutableStateOf(false) }

    BackHandler(enabled = isSearchActive) {
        isSearchActive = false
        listener.onSearchQueryChange("")
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            if (isSearchActive) {
                CrashDetailsSearchBar(
                    query = state.searchQuery,
                    onQueryChange = listener.onSearchQueryChange,
                    onClose = {
                        isSearchActive = false
                        listener.onSearchQueryChange("")
                    },
                )
            } else {
                CrashDetailsTopBar(
                    state = state,
                    onBackClick = listener.onBackClick,
                    onSearchClick = { isSearchActive = true },
                    onWrapLinesClick = listener.onWrapLinesClick,
                    onInfoClick = listener.onInfoClick,
                    onNotificationsClick = listener.onNotificationsClick,
                    onBlacklistClick = listener.onBlacklistClick,
                    onDeleteClick = listener.onDeleteClick,
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets.statusBars,
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            state.crash?.let { crash ->
                CrashDetailsHeader(
                    packageName = crash.packageName,
                    appName = crash.appName,
                    onCopyClick = listener.onCopyClick,
                    onShareClick = listener.onShareClick,
                    onExportClick = listener.onExportClick,
                    onZipClick = listener.onZipClick,
                )
            }

            state.crashLog?.let { log ->
                CrashLogContent(
                    log = log,
                    wrapLines = state.wrapCrashLogLines,
                    searchMatchRanges = state.searchMatchRanges,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CrashDetailsTopBar(
    state: CrashDetailsViewState,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    onWrapLinesClick: () -> Unit,
    onInfoClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onBlacklistClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showMenu by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(Strings.crash_details),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            NavigationBackButton(onClick = onBackClick)
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    painter = painterResource(Icons.ic_search),
                    contentDescription = null,
                )
            }

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
                    text = { Text(text = stringResource(Strings.wrap_log_lines_in_details)) },
                    leadingIcon = {
                        if (state.wrapCrashLogLines) {
                            Icon(
                                painter = painterResource(Icons.ic_check_circle),
                                contentDescription = null,
                            )
                        }
                    },
                    onClick = {
                        showMenu = false
                        onWrapLinesClick()
                    },
                )

                DropdownMenuItem(
                    text = { Text(text = stringResource(Strings.information)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(Icons.ic_info),
                            contentDescription = null,
                        )
                    },
                    onClick = {
                        showMenu = false
                        onInfoClick()
                    },
                )

                if (notificationsChannelsAvailable && state.useSeparateNotificationsChannelsForCrashes) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(Strings.notifications)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(Icons.ic_notifications),
                                contentDescription = null,
                            )
                        },
                        onClick = {
                            showMenu = false
                            onNotificationsClick()
                        },
                    )
                }

                state.blacklisted?.let { blacklisted ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(
                                    if (blacklisted) Strings.remove_from_blacklist
                                    else Strings.add_to_blacklist,
                                ),
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(
                                    if (blacklisted) Icons.ic_check_circle else Icons.ic_block,
                                ),
                                contentDescription = null,
                            )
                        },
                        onClick = {
                            showMenu = false
                            onBlacklistClick()
                        },
                    )
                }

                DropdownMenuItem(
                    text = { Text(text = stringResource(Strings.delete)) },
                    onClick = {
                        showMenu = false
                        onDeleteClick()
                    },
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CrashDetailsSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(text = stringResource(Strings.search)) },
        leadingIcon = {
            NavigationBackButton(onClick = onClose)
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        painter = painterResource(Icons.ic_clear),
                        contentDescription = null,
                    )
                }
            }
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
    )
}

@Composable
private fun CrashDetailsHeader(
    packageName: String,
    appName: String?,
    onCopyClick: () -> Unit,
    onShareClick: () -> Unit,
    onExportClick: () -> Unit,
    onZipClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = remember(packageName) {
                    try {
                        context.packageManager.getApplicationIcon(packageName)
                    } catch (e: Exception) {
                        null
                    }
                },
                contentDescription = null,
                modifier = Modifier.size(60.dp),
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = appName ?: stringResource(Strings.unknown),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = packageName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            VerticalButton(
                modifier = Modifier.weight(1f),
                icon = {
                    Icon(
                        painter = painterResource(Icons.ic_copy),
                        contentDescription = null,
                    )
                },
                text = { Text(text = stringResource(Strings.extended_copy)) },
                onClick = onCopyClick,
            )

            VerticalButton(
                modifier = Modifier.weight(1f),
                icon = {
                    Icon(
                        painter = painterResource(Icons.ic_share),
                        contentDescription = null,
                    )
                },
                text = { Text(text = stringResource(Strings.share)) },
                onClick = onShareClick,
            )

            VerticalButton(
                modifier = Modifier.weight(1f),
                icon = {
                    Icon(
                        painter = painterResource(Icons.ic_export),
                        contentDescription = null,
                    )
                },
                text = { Text(text = stringResource(Strings.export)) },
                onClick = onExportClick,
            )

            VerticalButton(
                modifier = Modifier.weight(1f),
                icon = {
                    Icon(
                        painter = painterResource(Icons.ic_archive),
                        contentDescription = null,
                    )
                },
                text = { Text(text = stringResource(Strings.zip)) },
                onClick = onZipClick,
            )
        }
    }
}

@Composable
private fun CrashLogContent(
    log: String,
    wrapLines: Boolean,
    searchMatchRanges: List<IntRange>,
    modifier: Modifier = Modifier,
) {
    val highlightColor = MaterialTheme.colorScheme.primaryContainer
    val annotatedLog = remember(log, searchMatchRanges, highlightColor) {
        buildAnnotatedString {
            append(log)
            searchMatchRanges.forEach { range ->
                addStyle(
                    style = SpanStyle(background = highlightColor),
                    start = range.first,
                    end = range.last + 1,
                )
            }
        }
    }

    if (wrapLines) {
        SelectionContainer(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(
                text = annotatedLog,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                fontSize = 12.sp,
            )
        }
    } else {
        Box(
            modifier = modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            SelectionContainer {
                Text(
                    text = annotatedLog,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    fontSize = 12.sp,
                    softWrap = false,
                )
            }
        }
    }
}

internal val MockCrashDetailsState = CrashDetailsViewState(
    crash = null,
    crashLog = "java.lang.NullPointerException: Attempt to invoke virtual method\n\tat com.example.App.onCreate(App.kt:42)",
    blacklisted = false,
    wrapCrashLogLines = true,
    useSeparateNotificationsChannelsForCrashes = false,
    searchQuery = "",
    searchMatchRanges = emptyList(),
)

@DayNightPreview
@Composable
private fun CrashDetailsScreenContentPreview() = LogFoxTheme {
    CrashDetailsScreenContent(state = MockCrashDetailsState)
}
