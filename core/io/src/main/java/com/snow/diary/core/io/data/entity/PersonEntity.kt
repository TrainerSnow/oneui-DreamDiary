package com.snow.diary.core.io.data.entity

import com.snow.diary.core.model.data.Person
import kotlinx.serialization.Serializable

@Serializable
data class PersonEntity(

    val id: Long? = null,

    val name: String,

    val isFavourite: Boolean,

    val notes: String?

){
    fun toModel() = Person(id, name, isFavourite, notes)

    constructor(person: Person) : this(
        person.id, person.name, person.isFavourite, person.notes
    )

}