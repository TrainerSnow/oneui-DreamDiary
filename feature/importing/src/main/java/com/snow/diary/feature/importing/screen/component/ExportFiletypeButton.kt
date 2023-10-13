package com.snow.diary.feature.importing.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snow.diary.core.io.ImportFiletype
import org.oneui.compose.theme.OneUITheme

@Composable
fun ImportFiletypeButton(
    modifier: Modifier = Modifier,
    filetype: ImportFiletype,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    val testStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    )

    Box(
        modifier = modifier
            .size(ExportFiletypeButtonDefaults.size)
            .clickable(
                onClick = onClick
            )
            .clip(ExportFiletypeButtonDefaults.shape)
            .background(
                color = if (selected) OneUITheme.colors.seslRippleColor else Color.Transparent,
                shape = ExportFiletypeButtonDefaults.shape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = filetype.name,
            style = testStyle
        )
    }
}

private object ExportFiletypeButtonDefaults {

    val size = DpSize(width = 32.dp, height = 32.dp)

    val shape = RoundedCornerShape(13.dp)
}