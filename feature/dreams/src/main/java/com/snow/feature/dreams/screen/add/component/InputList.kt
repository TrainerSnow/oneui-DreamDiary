package com.snow.feature.dreams.screen.add.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.snow.diary.model.combine.PersonWithRelation
import com.snow.diary.model.data.Location
import com.snow.diary.ui.data.PersonPreviewData
import com.snow.feature.dreams.R
import org.oneui.compose.util.ListPosition
import org.oneui.compose.widgets.EditText
import org.oneui.compose.widgets.box.RoundedCornerListItem

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
    ) {
        persons.forEachIndexed { index, person ->
            RoundedCornerListItem(
                listPosition = if (index == 0) ListPosition.First
                else ListPosition.Middle
            ) {
                PersonInputListItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    person = person,
                    onDeleteClick = { onPersonDeleteClick(person) }
                )
            }
        }
        RoundedCornerListItem(
            modifier = Modifier
                .fillMaxWidth(),
            listPosition = ListPosition.Last
        ) {
            EditText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                hint = stringResource(R.string.dream_add_person_search_hint),
                value = query,
                onValueChange = onQueryChange
            )
        }
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
        RoundedCornerListItem(
            modifier = Modifier
                .fillMaxWidth(),
            listPosition = ListPosition.Last
        ) {
            EditText(
                modifier = Modifier
                    .fillMaxWidth(),
                hint = stringResource(R.string.dream_add_location_search_hint),
                value = query,
                onValueChange = onQueryChange
            )
        }
    }
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