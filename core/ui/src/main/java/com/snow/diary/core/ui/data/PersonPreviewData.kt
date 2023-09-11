package com.snow.diary.core.ui.data

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.snow.diary.core.model.combine.PersonWithRelation
import com.snow.diary.core.model.data.Person

object PersonPreviewData {

    val persons = (1..200).map {
        Person(
            id = it.toLong(),
            name = "Person $it",
            isFavourite = it % 2 == 0,
            relationId = it.toLong(),
            notes = if (it % 2 == 0) null else LoremIpsum(200)
                .values
                .joinToString(" ")
        )
    }

    val personsWithRelation = persons.map { person ->
        PersonWithRelation(
            person = person,
            relation = RelationPreviewData.relations.random()
        )
    }

}