package com.snow.diary.domain.action.cross;

import com.snow.diary.database.cross.DreamLocationCrossref
import com.snow.diary.database.dao.CrossrefDao
import com.snow.diary.domain.action.Action

class RemoveDreamLocationCrossref(
    val crossrefDao: CrossrefDao
): Action<RemoveDreamLocationCrossref.Input, Unit>() {

    data class Input(

        val dreamId: Long,

        val locationId: Long

    )

    override suspend fun Input.compose() = crossrefDao
        .deleteDreamLocationCrossref(
            DreamLocationCrossref(dreamId, locationId)
        )

}