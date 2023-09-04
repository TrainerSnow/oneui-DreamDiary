package com.snow.feature.dreams.screen.add.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.snow.diary.model.data.Location
import com.snow.diary.model.data.Person
import com.snow.feature.dreams.R
import org.oneui.compose.base.Icon
import org.oneui.compose.util.ListPosition
import org.oneui.compose.widgets.box.RoundedCornerListItem
import org.oneui.compose.widgets.menu.MenuItem
import org.oneui.compose.widgets.menu.PopupMenu
import dev.oneuiproject.oneui.R as IconR

//TODO: Possibly move this into the lib


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
    item = { person, index ->
        RoundedCornerListItem(
            listPosition = if (index == 0) ListPosition.First
            else ListPosition.Middle,
            padding = InputListDefaults.itemPadding
        ) {
            PersonInputListItem(
                modifier = Modifier
                    .fillMaxWidth(),
                person = person,
                onDeleteClick = { onUnselectPerson(person) }
            )
        }
    },
    popupItem = { location ->
        MenuItem(
            label = location.name,
            onClick = {
                onSelectPerson(location)
            }
        )
    },
    searchText = { locations ->
        TextInputFormField(
            modifier = Modifier
                .fillMaxWidth(),
            input = query,
            onInputChange = onQueryChange,
            icon = Icon.Resource(IconR.drawable.ic_oui_contact_outline),
            listPosition = if (locations.isNotEmpty()) ListPosition.Last else ListPosition.Single,
            hint = stringResource(R.string.dream_add_person_search_hint)
        )
    }
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
    item = { location, index ->
        RoundedCornerListItem(
            listPosition = if (index == 0) ListPosition.First
            else ListPosition.Middle,
            padding = InputListDefaults.itemPadding
        ) {
            LocationInputItem(
                modifier = Modifier
                    .fillMaxWidth(),
                location = location,
                onDeleteClick = { onUnselectLocation(location) }
            )
        }
    },
    popupItem = { location ->
        MenuItem(
            label = location.name,
            onClick = {
                onSelectLocation(location)
            }
        )
    },
    searchText = { locations ->
        TextInputFormField(
            modifier = Modifier
                .fillMaxWidth(),
            input = query,
            onInputChange = onQueryChange,
            icon = Icon.Resource(IconR.drawable.ic_oui_location_outline),
            listPosition = if (locations.isNotEmpty()) ListPosition.Last else ListPosition.Single,
            hint = stringResource(R.string.dream_add_location_search_hint)
        )
    }
)

@Composable
fun <T> InputList(
    modifier: Modifier = Modifier,
    popupItems: List<T>,
    showPopup: Boolean,
    onPopupDismiss: () -> Unit,
    selectedItems: List<T>,
    item: @Composable (item: T, index: Int) -> Unit,
    popupItem: @Composable (T) -> Unit,
    searchText: @Composable (selectedItems: List<T>) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        val posProvider = object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize
            ): IntOffset {
                val spaceTop = anchorBounds.top
                val popupHeight = popupContentSize.height

                return if (popupHeight <= spaceTop) {
                    IntOffset(
                        x = 0,
                        y = anchorBounds.top - popupHeight
                    )
                } else IntOffset(
                    x = 0,
                    y = anchorBounds.bottom
                )
            }
        }

        selectedItems.forEachIndexed { index, item ->
            item(item, index)
        }
        Box {
            searchText(selectedItems)

            if (showPopup) {
                PopupMenu(
                    modifier = Modifier
                        .heightIn(max = 200.dp),
                    onDismissRequest = onPopupDismiss,
                    properties = PopupProperties(),
                    popupPositionProvider = posProvider
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                    ) {
                        popupItems.forEach { item ->
                            popupItem(item)
                        }
                    }
                }
            }
        }
    }
}

private object InputListDefaults {

    val itemPadding = PaddingValues(
        horizontal = 24.dp,
        vertical = 6.dp
    )

}