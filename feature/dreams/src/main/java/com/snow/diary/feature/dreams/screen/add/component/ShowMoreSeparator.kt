package com.snow.diary.feature.dreams.screen.add.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.snow.diary.feature.dreams.R
import org.oneui.compose.base.Icon
import org.oneui.compose.base.IconView
import org.oneui.compose.theme.OneUITheme
import dev.oneuiproject.oneui.R as IconR

@Composable
fun ShowMoreTextSeparator(
    modifier: Modifier = Modifier,
    expandedText: String = stringResource(R.string.dream_add_shrink),
    collapsedText: String = stringResource(R.string.dream_add_expand),
    expanded: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clickable(
                onClick = onClick
            )
            .padding(ShowMoreSeparatorDefaults.padding),
        contentAlignment = Alignment.Center
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement
                .spacedBy(ShowMoreSeparatorDefaults.iconSpacing)
        ){
            IconView(
                icon = Icon.Resource(IconR.drawable.ic_oui_keyboard_arrow_down),
                modifier = Modifier
                    .size(18.dp)
            )
            Text(
                text = if(expanded) expandedText else collapsedText,
                style = OneUITheme.types.textSeparator
            )
        }
    }
}

private object ShowMoreSeparatorDefaults {

    val padding = PaddingValues(
        vertical = 6.dp
    )

    val iconSpacing = 2.dp

}