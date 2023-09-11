package com.snow.diary.feature.export.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.diary.feature.export.R
import com.snow.diary.feature.export.info
import com.snow.diary.feature.export.screen.components.ExportFiletypeButton
import com.snow.diary.feature.export.suitableForImporting
import com.snow.diary.core.io.ExportFiletype
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.widgets.buttons.ColoredButton

@Composable
internal fun ExportScreen(
    viewModel: ExportViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ExportScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun ExportScreen(
    state: ExportState,
    onEvent: (ExportEvent) -> Unit
) {
    val titleStyle = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
    val infoTextStyle = TextStyle(
        fontSize = 14.sp,
        color = OneUITheme.colors.seslSecondaryTextColor,
        textAlign = TextAlign.Center
    )
    val warningTextStyle = TextStyle(
        fontSize = 14.sp,
        color = OneUITheme.colors.seslFunctionalRed,
        textAlign = TextAlign.Center
    )

    val screenHeight = LocalConfiguration.current.screenHeightDp
    val height = screenHeight.coerceAtMost(ExportScreenDefaults.maxHeight.value.toInt()).dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OneUITheme.colors.seslBackgroundColor)
            .padding(ExportScreenDefaults.padding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.export_title),
                style = titleStyle
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2), //TODO: This needs to be a divisor of the amount of ExportFiletype's, because the horizontalArrangement doesn't work.
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                items(
                    count = ExportFiletype.entries.size,
                    key = { it }
                ) {
                    ExportFiletypeButton(
                        filetype = ExportFiletype.entries[it],
                        selected = state.selectedFiletype == ExportFiletype.entries[it],
                        onClick = {
                            onEvent(
                                ExportEvent.SelectFiletype(
                                    ExportFiletype.entries[it]
                                )
                            )
                        }
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement
                    .spacedBy(ExportScreenDefaults.infoWarningSpacing)
            ) {
                Text(
                    text = stringResource(state.selectedFiletype.info),
                    style = infoTextStyle
                )
                Text(
                    modifier = Modifier
                        .graphicsLayer {
                            if (state.selectedFiletype.suitableForImporting) alpha =
                                0F //Hide warning but still show text so layout doesnt change weirdly
                        },
                    text = stringResource(R.string.export_warning_no_import),
                    style = warningTextStyle
                )
            }

            //TODO: When lib allows, make this button show progress indicator when ExportState#isExporting
            ColoredButton(
                label = stringResource(R.string.export_button_label),
                onClick = {
                    onEvent(
                        ExportEvent.Export
                    )
                },
                padding = ExportScreenDefaults.buttonPadding
            )
        }
    }
}

private object ExportScreenDefaults {

    val maxHeight = 900.dp

    val padding = 16.dp

    val infoWarningSpacing = 12.dp

    //TODO: Make button style in lib for bigger button ( = higher emphasis)
    val buttonPadding = PaddingValues(
        horizontal = 64.dp,
        vertical = 8.dp
    )

}

@Preview
@Composable
fun ExportScreenPreview() = ExportScreen(
    state = ExportState(
        selectedFiletype = ExportFiletype.JSON
    ),
    onEvent = { }
)