package com.snow.diary.feature.dreams.screen.add.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snow.diary.core.model.data.Location
import com.snow.diary.core.model.data.Person
import org.oneui.compose.base.Icon
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.widgets.buttons.IconButton
import org.oneui.compose.widgets.buttons.iconButtonColors
import dev.oneuiproject.oneui.R as IconR

@Composable
fun PersonInputListItem(
    modifier: Modifier = Modifier,
    person: Person,
    onDeleteClick: (Person) -> Unit
) {
    val nameStyle = TextStyle(
        fontSize = 17.sp
    )

    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            style = nameStyle,
            text = person.name
        )

        Spacer(
            modifier = Modifier
                .weight(1F)
        )

        IconButton(
            icon = Icon.Resource(IconR.drawable.ic_oui_minus),
            colors = iconButtonColors(
                tint = OneUITheme.colors.seslFunctionalRed
            ),
            onClick = { onDeleteClick(person) }
        )
    }
}

@Composable
fun LocationInputItem(
    modifier: Modifier = Modifier,
    location: Location,
    onDeleteClick: (Location) -> Unit
) {
    val nameStyle = TextStyle(
        fontSize = 13.sp
    )

    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ){
        Spacer(
            modifier = Modifier
                .width(InputListItemDefaults.dotNameSpacing)
        )
        Text(
            style = nameStyle,
            text = location.name
        )

        Spacer(
            modifier = Modifier
                .weight(1F)
        )

        IconButton(
            icon = Icon.Resource(IconR.drawable.ic_oui_minus),
            colors = iconButtonColors(
                tint = OneUITheme.colors.seslFunctionalRed
            ),
            onClick = { onDeleteClick(location) }
        )
    }
}

private object InputListItemDefaults {

    val dotNameSpacing = 18.dp

}