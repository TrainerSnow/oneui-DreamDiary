package com.snow.diary.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.snow.diary.IModelMappable
import com.snow.diary.model.data.Person

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
    val personId: Long? = null,

    val name: String,

    val isFavourite: Boolean,

    @ColumnInfo(index = true)
    val relationId: Long,

    val notes: String?
): IModelMappable<Person> {

    override fun toModel() = Person(personId, name, isFavourite, relationId, notes)

    constructor(person: Person) : this(
        person.id,
        person.name,
        person.isFavourite,
        person.relationId,
        person.notes
    )

}