package com.snow.diary.feature.dreams.screen.add.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.snow.diary.core.model.data.Location
import com.snow.diary.core.model.data.Person
import org.oneui.compose.input.InputList

@Composable
fun PersonInputList(
    modifier: Modifier = Modifier,
    selectablePersons: List<Person>,
    selectedPersons: List<Person>,
    query: String,
    showPopup: Boolean,
    onPopupDismiss: () -> Unit,
    onUnselectPerson: (Person) -> Unit,
    onSelectPerson: (Person) -> Unit,
    onQueryChange: (String) -> Unit
) = InputList(
    modifier = modifier,
    popupItems = selectablePersons,
    showPopup = showPopup,
    onPopupDismiss = onPopupDismiss,
    selectedItems = selectedPersons,
    nameOf = Person::name,
    onItemRemove = onUnselectPerson,
    onItemAdd = onSelectPerson,
    searchQuery = query,
    onSearchQueryChange = onQueryChange
)

@Composable
fun LocationInputList(
    modifier: Modifier = Modifier,
    selectableLocations: List<Location>,
    selectedLocations: List<Location>,
    query: String,
    showPopup: Boolean,
    onPopupDismiss: () -> Unit,
    onUnselectLocation: (Location) -> Unit,
    onSelectLocation: (Location) -> Unit,
    onQueryChange: (String) -> Unit
) = InputList(
    modifier = modifier,
    popupItems = selectableLocations,
    showPopup = showPopup,
    onPopupDismiss = onPopupDismiss,
    selectedItems = selectedLocations,
    nameOf = Location::name,
    onItemRemove = onUnselectLocation,
    onSelectLocation,
    searchQuery = query,
    onSearchQueryChange = onQueryChange
)