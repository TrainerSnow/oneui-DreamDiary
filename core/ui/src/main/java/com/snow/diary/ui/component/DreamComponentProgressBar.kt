package com.snow.diary.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.oneui.compose.base.Icon
import org.oneui.compose.base.IconView
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.widgets.box.RoundedCornerBox
import dev.oneuiproject.oneui.R as IconR

@Composable
fun DreamComponentProgressBar(
    modifier: Modifier = Modifier,
    color: Color,
    icon: Icon,
    progress: Float,
    onProgressChange: ((Float) -> Unit)? = null
) {
    val trackColor = OneUITheme.colors.seslSwitchTrackOffColor

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement
            .spacedBy(DreamComponentProgressBarDefaults.spacing)
    ) {
        IconView(
            icon = icon,
            modifier = Modifier
                .size(DreamComponentProgressBarDefaults.iconSize)
        )
        Canvas(
            modifier = Modifier
                .height(DreamComponentProgressBarDefaults.trackHeight)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    if (onProgressChange != null) {
                        detectTapGestures { pos ->
                            val newValue = pos.x / size.width
                            onProgressChange(newValue)
                        }
                    }
                }
                .pointerInput(Unit) {
                    if (onProgressChange != null) {
                        detectDragGestures { change, _ ->
                            val newValue = change.position.x / size.width
                            onProgressChange(newValue.coerceIn(0F, 1F))
                            change.consume()
                        }
                    }
                }
        ) {
            track(
                color = trackColor,
                height = DreamComponentProgressBarDefaults.trackHeight
            )
            progress(
                color = color,
                height = DreamComponentProgressBarDefaults.trackHeight,
                progress = progress
            )
        }
    }
}

@Composable
fun ClearnessProgressBar(
    modifier: Modifier = Modifier,
    clearness: Float,
    onClearnessChange: ((Float) -> Unit)? = null
) {
    DreamComponentProgressBar(
        modifier = modifier,
        color = Color(0xff63d1d2),
        icon = Icon.Resource(IconR.drawable.ic_oui_receiving_message_from_keywords),
        progress = clearness,
        onProgressChange = onClearnessChange
    )
}

@Composable
fun HappinessProgressBar(
    modifier: Modifier = Modifier,
    happiness: Float,
    onHappinessChange: ((Float) -> Unit)? = null
) {
    DreamComponentProgressBar(
        modifier = modifier,
        color = Color(0xfffcca05),
        icon = Icon.Resource(IconR.drawable.ic_oui_emoji_2),
        progress = happiness,
        onProgressChange = onHappinessChange
    )
}

private fun DrawScope.track(
    color: Color,
    height: Dp
) {
    val strokeWidth = height.toPx()
    val maxWidth = size.width - (strokeWidth / 2)


    val start = Offset(
        x = strokeWidth / 2,
        y = size.height / 2
    )
    val end = Offset(
        x = maxWidth,
        y = size.height / 2
    )

    drawLine(
        start = start,
        end = end,
        strokeWidth = strokeWidth,
        color = color,
        cap = StrokeCap.Round
    )
}

private fun DrawScope.progress(
    color: Color,
    height: Dp,
    progress: Float
) {
    val strokeWidth = height.toPx()
    val totalWidth = size.width - strokeWidth

    val start = Offset(
        x = strokeWidth / 2,
        y = size.height / 2
    )
    val end = Offset(
        x = totalWidth * progress + (strokeWidth / 2),
        y = size.height / 2
    )

    drawLine(
        start = start,
        end = end,
        strokeWidth = strokeWidth,
        color = color,
        cap = StrokeCap.Round
    )
}

private object DreamComponentProgressBarDefaults {

    val trackHeight = 16.dp

    val iconSize = 22.dp

    val spacing = 10.dp

}

@Preview
@Composable
fun DreamComponentProgressBarPreview() = RoundedCornerBox {
    DreamComponentProgressBar(
        color = Color(0xffeb9e5a),
        icon = Icon.Resource(IconR.drawable.ic_oui_receiving_message_from_keywords),
        progress = 0.2F
    )
}