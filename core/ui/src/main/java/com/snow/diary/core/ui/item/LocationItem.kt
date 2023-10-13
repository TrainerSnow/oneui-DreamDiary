package com.snow.diary.core.ui.item

/*import com.snow.diary.core.ui.data.LocationPreviewData*/
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.snow.diary.core.common.removeLineBreaks
import com.snow.diary.core.model.data.Location
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.util.ListPosition
import org.oneui.compose.widgets.box.RoundedCornerListItem

@Composable
fun LocationCard(
    modifier: Modifier = Modifier,
    location: Location,
    onClick: ((Location) -> Unit)? = null,
    listPosition: ListPosition = ListPosition.Single
) {
    val titleTextStyle = TextStyle(
        fontSize = 17.sp,
        fontWeight = FontWeight.Medium
    )
    val descTextStyle = TextStyle(
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
            Text(
                text = location.name,
                style = titleTextStyle
            )

            if(location.notes != null) {
                Text(
                    text = location.notes!!.removeLineBreaks(),
                    style = descTextStyle,
                    maxLines = LocationItemDefaults.descMaxLines,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

object LocationItemDefaults {

    const val descMaxLines = 3

}
