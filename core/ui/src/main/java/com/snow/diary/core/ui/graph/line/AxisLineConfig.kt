package com.snow.diary.core.ui.graph.line

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap

data class AxisLineConfig (

    val color: Color,

    val strokeWidth: Float = 1F,

    val pathEffect: PathEffect? = null,

    val strokeCap: StrokeCap = StrokeCap.Butt

)