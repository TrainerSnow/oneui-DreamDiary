package com.snow.diary.core.database.model.cross

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.snow.diary.core.database.IModelMappable
import com.snow.diary.core.database.cross.PersonRelationCrossref
import com.snow.diary.core.database.model.PersonEntity
import com.snow.diary.core.database.model.RelationEntity
import com.snow.diary.core.model.combine.PersonWithRelations
import com.snow.diary.core.model.combine.RelationWithPersons

data class PersonWithRelations(

    @Embedded val person: PersonEntity,

    @Relation(
        parentColumn = "personId",
        entityColumn = "relationId",
        associateBy = Junction(PersonRelationCrossref::class)
    )
    val relations: List<RelationEntity>

) : IModelMappable<PersonWithRelations> {
    override fun toModel() =
        PersonWithRelations(person.toModel(), relations.map(RelationEntity::toModel))
}

data class RelationWithPersons(

    @Embedded val relation: RelationEntity,

    @Relation(
        parentColumn = "relationId",
        entityColumn = "personId",
        associateBy = Junction(PersonRelationCrossref::class)
    )
    val persons: List<PersonEntity>

): IModelMappable<RelationWithPersons> {
    override fun toModel() = RelationWithPersons(relation.toModel(), persons.map(PersonEntity::toModel))
}