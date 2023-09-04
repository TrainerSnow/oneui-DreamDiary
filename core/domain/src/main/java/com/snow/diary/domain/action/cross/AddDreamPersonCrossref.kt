package com.snow.diary.domain.action.cross;

import com.snow.diary.database.cross.DreamPersonCrossref
import com.snow.diary.database.dao.CrossrefDao
import com.snow.diary.domain.action.Action

class AddDreamPersonCrossref(
    val crossrefDao: CrossrefDao
): Action<AddDreamPersonCrossref.Input, Unit>() {

    data class Input(

        val dreamId: Long,

        val personId: Long

    )

    override suspend fun Input.compose() = crossrefDao
        .addDreamPersonCrossref(
            DreamPersonCrossref(dreamId, personId)
        )

}