package com.snow.diary.core.ui.data

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.snow.diary.core.model.combine.PersonWithRelations
import com.snow.diary.core.model.data.Person

object PersonPreviewData {

    val persons = (1..200).map {
        Person(
            id = it.toLong(),
            name = "Person $it",
            isFavourite = it % 2 == 0,
            notes = if (it % 2 == 0) null else LoremIpsum(200)
                .values
                .joinToString(" ")
        )
    }

    val personsWithRelations = persons.map { person ->
        PersonWithRelations(
            person = person,
            relation = RelationPreviewData
                .relations
                .take(3)
        )
    }

}