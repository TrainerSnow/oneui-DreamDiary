package com.snow.diary.persons.screen.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.snow.diary.model.data.Relation
import com.snow.diary.persons.R
import com.snow.diary.persons.screen.add.components.RelationInputList
import com.snow.diary.ui.component.TextInputFormField
import org.oneui.compose.base.Icon
import org.oneui.compose.layout.toolbar.CollapsingToolbarCollapsedState
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.layout.toolbar.rememberCollapsingToolbarState
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.widgets.buttons.ColoredButton
import org.oneui.compose.widgets.buttons.IconButton
import org.oneui.compose.widgets.buttons.TransparentButton
import org.oneui.compose.widgets.buttons.iconButtonColors
import dev.oneuiproject.oneui.R as IconR


//TODO: When available, this screen should use a fullscreen dialog (= PopOverActivity). FOr now, it uses a simple CTL
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
    CollapsingToolbarLayout(
        toolbarTitle = stringResource(
            if (isEdit) R.string.person_edit_title
            else R.string.person_add_title
        ),
        expandable = false,
        state = rememberCollapsingToolbarState(
            CollapsingToolbarCollapsedState.COLLAPSED,
            with(LocalDensity.current) { 100.dp.toPx() } //TODO Remove when lib is ready
        ),
        appbarActions = {
            IconButton(
                onClick = {
                    onEvent(
                        AddPersonEvent.ChangeMarkAsFavourite(!state.markAsFavourite)
                    )
                },
                icon = Icon.Resource(
                    if (state.markAsFavourite)
                        IconR.drawable.ic_oui_star
                    else
                        IconR.drawable.ic_oui_star_outline
                ),
                colors = iconButtonColors(
                    tint = if (state.markAsFavourite)
                        OneUITheme.colors.seslFunctionalOrange
                    else
                        OneUITheme.colors.seslPrimaryTextColor
                )
            )
        }
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

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TransparentButton(
                    label = stringResource(R.string.person_add_cancel),
                    onClick = onNavigateBack
                )
                ColoredButton(
                    label = stringResource(R.string.person_add_save),
                    onClick = {
                        onEvent(
                            AddPersonEvent.Save
                        )
                        onNavigateBack()
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