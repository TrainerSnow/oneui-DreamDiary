package com.snow.diary.feature.relations.screen.add

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.diary.core.ui.component.TextInputFormField
import com.snow.diary.core.ui.util.windowSizeClass
import com.snow.diary.feature.relations.R
import org.oneui.compose.base.Icon
import org.oneui.compose.dialog.FullscreenDialogContent
import org.oneui.compose.dialog.FullscreenDialogLayout
import org.oneui.compose.layout.toolbar.CollapsingToolbarCollapsedState
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.layout.toolbar.rememberCollapsingToolbarState
import org.oneui.compose.picker.color.SimpleColorPickerPopup
import org.oneui.compose.widgets.box.RoundedCornerBox
import dev.oneuiproject.oneui.R as IconR

@Composable
internal fun AddRelation(
    viewModel: AddRelationViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val inputState by viewModel.inputState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isEdit = viewModel.isEdit

    AddRelation(
        inputState = inputState,
        uiState = uiState,
        isEdit = isEdit,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}


@Composable
private fun AddRelation(
    inputState: AddRelationInputState,
    uiState: AddRelationUiState,
    isEdit: Boolean,
    onEvent: (AddRelationEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    FullscreenDialogContent(
        modifier = Modifier
            .padding(WindowInsets.ime.asPaddingValues()),
        layout = FullscreenDialogLayout.fromSizeClass(windowSizeClass),
        positiveLabel = stringResource(R.string.relation_add_save),
        onPositiveClick = {
            onEvent(
                AddRelationEvent.Save
            )
            onNavigateBack()
        },
        negativeLabel = stringResource(R.string.relation_add_cancel),
        onNegativeClick = onNavigateBack
    ) {
        CollapsingToolbarLayout(
            toolbarTitle = stringResource(
                if (isEdit) R.string.relation_edit_title
                else R.string.relation_add_title
            ),
            state = rememberCollapsingToolbarState(
                CollapsingToolbarCollapsedState.COLLAPSED
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement
                    .spacedBy(AddRelationScreenDefaults.columnSpacing)
            ) {
                TextInputFormField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    input = inputState.name.input,
                    hint = stringResource(R.string.relation_add_name),
                    onInputChange = { onEvent(AddRelationEvent.ChangeName(it)) },
                    icon = Icon.Resource(IconR.drawable.ic_oui_community)
                )
                TextInputFormField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    input = inputState.note.input,
                    hint = stringResource(R.string.relation_add_notes),
                    onInputChange = { onEvent(AddRelationEvent.ChangeNote(it)) },
                    icon = Icon.Resource(IconR.drawable.ic_oui_memo_outline)
                )

                RoundedCornerBox(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        onEvent(
                            AddRelationEvent.ToggleColorPopupVisibility
                        )
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (uiState.showColorPickerPopup) {
                            SimpleColorPickerPopup(
                                onColorSelected = {
                                    onEvent(
                                        AddRelationEvent.ChangeColor(it)
                                    )
                                    onEvent(
                                        AddRelationEvent.ToggleColorPopupVisibility
                                    )
                                },
                                onDismissRequest = {
                                    onEvent(
                                        AddRelationEvent.ToggleColorPopupVisibility
                                    )
                                }
                            )
                        }
                        Text(
                            text = stringResource(R.string.relation_add_color)
                        )
                        Spacer(
                            modifier = Modifier
                                .weight(1F)
                        )
                        Box(
                            modifier = Modifier
                                .size(size = AddRelationScreenDefaults.colorInputRadius)
                                .background(color = inputState.color, shape = CircleShape)
                                .clip(CircleShape)
                                .clickable {
                                    onEvent(AddRelationEvent.ToggleColorPopupVisibility)
                                }
                        )
                    }
                }
            }
        }
    }
}

//TODO: Many of these screen default values are duplicated per screen. Move to lib/central file (maybe :ui)
private object AddRelationScreenDefaults {

    val columnSpacing = 12.dp

    val colorInputRadius = 24.dp

}