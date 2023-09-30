package com.snow.diary.feature.locations.screen.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.diary.core.ui.component.TextInputFormField
import com.snow.diary.feature.locations.R
import org.oneui.compose.base.Icon
import org.oneui.compose.dialog.FullscreenDialogContent
import org.oneui.compose.layout.toolbar.CollapsingToolbarCollapsedState
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.layout.toolbar.rememberCollapsingToolbarState
import dev.oneuiproject.oneui.R as IconR

@Composable
internal fun AddLocation(
    viewModel: AddLocationViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val inputState by viewModel.inputState.collectAsStateWithLifecycle()
    val isEdit = viewModel.isEdit

    AddLocation(
        state = inputState,
        isEdit = isEdit,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}


@Composable
private fun AddLocation(
    state: AddLocationState,
    isEdit: Boolean,
    onEvent: (AddLocationEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    FullscreenDialogContent(
        modifier = Modifier
            .padding(WindowInsets.ime.asPaddingValues()),
        positiveLabel = stringResource(R.string.location_add_save),
        onPositiveClick = {
            onEvent(
                AddLocationEvent.Save
            )
            onNavigateBack()
        },
        negativeLabel = stringResource(R.string.location_add_cancel),
        onNegativeClick = onNavigateBack
    ) {
        CollapsingToolbarLayout(
            toolbarTitle = stringResource(
                if (isEdit) R.string.location_edit_title
                else R.string.location_add_title
            ),
            state = rememberCollapsingToolbarState(
                CollapsingToolbarCollapsedState.COLLAPSED
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement
                    .spacedBy(AddLocationScreenDefaults.columnSpacing)
            ) {
                TextInputFormField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    input = state.name.input,
                    hint = stringResource(R.string.location_add_name),
                    onInputChange = { onEvent(AddLocationEvent.ChangeName(it)) },
                    icon = Icon.Resource(IconR.drawable.ic_oui_location_outline)
                )
                TextInputFormField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    input = state.note.input,
                    hint = stringResource(R.string.location_add_note),
                    onInputChange = { onEvent(AddLocationEvent.ChangeNote(it)) },
                    icon = Icon.Resource(IconR.drawable.ic_oui_memo_outline)
                )
            }
        }
    }
}

//TODO: Many of these screen default values are duplicated per screen. Move to lib/central file (maybe :ui)
private object AddLocationScreenDefaults {

    val columnSpacing = 12.dp

}