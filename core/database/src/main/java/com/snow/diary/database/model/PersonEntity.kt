package com.snow.diary.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.snow.diary.model.data.Person
import java.util.UUID

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = RelationEntity::class,
            parentColumns = ["id"],
            childColumns = ["relationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    tableName = "person"
)
data class PersonEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val name: String,

    val relationId: Long,

    val notes: String?
) {

    constructor(person: Person) : this(
        person.id,
        person.name,
        person.relationId,
        person.notes
    )

}

val PersonEntity.asModel: Person
    get() = Person(id, name, relationId, notes)