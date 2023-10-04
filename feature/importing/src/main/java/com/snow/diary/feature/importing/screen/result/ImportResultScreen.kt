package com.snow.diary.feature.importing.screen.result

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.diary.feature.importing.R
import org.oneui.compose.layout.toolbar.CollapsingToolbarCollapsedState
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.layout.toolbar.rememberCollapsingToolbarState
import org.oneui.compose.progress.CircularProgressIndicatorSize
import org.oneui.compose.progress.ProgressIndicator
import org.oneui.compose.progress.ProgressIndicatorType
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.util.OneUIPreview
import org.oneui.compose.widgets.buttons.Button
import org.oneui.compose.widgets.buttons.coloredButtonColors
import org.oneui.compose.widgets.buttons.transparentButtonColors

@Composable
internal fun ImportResultScreen(
    viewModel: ImportResultViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ImportResultScreen(
        state = state,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun ImportResultScreen(
    state: ImportResultState,
    onNavigateBack: () -> Unit
) {
    CollapsingToolbarLayout(
        modifier = Modifier
            .fillMaxSize(),
        toolbarTitle = stringResource(state.titleRes),
        toolbarSubtitle = (state as? ImportResultState.ImportFailed)?.errors?.size?.toString(),
        state = rememberCollapsingToolbarState(CollapsingToolbarCollapsedState.COLLAPSED)
    ) {
        when (state) {
            is ImportResultState.ImportFailed -> ErrorScreen(
                modifier = Modifier
                    .fillMaxSize(),
                state = state,
                onCancelClick = onNavigateBack
            )

            is ImportResultState.ImportSuccess -> SuccessScreen(
                modifier = Modifier
                    .fillMaxSize(),
                state = state,
                onContinueClick = onNavigateBack
            )

            ImportResultState.Importing -> ImportingScreen(
                modifier = Modifier
                    .fillMaxSize(),
            )
        }
    }
}

@Composable
private fun ImportingScreen(
    modifier: Modifier = Modifier
) {
    val textStyle = TextStyle(
        fontSize = 14.sp,
        color = OneUITheme.colors.seslPrimaryTextColor,
        textAlign = TextAlign.Center
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProgressIndicator(
            type = ProgressIndicatorType.CircularIndeterminate(
                size = CircularProgressIndicatorSize.Companion.XLarge
            )
        )
        Text(
            text = stringResource(R.string.import_loading),
            style = textStyle
        )
    }
}

@Composable
private fun SuccessScreen(
    modifier: Modifier = Modifier,
    state: ImportResultState.ImportSuccess,
    onContinueClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        val infoTextStyle = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = OneUITheme.colors.seslPrimaryTextColor,
            textAlign = TextAlign.Center
        )
        val importedTextStyle = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = OneUITheme.colors.seslFunctionalGreen
        )

        Text(
            text = stringResource(R.string.import_success_info),
            style = infoTextStyle
        )

        Column(
            verticalArrangement = Arrangement
                .spacedBy(12.dp)
        ) {
            Text(
                style = importedTextStyle,
                text = stringResource(
                    R.string.import_success_info_dreamsnum,
                    state.dreamsNum.toString()
                )
            )
            Text(
                style = importedTextStyle,
                text = stringResource(
                    R.string.import_success_info_personsnum,
                    state.personsNum.toString()
                )
            )
            Text(
                style = importedTextStyle,
                text = stringResource(
                    R.string.import_success_info_locationsnum,
                    state.locationsNum.toString()
                )
            )
            Text(
                style = importedTextStyle,
                text = stringResource(
                    R.string.import_success_info_relationsnum,
                    state.relationsNum.toString()
                )
            )
        }

        Button(
            label = stringResource(R.string.import_success_info_continue),
            colors = coloredButtonColors(),
            onClick = onContinueClick
        )
    }
}

@Composable
private fun ErrorScreen(
    modifier: Modifier = Modifier,
    state: ImportResultState.ImportFailed,
    onCancelClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        val infoTextStyle = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = OneUITheme.colors.seslPrimaryTextColor,
            textAlign = TextAlign.Center
        )
        val problemTextStyle = TextStyle(
            fontSize = 14.sp,
            color = OneUITheme.colors.seslFunctionalRed
        )
        val helpTextStyle = TextStyle(
            fontSize = 14.sp,
            color = OneUITheme.colors.seslSecondaryTextColor,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(R.string.import_failed_info_title),
            style = infoTextStyle
        )

        Column(
            verticalArrangement = Arrangement
                .spacedBy(12.dp)
        ) {
            state.errors.forEach { problem ->
                Text(
                    style = problemTextStyle,
                    text = stringResource(problem.descriptionRes)
                )
            }
        }

        Text(
            style = helpTextStyle,
            text = stringResource(R.string.import_failed_info_help)
        )

        Button(
            label = stringResource(R.string.import_problem_cancel),
            colors = transparentButtonColors(),
            onClick = onCancelClick
        )
    }
}

private val ImportResultState.titleRes: Int
    @StringRes get() = when (this) {
        is ImportResultState.ImportFailed -> R.string.import_title_failed
        ImportResultState.Importing -> R.string.import_title
        is ImportResultState.ImportSuccess -> R.string.import_title_success
    }

@Preview(device = "spec:width=411dp,height=891dp")
@Composable
private fun ImportScreenErrorPreview() {
    OneUIPreview(title = "ImportScreenErrorPreview") {
        ImportResultScreen(
            state = ImportResultState.ImportFailed(
                listOf(
                    ImportResultError.CorruptedFile,
                    ImportResultError.NonUniqueId,
                    ImportResultError.InvalidCrossref
                )
            ),
            onNavigateBack = { }
        )
    }
}

@Preview(device = "spec:width=411dp,height=891dp")
@Composable
private fun ImportScreenSuccessPreview() {
    OneUIPreview(title = "ImportScreenErrorPreview") {
        ImportResultScreen(
            state = ImportResultState.ImportSuccess(153, 34, 12, 6),
            onNavigateBack = { }
        )
    }
}