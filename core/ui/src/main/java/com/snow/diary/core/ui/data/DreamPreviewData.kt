package com.snow.diary.core.ui.data;

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.snow.diary.core.model.data.Dream
import java.time.LocalDate

object DreamPreviewData {

    val dreams = (1..200).map {
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
            isFavourite = it % 2 == 0,
            clearness = it / 200F,
            happiness = it / 200F,
            created = LocalDate.now().minusDays(it.toLong()),
            updated = LocalDate.now().minusDays(it + 2L)
        )
    }

}