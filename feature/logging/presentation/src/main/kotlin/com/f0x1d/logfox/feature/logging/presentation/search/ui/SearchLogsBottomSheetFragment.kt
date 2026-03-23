package com.f0x1d.logfox.feature.logging.presentation.search.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.f0x1d.logfox.core.ui.compose.BaseComposeBottomSheetFragment
import com.f0x1d.logfox.feature.logging.presentation.search.SearchLogsCommand
import com.f0x1d.logfox.feature.logging.presentation.search.SearchLogsSideEffect
import com.f0x1d.logfox.feature.logging.presentation.search.SearchLogsViewModel
import com.f0x1d.logfox.feature.logging.presentation.search.ui.compose.SearchLogsContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class SearchLogsBottomSheetFragment : BaseComposeBottomSheetFragment() {

    private val viewModel by viewModels<SearchLogsViewModel>()

    @Composable
    override fun Content() {
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.sideEffects.collect { effect ->
                when (effect) {
                    is SearchLogsSideEffect.Dismiss -> dismiss()
                    else -> Unit
                }
            }
        }

        SearchLogsContent(
            state = state,
            onQueryChanged = { viewModel.send(SearchLogsCommand.UpdateQuery(it)) },
            onCaseSensitiveToggle = { viewModel.send(SearchLogsCommand.ToggleCaseSensitive) },
            onSearch = { query ->
                if (query?.isEmpty() == true) return@SearchLogsContent
                viewModel.send(SearchLogsCommand.UpdateQuery(query))
            },
            onClear = { viewModel.send(SearchLogsCommand.UpdateQuery(null)) },
        )
    }
}
