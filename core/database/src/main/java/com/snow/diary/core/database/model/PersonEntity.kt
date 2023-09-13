package com.snow.diary.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.snow.diary.core.database.IModelMappable
import com.snow.diary.core.model.data.Person

@Entity(
    tableName = "person"
)
data class PersonEntity(

    @PrimaryKey(autoGenerate = true)
    val personId: Long? = null,

    val name: String,

    val isFavourite: Boolean,

    val notes: String?
): IModelMappable<Person> {

    override fun toModel() = Person(personId, name, isFavourite, notes)

    constructor(person: Person) : this(
        person.id,
        person.name,
        person.isFavourite,
        person.notes
    )

}