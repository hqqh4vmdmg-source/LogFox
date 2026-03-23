package com.f0x1d.logfox.feature.crashes.presentation.appcrashes.ui.compose

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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.f0x1d.logfox.compose.base.preview.DayNightPreview
import com.f0x1d.logfox.compose.designsystem.component.button.NavigationBackButton
import com.f0x1d.logfox.compose.designsystem.component.placeholder.ListPlaceholder
import com.f0x1d.logfox.compose.designsystem.theme.LogFoxTheme
import com.f0x1d.logfox.core.ui.icons.Icons
import com.f0x1d.logfox.feature.crashes.api.model.CrashType
import com.f0x1d.logfox.feature.crashes.presentation.appcrashes.AppCrashesViewState
import com.f0x1d.logfox.feature.crashes.presentation.appcrashes.ui.AppCrashesScreenListener
import com.f0x1d.logfox.feature.crashes.presentation.appcrashes.ui.MockAppCrashesScreenListener
import com.f0x1d.logfox.feature.crashes.presentation.common.model.AppCrashesCountItem
import com.f0x1d.logfox.feature.strings.Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AppCrashesScreenContent(
    state: AppCrashesViewState,
    modifier: Modifier = Modifier,
    listener: AppCrashesScreenListener = MockAppCrashesScreenListener,
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state = topAppBarState)

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = state.appName ?: state.packageName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    NavigationBackButton(onClick = listener.onBackClick)
                },
                scrollBehavior = scrollBehavior,
            )
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
            AppCrashesList(
                items = state.crashes,
                contentPadding = contentPadding,
                onCrashClick = listener.onCrashClick,
                onDeleteCrashClick = listener.onDeleteCrashClick,
            )
        }
    }
}

@Composable
private fun AppCrashesList(
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
            key = { _, item -> item.lastCrashId },
        ) { index, item ->
            AppCrashItem(
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
private fun AppCrashItem(
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
                text = "${item.crashType.readableName} · ${item.formattedDate}",
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

internal val MockAppCrashesState = AppCrashesViewState(
    packageName = "com.f0x1d.logfox",
    appName = "LogFox",
    crashes = listOf(
        AppCrashesCountItem(
            lastCrashId = 1L,
            appName = "LogFox",
            packageName = "com.f0x1d.logfox",
            crashType = CrashType.JAVA,
            count = 1,
            formattedDate = "01/01/1970 00:00",
        ),
    ),
)

@DayNightPreview
@Composable
private fun AppCrashesScreenContentPreview() = LogFoxTheme {
    AppCrashesScreenContent(state = MockAppCrashesState)
}
