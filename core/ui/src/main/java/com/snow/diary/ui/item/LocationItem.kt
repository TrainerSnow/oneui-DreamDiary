package com.snow.diary.ui.item

/*import com.snow.diary.ui.data.LocationPreviewData*/
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.snow.diary.common.removeLineBreaks
import com.snow.diary.model.data.Location
import org.oneui.compose.base.Icon
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.util.ListPosition
import org.oneui.compose.widgets.box.RoundedCornerListItem
import org.oneui.compose.widgets.buttons.IconButton
import dev.oneuiproject.oneui.R as IconR

@Composable
fun LocationCard(
    modifier: Modifier = Modifier,
    location: Location,
    onClick: ((Location) -> Unit)? = null,
    onLocationClick: ((Location) -> Unit)? = null,
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
        onClick = { onClick?.let { it(location) } },
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
                    text = location.name,
                    style = titleTextStyle
                )
                IconButton(
                    icon = Icon.Resource(IconR.drawable.ic_oui_location_outline),
                    onClick = { onLocationClick?.let { it(location) } },
                    padding = PaddingValues()
                )
            }

            Text(
                text = location.notes?.removeLineBreaks().orEmpty(),
                style = descTextStyle,
                maxLines = LocationItemDefaults.descMaxLines,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private object LocationItemDefaults {

    const val descMaxLines = 3

}

/*
@Preview
@Composable
fun LocationCard() = OneUIPreview(title = "LocationCard", padding = PaddingValues()) {
    LocationCard(
        location = LocationPreviewData
            .locations
            .random()
    )
}*/
