package com.snow.diary.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.oneuiproject.oneui.R
import org.oneui.compose.base.Icon
import org.oneui.compose.base.IconView
import org.oneui.compose.base.iconColors
import org.oneui.compose.theme.OneUITheme


@Composable
fun EmptyScreen(
    modifier: Modifier = Modifier,
    title: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(EmptyScreenDefaults.emptyIconTextSpacing, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val style = TextStyle(
            color = OneUITheme.colors.seslSecondaryTextColor,
            fontSize = 12.sp
        )

        IconView(
            icon = Icon.Resource(R.drawable.ic_oui_lasso_add),
            colors = iconColors(
                tint = OneUITheme.colors.seslSecondaryTextColor.copy(
                    alpha = 0.4F
                )
            ),
            modifier = Modifier
                .size(EmptyScreenDefaults.emptyIconSize),
            contentDescription = title
        )
        Text(
            text = title,
            style = style
        )
    }
}

private object EmptyScreenDefaults {

    val emptyIconSize = 65.dp
    val emptyIconTextSpacing = 16.dp

}