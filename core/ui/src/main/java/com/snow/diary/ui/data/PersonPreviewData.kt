package com.snow.diary.ui.data

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.snow.diary.model.data.Person

object PersonPreviewData {

    val persons = (1..200).map {
        Person(
            id = it.toLong(),
            name = "Person $it",
            relationId = it.toLong(),
            notes = if(it % 2 == 0) null else LoremIpsum(200)
                .values
                .joinToString(" ")
        )
    }

}