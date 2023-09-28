package com.snow.diary.core.ui.graph.line

import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.oneui.compose.util.OneUIPreview

@Composable
fun LineGraph(
    modifier: Modifier = Modifier,
    values: List<Float>,
    lineColor: Color
) {
    require(values.size >= 3) { "The values must be at least 3" }

    val points = values
        .mapIndexed { index, value -> PointF(index.toFloat(), value) }

    Canvas(
        modifier = modifier
    ) {
        val path = Path()
        val maxY = values.max()
        val maxX = values.size - 1
        fun valueToPoint(point: PointF) =
            PointF(size.width * (point.x / maxX), size.height * (1 - (point.y / maxY)))

        val firstPoint = valueToPoint(PointF(0F, values[0]))
        path.moveTo(firstPoint.x, firstPoint.y)
        val strippedPoints = points.subList(1, points.size)

        val controlPoints: List<Pair<PointF, PointF>> =
            strippedPoints.mapIndexed { index, currentPoint ->
                val recentPoint = points[index]
                val cp1 = PointF(
                    (currentPoint.x + recentPoint.x) / 2,
                    recentPoint.y
                )
                val cp2 = PointF(
                    (currentPoint.x + recentPoint.x) / 2,
                    currentPoint.y
                )

                cp1 to cp2
            }

        strippedPoints.forEachIndexed { index, _point ->
            val point = valueToPoint(_point)
            val cps = controlPoints[index]
            val cp1 = valueToPoint(cps.first)
            val cp2 = valueToPoint(cps.second)
            path
                .cubicTo(
                    cp1.x,
                    cp1.y,
                    cp2.x,
                    cp2.y,
                    point.x,
                    point.y
                )
        }

        drawPath(path, lineColor, style = Stroke(width = 5F))
    }
}

@Preview
@Composable
private fun LineGraphPreview() = OneUIPreview(title = "LineGraph") {
    LineGraph(
        modifier = Modifier
            .size(300.dp),
        values = listOf(1F, 4F, 6F, 2F, 3F, 9F, 2F, 4F),
        lineColor = Color.Red
    )
}