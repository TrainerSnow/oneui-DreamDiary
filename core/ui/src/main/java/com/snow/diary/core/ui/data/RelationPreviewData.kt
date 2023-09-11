package com.snow.diary.core.ui.data

import androidx.core.graphics.toColor
import com.snow.diary.core.model.data.Relation

object RelationPreviewData {

    private val colors = listOf(
        0xff039be6,
        0xffd44245,
        0xfff27199,
        0xffeb9e5a,
        0xfffcca05,
        0xff5fc59d,
        0xff69b054,
        0xff63d1d2,
        0xff81aae8,
        0xff4d7adf,
        0xffb093e6,
        0xffa9a9a9
    )

    val relations = (1..20).map {
        Relation(
            id = it.toLong(),
            name = "Relation $it",
            color = colors[(colors.size - 1) % it].toColor().toArgb(),
            notes = "Notes $it"
        )
    }

}