package com.snow.diary.core.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
fun ErrorScreen(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val warningStyle = TextStyle(
            color = OneUITheme.colors.seslErrorColor,
            fontSize = 12.sp
        )

        IconView(
            icon = Icon.Resource(R.drawable.ic_oui_error),
            colors = iconColors(
                tint = OneUITheme.colors.seslErrorColor
            ),
            modifier = Modifier
                .size(ErrorScreenDefaults.errorIconSize),
            contentDescription = title
        )
        Spacer(
            modifier = Modifier
                .height(ErrorScreenDefaults.errorIconTextSpacing)
        )
        Text(
            text = title,
            style = warningStyle
        )
        description?.let { desc ->
            Spacer(
                modifier = Modifier
                    .height(ErrorScreenDefaults.errorTextMsgSPacing)
            )
            Text(
                text = desc,
                style = warningStyle
            )
        }
    }
}

private object ErrorScreenDefaults {

    val errorIconSize = 65.dp
    val errorIconTextSpacing = 16.dp
    val errorTextMsgSPacing = 4.dp

}