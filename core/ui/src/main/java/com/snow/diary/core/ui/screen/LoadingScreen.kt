package com.snow.diary.core.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.oneui.compose.progress.CircularProgressIndicatorSize
import org.oneui.compose.progress.ProgressIndicator
import org.oneui.compose.progress.ProgressIndicatorType
import org.oneui.compose.theme.OneUITheme


@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    title: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(LoadingScreenDefaults.loadingIconTextSpacing, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val style = TextStyle(
            fontSize = 12.sp
        )

        ProgressIndicator(
            type = ProgressIndicatorType.CircularIndeterminate(
                size = CircularProgressIndicatorSize.Companion.Large
            )
        )
        Text(
            text = title,
            style = style
        )
    }
}

private object LoadingScreenDefaults {

    val loadingIconTextSpacing = 16.dp

}