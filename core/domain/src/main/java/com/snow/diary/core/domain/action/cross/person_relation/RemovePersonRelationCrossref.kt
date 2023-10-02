package com.snow.diary.core.domain.action.cross.person_relation

import com.snow.diary.core.database.cross.PersonRelationCrossref
import com.snow.diary.core.database.dao.CrossrefDao
import com.snow.diary.core.domain.action.Action

class RemovePersonRelationCrossref(
    val crossrefDao: CrossrefDao
): Action<RemovePersonRelationCrossref.Input, Unit>() {

    data class Input(

        val personId: Long,

        val relationId: Long

    )

    override suspend fun Input.compose() = crossrefDao
        .deletePersonRelationCrossref(
            PersonRelationCrossref(personId, relationId)
        )

}