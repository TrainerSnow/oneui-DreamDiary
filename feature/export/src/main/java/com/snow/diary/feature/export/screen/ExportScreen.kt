package com.snow.diary.feature.export.screen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.diary.core.common.time.TimeFormat.formatFullDescription
import com.snow.diary.core.io.ExportFiletype
import com.snow.diary.feature.export.R
import com.snow.diary.feature.export.info
import com.snow.diary.feature.export.screen.components.ExportFiletypeButton
import com.snow.diary.feature.export.suitableForImporting
import kotlinx.coroutines.flow.collectLatest
import org.oneui.compose.layout.toolbar.CollapsingToolbarCollapsedState
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.layout.toolbar.rememberCollapsingToolbarState
import org.oneui.compose.progress.CircularProgressIndicatorSize
import org.oneui.compose.progress.ProgressIndicator
import org.oneui.compose.progress.ProgressIndicatorType
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.widgets.buttons.Button
import org.oneui.compose.widgets.buttons.coloredButtonColors
import java.time.LocalDate

@Composable
internal fun ExportScreen(
    viewModel: ExportViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument(mimeType = state.selectedFiletype.mimeType)
    ) {
        viewModel.onEvent(ExportEvent.FileCreated(it))
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            fun toast(str: String) = Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
            when (event) {
                ExportUiEvent.ReturnFailure -> {
                    toast(context.resources.getString(R.string.export_failure))
                    onNavigateBack()
                }

                ExportUiEvent.ReturnSuccess -> {
                    toast(context.resources.getString(R.string.export_success))
                    onNavigateBack()
                }

                ExportUiEvent.OpenFilePicker -> {
                    val fileName =
                        context.resources.getString(
                            R.string.export_file_name,
                            LocalDate.now().formatFullDescription()
                        ) + state.selectedFiletype.fileExtension
                    launcher.launch(fileName)
                }
            }
        }
    }

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
        textAlign = TextAlign.Center,
        color = OneUITheme.colors.seslPrimaryTextColor
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


    CollapsingToolbarLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(OneUITheme.colors.seslBackgroundColor)
            .padding(ExportScreenDefaults.padding),
        toolbarTitle = stringResource(R.string.export_title),
        state = rememberCollapsingToolbarState(CollapsingToolbarCollapsedState.COLLAPSED),
        expandable = false
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.export_info),
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

            Button(
                label = {
                    if (state.isExporting) {
                        ProgressIndicator(
                            type = ProgressIndicatorType.CircularIndeterminate(
                                CircularProgressIndicatorSize.Companion.Small
                            )
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.export_button_label)
                        )
                    }
                },
                onClick = {
                    onEvent(
                        ExportEvent.Export
                    )
                },
                padding = ExportScreenDefaults.buttonPadding,
                colors = coloredButtonColors()
            )
        }
    }
}

private object ExportScreenDefaults {

    val maxHeight = 900.dp

    val padding = 16.dp

    val infoWarningSpacing = 12.dp

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