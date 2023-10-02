package com.snow.diary.core.domain.action.cross.dream_person

import com.snow.diary.core.database.cross.DreamPersonCrossref
import com.snow.diary.core.database.dao.CrossrefDao

class AddDreamPersonCrossref(
    val crossrefDao: CrossrefDao
): com.snow.diary.core.domain.action.Action<AddDreamPersonCrossref.Input, Unit>() {

    data class Input(

        val dreamId: Long,

        val personId: Long

    )

    override suspend fun Input.compose() = crossrefDao
        .addDreamPersonCrossref(
            DreamPersonCrossref(dreamId, personId)
        )

}