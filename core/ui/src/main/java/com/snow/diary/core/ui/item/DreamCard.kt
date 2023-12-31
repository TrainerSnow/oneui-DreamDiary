package com.snow.diary.core.ui.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snow.diary.core.common.removeLineBreaks
import com.snow.diary.core.common.time.TimeFormat.formatFullDescription
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.ui.R
import com.snow.diary.core.ui.callback.DreamCallback
import com.snow.diary.core.ui.data.DreamPreviewData
import org.oneui.compose.base.Icon
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.util.ListPosition
import org.oneui.compose.util.OneUIPreview
import org.oneui.compose.widgets.box.RoundedCornerListItem
import org.oneui.compose.widgets.buttons.IconButton
import org.oneui.compose.widgets.buttons.iconButtonColors
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit
import dev.oneuiproject.oneui.R as IconR

@Composable
fun DreamCard(
    modifier: Modifier = Modifier,
    dream: Dream,
    listPosition: ListPosition = ListPosition.Middle,
    dreamCallback: DreamCallback = DreamCallback
) {
    val titleTextStyle = TextStyle(
        fontSize = 17.sp,
        fontWeight = FontWeight.Medium
    )
    val timeAgoTextStyle = TextStyle(
        color = OneUITheme.colors.seslSecondaryTextColor,
        fontSize = 12.sp
    )
    val descTextStyle = TextStyle(
        fontSize = 13.sp
    )
    val updatedTextStyle = TextStyle(
        color = OneUITheme.colors.seslSecondaryTextColor,
        fontSize = 10.sp
    )

    RoundedCornerListItem(
        modifier = modifier,
        onClick = { dreamCallback.onClick(dream) },
        listPosition = listPosition
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dream.created.formatFullDescription(),
                    style = titleTextStyle
                )

                val period = Period.between(dream.created, LocalDate.now())
                val isToday = period.isZero
                val days = ChronoUnit.DAYS.between(dream.created, LocalDate.now())

                val label =
                    if (isToday) stringResource(R.string.dreamcard_time_passed_placeholder)
                    else stringResource(
                        id = R.string.dreamcard_time_passed,
                        days.toString()
                    )
                Text(
                    text = label,
                    style = timeAgoTextStyle
                )
            }

            Spacer(
                modifier = Modifier
                    .height(DreamCardDefaults.headerTextSpacing)
            )

            Text(
                text = dream.description.removeLineBreaks(),
                style = descTextStyle,
                maxLines = DreamCardDefaults.maxLinesDesc,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier
                    .height(DreamCardDefaults.textBottomSpacing)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement
                    .spacedBy(
                        DreamCardDefaults.updatedIconSpacing,
                        alignment = Alignment.End
                    )
            ) {
                val didUpdate = dream.updated != dream.created
                if (didUpdate) {
                    val days = Period.between(dream.updated, LocalDate.now()).days
                    Text(
                        text = if (days == 0) stringResource(R.string.dreamcard_time_passed_placeholder) else stringResource(
                            id = R.string.dreamcard_updated,
                            days.toString()
                        ),
                        style = updatedTextStyle
                    )
                }

                IconButton(
                    padding = PaddingValues(),
                    icon = Icon.Resource(
                        if (dream.isFavourite) IconR.drawable.ic_oui_favorite_on
                        else IconR.drawable.ic_oui_favorite_off
                    ),
                    modifier = Modifier
                        .size(DreamCardDefaults.favIconSize),
                    colors = if (dream.isFavourite) iconButtonColors(
                        tint = OneUITheme.colors.seslFunctionalOrange
                    ) else iconButtonColors(),
                    onClick = { dreamCallback.onFavouriteClick(dream) }
                )
            }
        }
    }
}

private object DreamCardDefaults {

    val headerTextSpacing = 4.dp

    val textBottomSpacing = 4.dp

    val favIconSize = 20.dp

    val updatedIconSpacing = 8.dp

    const val maxLinesDesc = 3

    const val borderStrokeWidth = 4F

}

@Preview
@Composable
fun DreamCardPreview() = OneUIPreview(
    title = "DreamCard",
    padding = PaddingValues()
) {
    DreamCard(
        dream = DreamPreviewData.dreams.random()
    )
}