package com.snow.diary.ui.param;

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.snow.diary.model.data.Dream
import java.time.LocalDate

class DreamParamProvider: PreviewParameterProvider<Dream> {
    override val values: Sequence<Dream>
        get() = (1..10).map {
            Dream(
                id = it.toLong(),
                description = LoremIpsum()
                    .values
                    .take(100)
                    .joinToString(),
                note = LoremIpsum()
                    .values
                    .take(100)
                    .joinToString(),
                clearness = it / 10F,
                happiness = it / 10F,
                created = LocalDate.now().minusWeeks(it.toLong()),
                updated = LocalDate.now().minusWeeks(it + 2L)
            )
        }.asSequence()
}