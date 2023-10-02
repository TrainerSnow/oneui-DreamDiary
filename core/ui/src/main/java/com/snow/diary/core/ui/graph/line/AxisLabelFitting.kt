package com.snow.diary.core.ui.graph.line

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

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
    ): List<PositionedLabel>


    data object All : AxisLabelFitting {
        override fun DrawScope.resolve(
            measurer: TextMeasurer,
            labels: List<String>,
            textStyle: TextStyle,
            isXAxis: Boolean,
            allowLabelsOutside: Boolean
        ): List<PositionedLabel> {
            val labelposes = labels.map {
                PositionedLabel.fromLabel(
                    labels.indexOf(it),
                    labels.size,
                    IntSize(
                        width = size.width.toInt(),
                        height = size.height.toInt()
                    ),
                    label = it,
                    isXAxis,
                    measurer,
                    textStyle,
                    allowLabelsOutside
                )
            }

            return labelposes
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
        ): List<PositionedLabel> {
            return PositionedLabel
                .create(
                    labels,
                    textStyle,
                    measurer,
                    isXAxis,
                    IntSize(
                        width = size.width.toInt(),
                        height = size.height.toInt()
                    ),
                    allowLabelsOutside,
                    padding.toPx()
                )
        }
    }
}

data class PositionedLabel(

    val label: String,

    val rect: Rect,

    val index: Int

) {

    companion object {

        fun create(
            labels: List<String>,
            labelStyle: TextStyle,
            measurer: TextMeasurer,
            isXAxis: Boolean,
            size: IntSize,
            allowPlaceOutside: Boolean,
            padding: Float
        ): List<PositionedLabel> {

            val positionedLabels = mutableListOf<PositionedLabel>()

            val firstPosLabel = fromLabel(
                index = 0,
                ofSize = labels.size,
                size = size,
                label = labels.first(),
                measurer = measurer,
                isXAxis = isXAxis,
                textStyle = labelStyle,
                allowPlaceOutside = allowPlaceOutside
            )
            val lastPosLabel = fromLabel(
                index = labels.size - 1,
                ofSize = labels.size,
                size = size,
                label = labels.last(),
                measurer = measurer,
                isXAxis = isXAxis,
                textStyle = labelStyle,
                allowPlaceOutside = allowPlaceOutside
            )

            positionedLabels.add(firstPosLabel)
            positionedLabels.add(lastPosLabel)

            var toAddLabels = getInBetweenLabels(
                positionedLabels,
                labels,
                labelStyle,
                padding,
                isXAxis,
                measurer,
                allowPlaceOutside,
                size
            )
            while (toAddLabels.isNotEmpty()) {
                toAddLabels.forEach { label ->
                    val toInsertIndex = positionedLabels
                        .indexOfFirst { it.index > label.index }
                    positionedLabels.add(toInsertIndex, label)
                }
                toAddLabels = getInBetweenLabels(
                    positionedLabels,
                    labels,
                    labelStyle,
                    padding,
                    isXAxis,
                    measurer,
                    allowPlaceOutside,
                    size
                )
            }

            return positionedLabels
        }

        private fun getInBetweenLabels(
            labels: List<PositionedLabel>,
            allLabels: List<String>,
            textStyle: TextStyle,
            padding: Float,
            isXAxis: Boolean,
            measurer: TextMeasurer,
            allowPlaceOutside: Boolean,
            size: IntSize,
        ): List<PositionedLabel> {
            val rLabels = mutableListOf<PositionedLabel>()

            labels.subList(0, labels.size - 1).forEachIndexed { index, currentLabel ->
                val nextLabel = labels[index + 1]

                val inBetween = getInBetweenLabel(
                    label1 = currentLabel,
                    label2 = nextLabel,
                    allLabels = allLabels,
                    measurer = measurer,
                    textStyle = textStyle,
                    isXAxis = isXAxis,
                    allowPlaceOutside = allowPlaceOutside,
                    size = size,
                    padding = padding
                )

                if (inBetween != null) rLabels.add(inBetween)
            }

            return rLabels
        }

        private fun getInBetweenLabel(
            label1: PositionedLabel,
            label2: PositionedLabel,
            allLabels: List<String>,
            measurer: TextMeasurer,
            textStyle: TextStyle,
            isXAxis: Boolean,
            allowPlaceOutside: Boolean,
            size: IntSize,
            padding: Float
        ): PositionedLabel? {
            val label1Index = allLabels.indexOf(label1.label)
            val label2Index = allLabels.indexOf(label2.label)

            if (label2Index == label1Index + 1 || label2Index < label1Index) return null

            val dif = label2Index - label1Index
            val halfDif = dif / 2
            val middleIndex = label1Index + halfDif
            val middleLabel = allLabels[middleIndex]

            val created = fromLabel(
                index = middleIndex,
                ofSize = allLabels.size,
                size = size,
                label = middleLabel,
                isXAxis = isXAxis,
                measurer = measurer,
                textStyle = textStyle,
                allowPlaceOutside = allowPlaceOutside
            )

            val createdRectWithPadding = Rect(
                left = created.rect.left - padding,
                top = created.rect.top - padding,
                right = created.rect.right + padding,
                bottom = created.rect.bottom + padding
            )

            val left1 = label1.rect.left
            val right1 = label1.rect.right
            val left2 = label2.rect.left
            val right2 = label2.rect.right
            val leftp = createdRectWithPadding.left
            val rightp = createdRectWithPadding.right
            val bottom1 = label1.rect.bottom
            val top1 = label1.rect.top
            val bottom2 = label2.rect.bottom
            val top2 = label2.rect.top
            val bottomp = createdRectWithPadding.bottom
            val topp = createdRectWithPadding.top

            val o1 = if (isXAxis) {
                (left1 in (leftp..right1)) or (right1 in (leftp..rightp))
            } else {
                (bottom1 in (topp..bottomp)) or (top1 in (topp..bottomp))
            }
            val o2 = if (isXAxis) {
                (left2 in (leftp..rightp)) or (right2 in (leftp..rightp))
            } else {
                (bottom2 in (topp..bottomp)) or (top2 in (topp..bottomp))
            }

            val overlaps = o1 or o2
            return if (overlaps) null
            else created
        }

        fun fromLabel(
            index: Int,
            ofSize: Int,
            size: IntSize,
            label: String,
            isXAxis: Boolean,
            measurer: TextMeasurer,
            textStyle: TextStyle,
            allowPlaceOutside: Boolean
        ): PositionedLabel {
            val result = measurer.measure(label, textStyle)
            result.size.toString()

            val mainAxisPos = when (index) {
                0 -> {
                    if (allowPlaceOutside)
                        if (isXAxis) -(result.size.width / 2)
                        else size.height - (result.size.height / 2)
                    else
                        if (isXAxis) 0
                        else size.height - result.size.height
                }

                ofSize - 1 -> {
                    if (allowPlaceOutside)
                        if (isXAxis) size.width - (result.size.width / 2)
                        else -(result.size.height / 2)
                    else
                        if (isXAxis) size.width - result.size.width
                        else 0
                }

                else -> {
                    val progress = index.toFloat() / (ofSize - 1F)
                    if (isXAxis) (size.width * progress).toInt() - (result.size.width / 2)
                    else size.height - (size.height * progress).toInt() - (result.size.height / 2)
                }
            }

            val crossAxisPos = if (isXAxis) (size.height / 2) - (result.size.height / 2)
            else (size.width / 2) - (result.size.width / 2)

            val rect = Rect(
                left = if (isXAxis) mainAxisPos.toFloat()
                else crossAxisPos.toFloat(),
                top = if (isXAxis) crossAxisPos.toFloat()
                else mainAxisPos.toFloat(),
                right = if (isXAxis) mainAxisPos + result.size.width.toFloat()
                else crossAxisPos + result.size.width.toFloat(),
                bottom = if (isXAxis) crossAxisPos - result.size.height.toFloat()
                else mainAxisPos + result.size.height.toFloat()
            )

            return PositionedLabel(label, rect, index)
        }

    }

}