package com.snow.diary.core.ui.graph.line;

import android.util.Log.d
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

//TODO: This file is terribly, doesn't follow any conventions and barely, with luck, does what it needs to do. Should be refactored.
sealed interface AxisLabelFitting {

    /**
     * Calculates the positions for the labels
     *
     * @param measurer The [TextMeasurer] to use
     * @param labels All labels
     * @param textStyle The [TextStyle] to use
     * @param allowLabelsOutside Whether labels should be able to pop out of the canvas.
     * @return A list of pairs, which contain the label and the 'position' where it is to be placed. The position is a float value, where 0 is start/bottom and 1 is end/top.
     */
    fun DrawScope.resolve(
        measurer: TextMeasurer,
        labels: List<String>,
        textStyle: TextStyle,
        isXAxis: Boolean,
        allowLabelsOutside: Boolean
    ): List<Pair<Float, String>>

    data object All : AxisLabelFitting {
        override fun DrawScope.resolve(
            measurer: TextMeasurer,
            labels: List<String>,
            textStyle: TextStyle,
            isXAxis: Boolean,
            allowLabelsOutside: Boolean
        ): List<Pair<Float, String>> = labels.mapIndexed { index, label ->
            val result = measurer.measure(label, textStyle)

            val progress = index.toFloat() / (labels.size - 1F)
            val axisSize = if (isXAxis) size.width else size.height
            val itemSize = if (isXAxis) result.size.width else result.size.height
            val factor = if (allowLabelsOutside) if (isXAxis) 0.5F else -0.5F else when (index) {
                0 -> if (isXAxis) 0F else -1F
                labels.size - 1 -> if (isXAxis) 1F else 0F
                else -> if (isXAxis) 0.5F else -0.5F
            }
            val pos = (progress * axisSize) - (itemSize * factor)

            pos to label
        }
    }

    data class Fitting(
        val padding: Dp = 0.dp
    ) : AxisLabelFitting {
        override fun DrawScope.resolve(
            measurer: TextMeasurer,
            labels: List<String>,
            textStyle: TextStyle,
            isXAxis: Boolean,
            allowLabelsOutside: Boolean
        ): List<Pair<Float, String>> {
            fun Size.axisSize() = if (isXAxis) width else height
            fun IntSize.axisSize() = if (isXAxis) width else height

            val totalSpaceNeeded = labels.sumOf {
                measurer.measure(it, textStyle).size.axisSize()
            } + ((labels.size - 1) * padding.toPx())

            if (totalSpaceNeeded <= size.axisSize()) return with(All) {
                resolve(measurer, labels, textStyle, isXAxis, allowLabelsOutside)
            }

            val pairs = mutableListOf<Pair<Float, String>>()

            var result = measurer.measure(labels.first(), textStyle)
            var position =
                if (allowLabelsOutside) (if (isXAxis) -1 else 1) * (result.size.axisSize() / 2F) else if (isXAxis) 0F else result.size.height.toFloat()
            if(isXAxis)
                d("AxisFitting", """
                    Start of first = $position
                    End of first = ${position + result.size.width}
                    Padding = ${padding.toPx()}
                """.trimIndent())

            //Adding first
            pairs.add(position to labels.first())

            //Last one. Needs to be added last
            val lastResult = measurer.measure(labels.last(), textStyle)
            val lastPosition =
                size.axisSize() - (if (allowLabelsOutside) (if (isXAxis) lastResult.size.axisSize() / 2
                else lastResult.size.axisSize() / -2)
                else if (isXAxis) lastResult.size.axisSize()
                else 0)

            for (i in labels.indices) {
                if ((i == 0) or (i == labels.size - 1)) continue //Alr added first and last

                val label = labels[i]

                val recentEndWithPadding = position + result.size.width + padding.toPx()

                result = measurer.measure(label, textStyle)

                val newPos =
                    ((i.toFloat() / (labels.size - 1F)) * size.axisSize()) - (result.size.axisSize() / 2)
                if(isXAxis)
                    d("AxisFitting", """
                        Recent end = $recentEndWithPadding
                        New position = $newPos
                    """.trimIndent())


                //Checking if it collides with the last one. If it does, we don't add it
                val collidesWithLast =
                    newPos + padding.toPx() + result.size.axisSize() > lastPosition

                if ((recentEndWithPadding <= newPos) and !collidesWithLast) {
                    position = newPos
                    pairs.add(position to label)
                }
            }

            pairs.add(lastPosition to labels.last())

            return pairs
        }
    }
}