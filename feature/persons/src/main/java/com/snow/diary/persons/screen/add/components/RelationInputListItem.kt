package com.snow.diary.persons.screen.add.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.snow.diary.model.data.Relation
import dev.oneuiproject.oneui.R
import org.oneui.compose.base.Icon
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.widgets.buttons.IconButton
import org.oneui.compose.widgets.buttons.iconButtonColors

@Composable
fun RelationInputListItem(
    modifier: Modifier = Modifier,
    relation: Relation,
    onDeleteClick: (Relation) -> Unit
) {
    val nameStyle = TextStyle(
        fontSize = 17.sp
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            style = nameStyle,
            text = relation.name
        )

        Spacer(
            modifier = Modifier
                .weight(1F)
        )

        IconButton(
            icon = Icon.Resource(R.drawable.ic_oui_remove_2),
            colors = iconButtonColors(
                tint = OneUITheme.colors.seslFunctionalRed
            ),
            onClick = { onDeleteClick(relation) }
        )
    }
}