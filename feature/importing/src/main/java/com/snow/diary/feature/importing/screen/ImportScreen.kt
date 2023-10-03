package com.snow.diary.feature.importing.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snow.diary.core.domain.action.io.ImportIOData
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
private fun ImportScreen(
    state: ImportState,
    onEvent: (ImportEvent) -> Unit
) {
    CollapsingToolbarLayout(
        modifier = Modifier
            .fillMaxSize(),
        toolbarTitle = stringResource(state.titleRes),
        toolbarSubtitle = (state as? ImportState.ImportFailed)?.problems?.size?.toString(),
        state = rememberCollapsingToolbarState(CollapsingToolbarCollapsedState.COLLAPSED)
    ) {
        when (state) {
            is ImportState.ImportFailed -> ErrorScreen(
                modifier = Modifier
                    .fillMaxSize(),
                state = state,
                onCancelClick = { onEvent(ImportEvent.Return) },
                onReImportClick = { onEvent(ImportEvent.OpenFilePicker) }
            )

            is ImportState.ImportSuccess -> SuccessScreen(
                modifier = Modifier
                    .fillMaxSize(),
                state = state,
                onContinueClick = { onEvent(ImportEvent.Return) }
            )

            ImportState.Importing -> ImportingScreen(
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
    state: ImportState.ImportSuccess,
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
    state: ImportState.ImportFailed,
    onCancelClick: () -> Unit,
    onReImportClick: () -> Unit
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
            state.problems.forEach { problem ->
                Text(
                    style = problemTextStyle,
                    text = stringResource(problem.descriptionRed)
                )
            }
        }

        Text(
            style = helpTextStyle,
            text = stringResource(R.string.import_failed_info_help)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 500.dp)
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                label = stringResource(R.string.import_problem_cancel),
                colors = transparentButtonColors(),
                onClick = onCancelClick
            )
            Button(
                label = stringResource(R.string.import_problem_reimport),
                colors = coloredButtonColors(),
                onClick = onReImportClick
            )
        }
    }
}

private val ImportState.titleRes: Int
    @StringRes get() = when (this) {
        is ImportState.ImportFailed -> R.string.import_title_failed
        ImportState.Importing -> R.string.import_title
        is ImportState.ImportSuccess -> R.string.import_title_success
    }

private val ImportIOData.ImportProblem.descriptionRed: Int
    @StringRes get() = when (this) {
        ImportIOData.ImportProblem.InvalidCrossrefReference -> R.string.import_problem_invalidref
        ImportIOData.ImportProblem.NonUniqueIds -> R.string.import_problem_nonunique
    }

@Preview(device = "spec:width=411dp,height=891dp")
@Composable
private fun ImportScreenErrorPreview() {
    OneUIPreview(title = "ImportScreenErrorPreview") {
        ImportScreen(
            state = ImportState.ImportFailed(
                listOf(
                    ImportIOData.ImportProblem.NonUniqueIds,
                    ImportIOData.ImportProblem.InvalidCrossrefReference
                )
            ),
            onEvent = { }
        )
    }
}

@Preview(device = "spec:width=411dp,height=891dp")
@Composable
private fun ImportScreenSuccessPreview() {
    OneUIPreview(title = "ImportScreenErrorPreview") {
        ImportScreen(
            state = ImportState.ImportSuccess(153, 34, 12, 6),
            onEvent = { }
        )
    }
}