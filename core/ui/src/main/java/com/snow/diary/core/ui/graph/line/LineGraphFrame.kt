package com.snow.diary.core.ui.graph.line

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LineGraphFrame(
    modifier: Modifier = Modifier,
    yLabels: List<String> = emptyList(),
    yFitting: AxisLabelFitting,
    yAxisLineConfig: AxisLineConfig? = null,
    yLabelTextStyle: TextStyle = TextStyle(),
    xLabels: List<String> = emptyList(),
    xFitting: AxisLabelFitting,
    xAxisLineConfig: AxisLineConfig? = null,
    xLabelTextStyle: TextStyle = TextStyle(),
    bottomSection: Dp = 40.dp,
    endSection: Dp = 40.dp,
    allowLabelsOutside: Boolean = false,
    graph: @Composable () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(1F)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1F)
                ) {
                    graph()
                    LineOverlay(
                        modifier = Modifier
                            .fillMaxSize(),
                        yLabels = yLabels,
                        yFitting = yFitting,
                        yAxisLineConfig = yAxisLineConfig,
                        yLabelTextStyle = yLabelTextStyle,
                        xLabels = xLabels,
                        xFitting = xFitting,
                        xAxisLineConfig = xAxisLineConfig,
                        xLabelTextStyle = xLabelTextStyle,
                        allowOutsideCanvas = allowLabelsOutside
                    )
                }

                Box(
                    modifier = Modifier
                        .height(bottomSection)
                        .fillMaxWidth()
                ) {
                    XSection(
                        modifier = Modifier
                            .fillMaxSize(),
                        labels = xLabels,
                        fitting = xFitting,
                        allowLabelsOutside = allowLabelsOutside,
                        textStyle = xLabelTextStyle
                    )
                }
            }

            Box(
                modifier = Modifier
                    .padding(
                        bottom = bottomSection
                    )
            ) {
                Box(
                    modifier = Modifier
                        .width(endSection)
                        .fillMaxHeight()
                ) {
                    YSection(
                        modifier = Modifier
                            .fillMaxSize(),
                        labels = yLabels,
                        fitting = yFitting,
                        allowLabelsOutside = allowLabelsOutside,
                        textStyle = yLabelTextStyle
                    )
                }
            }
        }
    }
}

@Composable
private fun XSection(
    modifier: Modifier = Modifier,
    labels: List<String>,
    fitting: AxisLabelFitting,
    allowLabelsOutside: Boolean,
    textStyle: TextStyle
) {
    val measurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier
            .fillMaxSize()
    ) {
        val labelPoses = with(fitting) {
            resolve(measurer, labels, textStyle, true, allowLabelsOutside)
        }
        labelPoses.forEach { labelPos ->
            drawText(
                text = labelPos.label,
                textMeasurer = measurer,
                topLeft = labelPos.rect.topLeft,
                softWrap = false
            )
        }
    }
}

@Composable
fun YSection(
    modifier: Modifier = Modifier,
    labels: List<String>,
    fitting: AxisLabelFitting,
    allowLabelsOutside: Boolean,
    textStyle: TextStyle
) {
    val measurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier
            .fillMaxSize()
    ) {
        val labelPoses = with(fitting) {
            resolve(measurer, labels, textStyle, false, allowLabelsOutside)
        }
        labelPoses.forEach { labelPos ->
            drawText(
                text = labelPos.label,
                textMeasurer = measurer,
                topLeft = labelPos.rect.topLeft
            )
        }
    }
}

@Composable
private fun LineOverlay(
    modifier: Modifier = Modifier,
    yLabels: List<String>,
    yFitting: AxisLabelFitting,
    yAxisLineConfig: AxisLineConfig? = null,
    yLabelTextStyle: TextStyle,
    xLabels: List<String>,
    xFitting: AxisLabelFitting,
    xAxisLineConfig: AxisLineConfig? = null,
    xLabelTextStyle: TextStyle,
    allowOutsideCanvas: Boolean
) {
    val measurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier
    ) {
        val yLabelPositions = with(yFitting) {
            resolve(measurer, yLabels, yLabelTextStyle, false, allowOutsideCanvas)
        }
        val xLabelPositions = with(xFitting) {
            resolve(measurer, xLabels, xLabelTextStyle, true, allowOutsideCanvas)
        }

        yAxisLineConfig?.let { config ->
            yLabelPositions.forEachIndexed { index, labelPos ->
                val lineY = when (index) {
                    0 -> size.height
                    yLabelPositions.size - 1 -> 0F
                    else -> labelPos.rect.center.y
                }
                drawLine(
                    color = config.color,
                    strokeWidth = config.strokeWidth,
                    cap = config.strokeCap,
                    pathEffect = config.pathEffect,
                    start = Offset(
                        x = size.width,
                        y = lineY
                    ),
                    end = Offset(
                        x = 0F,
                        y = lineY
                    )
                )
            }
        }

        xAxisLineConfig?.let { config ->
            xLabelPositions.forEachIndexed { index, labelPos ->
                val lineX = when (index) {
                    0 -> 0F
                    xLabelPositions.size - 1 -> size.width
                    else -> labelPos.rect.center.x
                }
                drawLine(
                    color = config.color,
                    strokeWidth = config.strokeWidth,
                    cap = config.strokeCap,
                    pathEffect = config.pathEffect,
                    start = Offset(
                        x = lineX,
                        y = size.height
                    ),
                    end = Offset(
                        x = lineX,
                        y = 0F
                    )
                )
            }
        }
    }
}

@Preview(widthDp = 900, heightDp = 500)
@Composable
private fun LineGraphFramePreview() = LineGraphFrame(
    modifier = Modifier
        .height(400.dp)
        .fillMaxWidth(),
    xLabels = (1..12).map { "1.$it." },
    yLabels = (1..12).map { "2.$it." },
    xFitting = AxisLabelFitting.All,
    yFitting = AxisLabelFitting.All,
    allowLabelsOutside = false,
    xAxisLineConfig = AxisLineConfig(
        color = Color.Red,
        strokeWidth = 1F
    ),
    yAxisLineConfig = AxisLineConfig(
        color = Color.Green,
        strokeWidth = 1F
    )
) {
}