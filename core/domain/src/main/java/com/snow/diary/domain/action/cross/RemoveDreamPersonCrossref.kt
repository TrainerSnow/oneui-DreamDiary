package com.snow.diary.domain.action.cross;

import com.snow.diary.database.cross.DreamPersonCrossref
import com.snow.diary.database.dao.CrossrefDao
import com.snow.diary.domain.action.Action

class RemoveDreamPersonCrossref(
    val crossrefDao: CrossrefDao
): Action<RemoveDreamPersonCrossref.Input, Unit>() {

    data class Input(

        val dreamId: Long,

        val personId: Long

    )

    override suspend fun Input.compose() = crossrefDao
        .deleteDreamPersonCrossref(
            DreamPersonCrossref(dreamId, personId)
        )

}