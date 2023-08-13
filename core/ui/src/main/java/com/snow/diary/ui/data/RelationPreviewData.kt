package com.snow.diary.ui.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.snow.diary.model.data.Relation

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
    ).map {
        android.graphics.Color.valueOf(Color(it).toArgb())
    }

    val relations = (1..200).map {
        Relation(
            id = it.toLong(),
            name = "Relation $it",
            color = colors[(colors.size - 1) % it]
        )
    }

}