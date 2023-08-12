package com.snow.diary.ui.param;

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.snow.diary.model.data.Dream
import java.time.LocalDate

class DreamParamProvider : PreviewParameterProvider<Dream> {
    override val values: Sequence<Dream>
        get() = (1..200).map {
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
                clearness = it / 200F,
                happiness = it / 200F,
                created = LocalDate.now().minusDays(it.toLong()),
                updated = LocalDate.now().minusDays(it + 2L)
            )
        }.asSequence()
}

class DreamsParamProvider : PreviewParameterProvider<List<Dream>> {
    override val values: Sequence<List<Dream>>
        get() = listOf(
                (1..200).map {
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
                        clearness = it / 200F,
                        happiness = it / 200F,
                        created = LocalDate.now().minusDays(it.toLong()),
                        updated = LocalDate.now().minusDays(it + 2L)
                    )
                }
        ).asSequence()
}