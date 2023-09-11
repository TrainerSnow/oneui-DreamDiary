package com.snow.diary.ui.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.snow.diary.model.data.Relation
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.util.ListPosition
import org.oneui.compose.widgets.box.RoundedCornerListItem

@Composable
fun RelationCard(
    modifier: Modifier = Modifier,
    relation: Relation,
    onClick: ((Relation) -> Unit)? = null,
    listPosition: ListPosition = ListPosition.Single
) {
    val titleTextStyle = TextStyle(
        color = OneUITheme.colors.seslPrimaryTextColor,
        fontSize = 17.sp,
        fontWeight = FontWeight.Medium
    )
    val descTextStyle = TextStyle(
        color = OneUITheme.colors.seslPrimaryTextColor,
        fontSize = 13.sp
    )

    RoundedCornerListItem(
        modifier = modifier,
        onClick = { onClick?.let { it(relation) } },
        listPosition = listPosition
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = relation.name,
                    style = titleTextStyle
                )
            }

            //TODO: Add note to relations
            /*Text(
                text = relation.note?.removeLineBreaks().orEmpty(),
                style = descTextStyle,
                maxLines = LocationItemDefaults.descMaxLines,
                overflow = TextOverflow.Ellipsis
            )*/
        }
    }
}

private object RelationItemDefaults {

    const val descMaxLines = 3

}
