package com.snow.diary.feature.dreams.screen.add.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.snow.diary.core.model.data.Location
import com.snow.diary.core.model.data.Person
import com.snow.diary.feature.dreams.R
import org.oneui.compose.base.Icon
import org.oneui.compose.input.InputList
import dev.oneuiproject.oneui.R as IconR

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
    onSearchQueryChange = onQueryChange,
    hint = stringResource(R.string.dream_add_person_search_hint),
    icon = Icon.Resource(IconR.drawable.ic_oui_contact_outline)
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
    onSearchQueryChange = onQueryChange,
    hint = stringResource(R.string.dream_add_location_search_hint),
    icon = Icon.Resource(IconR.drawable.ic_oui_location_outline)
)