package com.snow.diary.ui.dream

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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snow.diary.common.removeLineBreaks
import com.snow.diary.common.time.TimeFormat.formatFullDescription
import com.snow.diary.common.util.ListPosition
import com.snow.diary.model.data.Dream
import com.snow.diary.ui.R
import com.snow.diary.ui.param.DreamParamProvider
import org.oneui.compose.base.Icon
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.util.OneUIPreview
import org.oneui.compose.widgets.box.RoundedCornerBox
import org.oneui.compose.widgets.buttons.IconButton
import java.time.LocalDate
import java.time.Period
import dev.oneuiproject.oneui.R as IconR

@Composable
fun DreamCard(
    modifier: Modifier = Modifier,
    dream: Dream,
    //TODO: When https://github.com/TrainerSnow/oneui-compose/issues/26 is resolved, use this param to style the component
    listPosition: ListPosition = ListPosition.Middle,
    onClick: (() -> Unit)? = null,
    onFavouriteClick: (() -> Unit)? = null
) {
    val titleTextStyle = TextStyle(
        color = OneUITheme.colors.seslPrimaryTextColor,
        fontSize = 17.sp,
        fontWeight = FontWeight.Medium
    )
    val timeAgoTextStyle = TextStyle(
        color = OneUITheme.colors.seslSecondaryTextColor,
        fontSize = 12.sp
    )
    val descTextStyle = TextStyle(
        color = OneUITheme.colors.seslPrimaryTextColor,
        fontSize = 13.sp
    )
    val updatedTextStyle = TextStyle(
        color = OneUITheme.colors.seslSecondaryTextColor,
        fontSize = 10.sp
    )

    RoundedCornerBox(
        onClick = onClick,
        modifier = modifier
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

                val days = Period.between(dream.created, LocalDate.now()).days
                val label =
                    if (days == 0) stringResource(R.string.dreamcard_time_passed_placeholder)
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
                if(didUpdate) {
                    val days = Period.between(dream.updated, LocalDate.now()).days
                    Text(
                        text = stringResource(
                            id = R.string.dreamcard_updated,
                            days.toString()
                        ),
                        style = updatedTextStyle
                    )
                }

                //TODO: Animate this button!
                IconButton(
                    padding = PaddingValues(),
                    icon = Icon.Resource(IconR.drawable.ic_oui_favorite_off),
                    modifier = Modifier
                        .size(DreamCardDefaults.favIconSize),
                    onClick = { onFavouriteClick?.let { it() } }
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

}

@Preview
@Composable
fun DreamCardPreview(
    @PreviewParameter(
        DreamParamProvider::class,
        1
    ) dream: Dream
) = OneUIPreview(
    title = "DreamCard",
    padding = PaddingValues()
) {
    DreamCard(
        dream = dream,
        onClick = { }
    )
}