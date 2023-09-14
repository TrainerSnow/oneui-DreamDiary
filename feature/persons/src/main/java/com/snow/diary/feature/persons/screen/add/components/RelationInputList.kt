package com.snow.diary.feature.persons.screen.add.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.snow.diary.core.model.data.Relation
import org.oneui.compose.input.InputList

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
    onSearchQueryChange = onQueryChange
)