package com.snow.feature.dreams.screen.add.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.snow.diary.model.combine.PersonWithRelation
import com.snow.diary.model.data.Location
import com.snow.diary.ui.data.PersonPreviewData
import com.snow.feature.dreams.R
import org.oneui.compose.base.Icon
import org.oneui.compose.util.ListPosition
import org.oneui.compose.widgets.box.RoundedCornerListItem
import dev.oneuiproject.oneui.R as IconR

//TODO: Possibly move this into the lib
@Composable
fun PersonInputList(
    modifier: Modifier = Modifier,
    persons: List<PersonWithRelation>,
    onPersonDeleteClick: (PersonWithRelation) -> Unit,
    query: String,
    onQueryChange: (String) -> Unit
) {
    Column(
        modifier = modifier
            .animateContentSize()
    ) {
        persons.forEachIndexed { index, person ->
            RoundedCornerListItem(
                listPosition = if (index == 0) ListPosition.First
                else ListPosition.Middle,
                padding = InputListDefaults.itemPadding
            ) {
                PersonInputListItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    person = person,
                    onDeleteClick = { onPersonDeleteClick(person) }
                )
            }
        }
        TextInputFormField(
            modifier = Modifier
                .fillMaxWidth(),
            input = query,
            onInputChange = onQueryChange,
            icon = Icon.Resource(IconR.drawable.ic_oui_contact_outline),
            listPosition = if (persons.isNotEmpty()) ListPosition.Last else ListPosition.Single,
            hint = stringResource(R.string.dream_add_person_search_hint)
        )
    }
}

@Composable
fun LocationInputList(
    modifier: Modifier = Modifier,
    locations: List<Location>,
    onLocationDeleteClick: (Location) -> Unit,
    query: String,
    onQueryChange: (String) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        locations.forEachIndexed { index, location ->
            RoundedCornerListItem(
                listPosition = if (index == 0) ListPosition.First
                else ListPosition.Middle
            ) {
                LocationInputItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    location = location,
                    onDeleteClick = { onLocationDeleteClick(location) }
                )
            }
        }
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
}

private object InputListDefaults {

    val itemPadding = PaddingValues(
        horizontal = 24.dp,
        vertical = 6.dp
    )

}

@Preview
@Composable
fun PersonInputListPreview() {
    PersonInputList(
        persons = PersonPreviewData.personsWithRelation.take(4),
        onPersonDeleteClick = {},
        query = "",
        onQueryChange = {})
}