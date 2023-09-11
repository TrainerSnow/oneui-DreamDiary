package com.snow.diary.feature.dreams.screen.add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.diary.core.ui.component.OptionalInput
import com.snow.diary.core.ui.component.TextInputFormField
import com.snow.diary.feature.dreams.R
import com.snow.diary.feature.dreams.screen.add.component.LocationInputList
import com.snow.diary.feature.dreams.screen.add.component.PersonInputList
import com.snow.diary.feature.dreams.screen.add.component.ShowMoreTextSeparator
import org.oneui.compose.base.Icon
import org.oneui.compose.layout.toolbar.CollapsingToolbarCollapsedState
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.layout.toolbar.rememberCollapsingToolbarState
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.widgets.HorizontalSeekbar
import org.oneui.compose.widgets.buttons.ColoredButton
import org.oneui.compose.widgets.buttons.IconButton
import org.oneui.compose.widgets.buttons.TransparentButton
import org.oneui.compose.widgets.buttons.iconButtonColors
import org.oneui.compose.widgets.seekBarColors
import dev.oneuiproject.oneui.R as IconR

@Composable
internal fun AddDreamScreen(
    viewModel: AddDreamViewModel = hiltViewModel(),
    dismissDream: () -> Unit
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
        onEvent = viewModel::onEvent,
        dismissDream = dismissDream
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
    onEvent: (AddDreamEvent) -> Unit,
    dismissDream: () -> Unit
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
        appbarNavAction = {
            IconButton(
                icon = Icon.Resource(IconR.drawable.ic_oui_close),
                onClick = dismissDream
            )
        },
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
                modifier = Modifier
                    .fillMaxWidth(),
                selectablePersons = queryState.persons,
                selectedPersons = extrasState.persons,
                query = inputState.personQuery.input,
                showPopup = uiState.showPersonsPopup,
                onPopupDismiss = {
                    onEvent(
                        AddDreamEvent.TogglePersonPopup
                    )
                },
                onUnselectPerson = {
                    onEvent(
                        AddDreamEvent.RemovePerson(it)
                    )
                },
                onSelectPerson = {
                    onEvent(
                        AddDreamEvent.SelectPerson(it)
                    )
                },
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
                        modifier = Modifier
                            .fillMaxWidth(),
                        selectableLocations = queryState.locations,
                        selectedLocations = extrasState.locations,
                        query = inputState.locationQuery.input,
                        showPopup = uiState.showLocationsPopup,
                        onPopupDismiss = {
                            onEvent(
                                AddDreamEvent.ToggleLocationPopup
                            )
                        },
                        onUnselectLocation = {
                            onEvent(
                                AddDreamEvent.RemoveLocation(it)
                            )
                        },
                        onSelectLocation = {
                            onEvent(
                                AddDreamEvent.SelectLocation(it)
                            )
                        },
                        onQueryChange = {
                            onEvent(
                                AddDreamEvent.ChangeLocationQuery(it)
                            )
                        }
                    )

                    OptionalInput(
                        modifier = Modifier
                            .fillMaxWidth(),
                        title = stringResource(R.string.dream_add_happiness),
                        expanded = inputState.happiness != null,
                        onExpandedChange = {
                            onEvent(
                                AddDreamEvent.ChangeHappiness(if (it) 0.5F else null)
                            )
                        }
                    ) {
                        HorizontalSeekbar(
                            value = inputState.happiness!!,
                            onValueChange = {
                                onEvent(
                                    AddDreamEvent.ChangeHappiness(it)
                                )
                            },
                            colors = seekBarColors(
                                color = Color(0xfffcca05) //TODO xml values for these colors
                            )
                        )
                    }

                    OptionalInput(
                        modifier = Modifier
                            .fillMaxWidth(),
                        title = stringResource(R.string.dream_add_clearness),
                        expanded = inputState.clearness != null,
                        onExpandedChange = {
                            onEvent(
                                AddDreamEvent.ChangeClearness(
                                    if (it) 0.5F else null
                                )
                            )
                        }
                    ) {
                        HorizontalSeekbar(
                            value = inputState.clearness!!,
                            onValueChange = {
                                onEvent(
                                    AddDreamEvent.ChangeClearness(it)
                                )
                            },
                            colors = seekBarColors(
                                color = Color(0xff63d1d2) //TODO xml values for these colors
                            )
                        )
                    }
                }
            }


            //TODO: These buttons are only temporary, until the lib allows for fullscreen dialogs (=PopOverActivity)
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TransparentButton(
                    label = stringResource(R.string.dream_add_cancel),
                    onClick = dismissDream
                )
                ColoredButton(
                    label = stringResource(R.string.dream_add_save),
                    onClick = {
                        onEvent(
                            AddDreamEvent.Add
                        )
                        dismissDream()
                    }
                )
            }
        }
    }
}


private object AddDreamScreenDefaults {

    val columnSpacing = 12.dp

}