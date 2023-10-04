package com.snow.diary.feature.importing.screen.config

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.diary.core.io.ImportFiletype
import com.snow.diary.feature.importing.R
import com.snow.diary.feature.importing.screen.component.ImportFiletypeButton
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.widgets.buttons.Button
import org.oneui.compose.widgets.buttons.coloredButtonColors

@Composable
internal fun ImportConfigScreen(
    viewModel: ImportConfigViewModel = hiltViewModel(),
    onUriSelected: (Uri) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) {
        if (it != null) onUriSelected(it)
    }

    ImportConfigScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onContinue = {
            launcher.launch(arrayOf(state.selectedType.mimeType))
        }
    )
}

@Composable
private fun ImportConfigScreen(
    state: ImportConfigState,
    onEvent: (ImportConfigEvent) -> Unit,
    onContinue: () -> Unit
) {
    val titleStyle = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    CollapsingToolbarLayout(
        toolbarTitle = stringResource(R.string.import_config_title)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.import_config_filetype_prompt),
                style = titleStyle
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2), //TODO: This needs to be a divisor of the amount of ImportFiletype's, because the horizontalArrangement doesn't work.
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                items(
                    count = ImportFiletype.entries.size,
                    key = { it }
                ) {
                    ImportFiletypeButton(
                        filetype = ImportFiletype.entries[it],
                        selected = state.selectedType == ImportFiletype.entries[it],
                        onClick = {
                            onEvent(
                                ImportConfigEvent.SelectType(
                                    ImportFiletype.entries[it]
                                )
                            )
                        }
                    )
                }
            }

            Button(
                label = {
                    Text(
                        text = stringResource(R.string.import_config_button_label)
                    )
                },
                onClick = {
                    onContinue()
                },
                padding = PaddingValues(
                    horizontal = 64.dp,
                    vertical = 8.dp
                ),
                colors = coloredButtonColors()
            )
        }
    }
}