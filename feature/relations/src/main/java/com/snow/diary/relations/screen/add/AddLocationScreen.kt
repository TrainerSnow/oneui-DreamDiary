package com.snow.diary.relations.screen.add

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.diary.relations.R
import com.snow.diary.ui.component.TextInputFormField
import org.oneui.compose.base.Icon
import org.oneui.compose.layout.toolbar.CollapsingToolbarCollapsedState
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.layout.toolbar.rememberCollapsingToolbarState
import org.oneui.compose.picker.color.SimpleColorPickerPopup
import org.oneui.compose.widgets.box.RoundedCornerBox
import org.oneui.compose.widgets.buttons.ColoredButton
import org.oneui.compose.widgets.buttons.IconButton
import org.oneui.compose.widgets.buttons.TransparentButton
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


//TODO: When available, this screen should use a fullscreen dialog (= PopOverActivity). FOr now, it uses a simple CTL
@Composable
private fun AddRelation(
    inputState: AddRelationInputState,
    uiState: AddRelationUiState,
    isEdit: Boolean,
    onEvent: (AddRelationEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    CollapsingToolbarLayout(
        toolbarTitle = stringResource(
            if (isEdit) R.string.relation_edit_title
            else R.string.relation_add_title
        ),
        expandable = false,
        state = rememberCollapsingToolbarState(
            CollapsingToolbarCollapsedState.COLLAPSED,
            with(LocalDensity.current) { 100.dp.toPx() } //TODO Remove when lib is ready
        ),
        appbarNavAction = {
            IconButton(
                icon = Icon.Resource(IconR.drawable.ic_oui_close),
                onClick = onNavigateBack
            )
        },
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
                icon = Icon.Resource(IconR.drawable.ic_oui_memo_outline) //TODO: Proper icon
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

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TransparentButton(
                    label = stringResource(R.string.relation_add_cancel),
                    onClick = onNavigateBack
                )
                ColoredButton(
                    label = stringResource(R.string.relation_add_save),
                    onClick = {
                        onEvent(
                            AddRelationEvent.Save
                        )
                        onNavigateBack()
                    }
                )
            }
        }
    }
}

//TODO: Many of these screen default values are duplicated per screen. Move to lib/central file (maybe :ui)
private object AddRelationScreenDefaults {

    val columnSpacing = 12.dp

    val colorInputRadius = 24.dp

}