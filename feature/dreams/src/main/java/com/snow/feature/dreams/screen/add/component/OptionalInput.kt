package com.snow.feature.dreams.screen.add.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.oneui.compose.widgets.box.RoundedCornerBox
import org.oneui.compose.widgets.buttons.Checkbox

@Composable
fun OptionalInput(
    modifier: Modifier = Modifier,
    title: String,
    expanded: Boolean = false,
    onExpandedChange: (Boolean) -> Unit,
    input: @Composable () -> Unit
) {
    val titleStyle = TextStyle(
        fontSize = 16.sp
    )

    RoundedCornerBox(
        modifier = modifier
            .animateContentSize(),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            verticalArrangement = Arrangement
                .spacedBy(OptionalInputDefaults.spacing)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = expanded,
                    onCheckedChange = onExpandedChange
                )
                Text(
                    text = title,
                    style = titleStyle
                )
            }

            if(expanded) {
                input()
            }
        }
    }
}

private object OptionalInputDefaults {

    val spacing = 8.dp

}