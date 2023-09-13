package com.snow.diary.core.ui.item

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snow.diary.core.common.removeLineBreaks
import com.snow.diary.core.model.data.Person
import com.snow.diary.core.model.data.Relation
import com.snow.diary.core.ui.callback.PersonCallback
import com.snow.diary.core.ui.data.PersonPreviewData
import com.snow.diary.core.ui.data.RelationPreviewData
import org.oneui.compose.base.Icon
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.util.ListPosition
import org.oneui.compose.util.OneUIPreview
import org.oneui.compose.widgets.box.RoundedCornerListItem
import org.oneui.compose.widgets.buttons.IconButton
import org.oneui.compose.widgets.buttons.iconButtonColors
import dev.oneuiproject.oneui.R as IconR

@Composable
fun PersonCard(
    modifier: Modifier = Modifier,
    person: Person,
    relations: List<Relation> = emptyList(),
    personCallback: PersonCallback = PersonCallback,
    listPosition: ListPosition = ListPosition.Single,
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
                RelationsColorCircle(
                    relations = relations,
                    onClick = personCallback::onRelationsClick
                )
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
                    padding = PaddingValues(),
                    icon = Icon.Resource(
                        if (person.isFavourite) IconR.drawable.ic_oui_favorite_on
                        else IconR.drawable.ic_oui_favorite_off
                    ),
                    modifier = Modifier
                        .size(PersonCardDefaults.favIconSize),
                    colors = if (person.isFavourite) iconButtonColors(
                        tint = OneUITheme.colors.seslFunctionalOrange
                    ) else iconButtonColors(),
                    onClick = { personCallback.onFavouriteClick(person) }
                )
            }
        }
    }
}

@Composable
private fun RelationsColorCircle(
    relations: List<Relation>,
    onClick: (List<Relation>) -> Unit
) = Canvas(
    modifier = Modifier
        .size(
            size = PersonCardDefaults.relationCircleRadius * 2
        )
        .clickable { relations.let(onClick) }
) {
    require(relations.isNotEmpty())
    var startDeg = 0F
    val perDeg = 360F / relations.size

    relations.forEach { relation ->
        drawArc(
            color = Color(relation.color),
            startAngle = startDeg,
            sweepAngle = perDeg,
            useCenter = true
        )

        startDeg += perDeg
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

    val favIconSize = 20.dp

}

@Preview
@Composable
fun PersonCard() = OneUIPreview(title = "PersonCard", padding = PaddingValues()) {
    PersonCard(
        person = PersonPreviewData
            .persons
            .random(),
        relations = RelationPreviewData
            .relations
            .take(3)
    )
}
