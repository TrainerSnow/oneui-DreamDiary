package com.snow.diary.feature.persons.screen.add

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
import com.snow.diary.core.model.data.Relation
import com.snow.diary.core.ui.component.TextInputFormField
import com.snow.diary.core.ui.util.windowSizeClass
import com.snow.diary.feature.persons.R
import com.snow.diary.feature.persons.screen.add.components.RelationInputList
import org.oneui.compose.base.Icon
import org.oneui.compose.dialog.FullscreenDialogContent
import org.oneui.compose.dialog.FullscreenDialogLayout
import org.oneui.compose.layout.toolbar.CollapsingToolbarCollapsedState
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.layout.toolbar.rememberCollapsingToolbarState
import dev.oneuiproject.oneui.R as IconR

@Composable
internal fun AddPerson(
    viewModel: AddPersonViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val inputState by viewModel.inputState.collectAsStateWithLifecycle()
    val selectedRelations by viewModel.selectedRelations.collectAsStateWithLifecycle()
    val selectableRelation by viewModel.selectableRelations.collectAsStateWithLifecycle()
    val showRelationPopup by viewModel.showPopup.collectAsStateWithLifecycle()
    val isEdit = viewModel.isEdit

    AddPerson(
        state = inputState,
        selectedRelations = selectedRelations,
        selectableRelations = selectableRelation,
        showRelationPopup = showRelationPopup,
        isEdit = isEdit,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}


@Composable
private fun AddPerson(
    state: AddPersonState,
    selectedRelations: List<Relation>,
    selectableRelations: List<Relation>,
    showRelationPopup: Boolean,
    isEdit: Boolean,
    onEvent: (AddPersonEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    FullscreenDialogContent(
        modifier = Modifier
            .padding(WindowInsets.ime.asPaddingValues()),
        layout = FullscreenDialogLayout.fromSizeClass(windowSizeClass),
        positiveLabel = stringResource(R.string.person_add_save),
        onPositiveClick = {
            onEvent(
                AddPersonEvent.Save
            )
            onNavigateBack()
        },
        negativeLabel = stringResource(R.string.person_add_cancel),
        onNegativeClick = onNavigateBack
    ) {
        CollapsingToolbarLayout(
            toolbarTitle = stringResource(
                if (isEdit) R.string.person_edit_title
                else R.string.person_add_title
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
                    .spacedBy(AddPersonScreenDefaults.columnSpacing)
            ) {
                TextInputFormField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    input = state.name.input,
                    hint = stringResource(R.string.person_add_name),
                    onInputChange = { onEvent(AddPersonEvent.ChangeName(it)) },
                    icon = Icon.Resource(IconR.drawable.ic_oui_contact_outline)
                )
                TextInputFormField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    input = state.note.input,
                    hint = stringResource(R.string.person_add_note),
                    onInputChange = { onEvent(AddPersonEvent.ChangeNote(it)) },
                    icon = Icon.Resource(IconR.drawable.ic_oui_memo_outline)
                )

                RelationInputList(
                    modifier = Modifier
                        .fillMaxWidth(),
                    selectableRelations = selectableRelations,
                    selectedRelations = selectedRelations,
                    query = state.relationQuery,
                    showPopup = showRelationPopup,
                    onPopupDismiss = {
                        onEvent(
                            AddPersonEvent.ToggleRelationsPopup
                        )
                    },
                    onUnselectRelation = {
                        onEvent(
                            AddPersonEvent.UnselectRelation(it)
                        )
                    },
                    onSelectRelation = {
                        onEvent(
                            AddPersonEvent.SelectRelation(it)
                        )
                    },
                    onQueryChange = {
                        onEvent(
                            AddPersonEvent.ChangeRelationQUery(it)
                        )
                    }
                )
            }
        }
    }
}

//TODO: Many of these screen default values are duplicated per screen. Move to lib/central file (maybe :ui)
private object AddPersonScreenDefaults {

    val columnSpacing = 12.dp

}