package com.snow.diary.feature.persons.screen.add.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.snow.diary.core.model.data.Relation
import com.snow.diary.feature.persons.R
import org.oneui.compose.base.Icon
import org.oneui.compose.input.InputList
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
    nameOf = Relation::name,
    onItemRemove = onUnselectRelation,
    onItemAdd = onSelectRelation,
    searchQuery = query,
    onSearchQueryChange = onQueryChange,
    hint = stringResource(R.string.person_add_relation_list_hint),
    icon = Icon.Resource(IconR.drawable.ic_oui_community)
)