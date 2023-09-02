package com.snow.feature.dreams.screen.add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.feature.dreams.R
import com.snow.feature.dreams.screen.add.component.LocationInputList
import com.snow.feature.dreams.screen.add.component.PersonInputList
import com.snow.feature.dreams.screen.add.component.ShowMoreTextSeparator
import com.snow.feature.dreams.screen.add.component.TextInputFormField
import org.oneui.compose.base.Icon
import org.oneui.compose.layout.toolbar.CollapsingToolbarCollapsedState
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.layout.toolbar.rememberCollapsingToolbarState
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.widgets.buttons.IconButton
import org.oneui.compose.widgets.buttons.iconButtonColors
import org.oneui.compose.widgets.menu.MenuItem
import org.oneui.compose.widgets.menu.PopupMenu
import dev.oneuiproject.oneui.R as IconR

@Composable
internal fun AddDreamScreen(
    viewModel: AddDreamViewModel = hiltViewModel()
) {
    val inputState by viewModel.inputState.collectAsStateWithLifecycle()
    val extrasState by viewModel.extrasState.collectAsStateWithLifecycle()
    val queryState by viewModel.queryState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isEdit = viewModel.isEdit

    AddDreamScreen(
        inputState = inputState,
        extrasState = extrasState,
        queryState = queryState,
        uiState = uiState,
        isEdit = isEdit,
        onEvent = viewModel::onEvent
    )
}

//TODO: When available, this screen should use a fullscreen dialog (= PopOverActivity). FOr now, it uses a simple CTL
@Composable
private fun AddDreamScreen(
    inputState: AddDreamInputState,
    extrasState: AddDreamExtrasState,
    queryState: AddDreamQueryState,
    uiState: AddDreamUiState,
    isEdit: Boolean,
    onEvent: (AddDreamEvent) -> Unit
) {
    CollapsingToolbarLayout(
        toolbarTitle = stringResource(
            if (isEdit) R.string.dream_add_title_edit
            else R.string.dream_add_title
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
                        AddDreamEvent.ChangeMarkAsFavourite(!inputState.markAsFavourite)
                    )
                },
                icon = Icon.Resource(
                    if (inputState.markAsFavourite)
                        IconR.drawable.ic_oui_star
                    else
                        IconR.drawable.ic_oui_star_outline
                ),
                colors = iconButtonColors(
                    tint = if (inputState.markAsFavourite)
                        OneUITheme.colors.seslFunctionalOrange
                    else
                        OneUITheme.colors.seslPrimaryTextColor
                )
            )
        }
    ) {
        if (uiState.showPersonsPopup) {
            //TODO: Possibly move these popup-menus into the composable for InputList
            PopupMenu(
                modifier = Modifier
                    .heightIn(max = AddDreamScreenDefaults.popupMaxHeight),
                onDismissRequest = {
                    onEvent(
                        AddDreamEvent.TogglePersonPopup
                    )
                },
                properties = PopupProperties()
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    queryState.persons.forEach { person ->
                        MenuItem(
                            label = person.name,
                            onClick = {
                                onEvent(
                                    AddDreamEvent.SelectPerson(person)
                                )
                            }
                        )
                    }
                }
            }
        }

        if (uiState.showLocationsPopup) {
            PopupMenu(
                modifier = Modifier
                    .heightIn(max = AddDreamScreenDefaults.popupMaxHeight),
                onDismissRequest = {
                    onEvent(
                        AddDreamEvent.TogglePersonPopup
                    )
                },
                properties = PopupProperties()
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    queryState.locations.forEach { location ->
                        MenuItem(
                            label = location.name,
                            onClick = {
                                onEvent(
                                    AddDreamEvent.SelectLocation(location)
                                )
                            }
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement
                .spacedBy(AddDreamScreenDefaults.columnSpacing)
        ) {
            TextInputFormField(
                modifier = Modifier
                    .fillMaxWidth(),
                input = inputState.description.input,
                hint = stringResource(R.string.dream_add_content_hint),
                onInputChange = { onEvent(AddDreamEvent.ChangeDescription(it)) },
                icon = Icon.Resource(IconR.drawable.ic_oui_message_outline)
            )

            PersonInputList(
                persons = extrasState.persons,
                onPersonDeleteClick = {
                    onEvent(
                        AddDreamEvent.RemovePerson(it)
                    )
                },
                query = inputState.personQuery.input,
                onQueryChange = {
                    onEvent(
                        AddDreamEvent.ChangePersonQuery(it)
                    )
                }
            )

            ShowMoreTextSeparator(
                modifier = Modifier
                    .fillMaxWidth(),
                expanded = uiState.showAdvancedSettings,
                onClick = {
                    onEvent(
                        AddDreamEvent.ToggleAdvancedSettings
                    )
                }
            )

            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth(),
                visible = uiState.showAdvancedSettings,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement
                        .spacedBy(AddDreamScreenDefaults.columnSpacing)
                ) {
                    TextInputFormField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        input = inputState.note.input,
                        hint = stringResource(R.string.dream_add_note_hint),
                        onInputChange = { onEvent(AddDreamEvent.ChangeNote(it)) },
                        icon = Icon.Resource(IconR.drawable.ic_oui_memo_outline)
                    )

                    LocationInputList(
                        locations = extrasState.locations,
                        onLocationDeleteClick = {
                            onEvent(
                                AddDreamEvent.RemoveLocation(it)
                            )
                        },
                        query = inputState.locationQuery.input,
                        onQueryChange = {
                            onEvent(
                                AddDreamEvent.ChangeLocationQuery(it)
                            )
                        }
                    )
                }
            }
        }
    }
}


private object AddDreamScreenDefaults {

    val columnSpacing = 12.dp

    val popupMaxHeight = 100.dp

}