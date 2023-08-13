package com.snow.diary.ui.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snow.diary.common.removeLineBreaks
import com.snow.diary.model.data.Person
import com.snow.diary.model.data.Relation
import com.snow.diary.ui.callback.PersonCallback
import com.snow.diary.ui.data.PersonPreviewData
import com.snow.diary.ui.data.RelationPreviewData
import org.oneui.compose.base.Icon
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.util.ListPosition
import org.oneui.compose.util.OneUIPreview
import org.oneui.compose.widgets.box.RoundedCornerListItem
import org.oneui.compose.widgets.buttons.IconButton
import dev.oneuiproject.oneui.R as IconR

@Composable
fun PersonCard(
    modifier: Modifier = Modifier,
    person: Person,
    relation: Relation? = null,
    personCallback: PersonCallback = PersonCallback,
    listPosition: ListPosition = ListPosition.Middle,
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
        onClick = { personCallback.onClick(person) },
        listPosition = listPosition,
        padding = PersonCardDefaults.padding
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement
                    .spacedBy(PersonCardDefaults.relCircleSpacing)
            ) {
                relation?.let { rel ->
                    Box(
                        modifier = Modifier
                            .size(PersonCardDefaults.relationCircleRadius * 2)
                            .clip(CircleShape)
                            .background(
                                color = Color(rel.color.toArgb()),
                                shape = CircleShape
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { personCallback.onRelationClick(rel) }
                            )
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1F)
                ) {
                    Text(
                        text = person.name,
                        style = titleTextStyle
                    )
                    Row {
                        person.notes?.let { notes ->
                            Text(
                                text = notes.removeLineBreaks(),
                                style = descTextStyle,
                                maxLines = PersonCardDefaults.notesMaxLines
                            )
                        }
                    }
                }

                IconButton(
                    icon = Icon.Resource(IconR.drawable.ic_oui_favorite_off),
                    padding = PaddingValues(),
                    onClick = { personCallback.onFavouriteClick(person) }
                )
            }
        }
    }
}

private object PersonCardDefaults {

    val relationCircleRadius = 12.dp

    const val notesMaxLines = 3

    val relCircleSpacing = 12.dp

    val padding = PaddingValues(
        start = 12.dp,
        top = 12.dp,
        end = 24.dp,
        bottom = 12.dp
    )

}

@Preview
@Composable
fun PersonCard() = OneUIPreview(title = "PersonCard", padding = PaddingValues()) {
    PersonCard(
        person = PersonPreviewData
            .persons
            .component1(),
        relation = RelationPreviewData
            .relations
            .first(),
        listPosition = ListPosition.Single
    )
}
