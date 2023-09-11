package com.snow.diary.feature.persons.screen.add.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.snow.diary.core.model.data.Relation
import com.snow.diary.feature.persons.R
import com.snow.diary.core.ui.component.InputList
import com.snow.diary.core.ui.component.TextInputFormField
import org.oneui.compose.base.Icon
import org.oneui.compose.util.ListPosition
import org.oneui.compose.widgets.box.RoundedCornerListItem
import org.oneui.compose.widgets.menu.MenuItem
import dev.oneuiproject.oneui.R as IconR

@Composable
fun RelationInputList(
    modifier: Modifier = Modifier,
    selectableRelations: List<Relation>,
    selectedRelations: List<Relation>,
    query: String,
    showPopup: Boolean,
    onPopupDismiss: () -> Unit,
    onUnselectRelation: (Relation) -> Unit,
    onSelectRelation: (Relation) -> Unit,
    onQueryChange: (String) -> Unit
) = InputList(
    modifier = modifier,
    popupItems = selectableRelations,
    showPopup = showPopup,
    onPopupDismiss = onPopupDismiss,
    selectedItems = selectedRelations,
    item = { relation, index ->
        RoundedCornerListItem(
            listPosition = if (index == 0) ListPosition.First
            else ListPosition.Middle,
            padding = InputListDefaults.itemPadding
        ) {
            RelationInputListItem(
                modifier = Modifier
                    .fillMaxWidth(),
                relation = relation,
                onDeleteClick = { onUnselectRelation(relation) }
            )
        }
    },
    popupItem = { location ->
        MenuItem(
            label = location.name,
            onClick = {
                onSelectRelation(location)
            }
        )
    },
    searchText = { locations ->
        TextInputFormField(
            modifier = Modifier
                .fillMaxWidth(),
            input = query,
            onInputChange = onQueryChange,
            icon = Icon.Resource(IconR.drawable.ic_oui_community),
            listPosition = if (locations.isNotEmpty()) ListPosition.Last else ListPosition.Single,
            hint = stringResource(R.string.person_add_relation_list_hint)
        )
    }
)

private object InputListDefaults {

    val itemPadding = PaddingValues(
        horizontal = 24.dp,
        vertical = 6.dp
    )

}