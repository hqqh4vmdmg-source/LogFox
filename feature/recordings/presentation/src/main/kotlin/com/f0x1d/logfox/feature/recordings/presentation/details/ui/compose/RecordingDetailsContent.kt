package com.f0x1d.logfox.feature.recordings.presentation.details.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.f0x1d.logfox.compose.designsystem.component.button.VerticalButton
import com.f0x1d.logfox.core.ui.icons.Icons
import com.f0x1d.logfox.feature.recordings.presentation.details.RecordingDetailsViewState
import com.f0x1d.logfox.feature.strings.Strings

@Composable
internal fun RecordingDetailsContent(
    state: RecordingDetailsViewState,
    onTitleChanged: (String) -> Unit = {},
    onExportClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onZipClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 10.dp)
            .padding(top = 20.dp, bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        state.recordingItem?.let { item ->
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = item.formattedDate,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.currentTitle.orEmpty(),
            onValueChange = onTitleChanged,
            label = { Text(text = stringResource(Strings.title)) },
            singleLine = true,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
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
