package com.snow.diary.domain.action.cross;

import com.snow.diary.database.cross.DreamLocationCrossref
import com.snow.diary.database.dao.CrossrefDao
import com.snow.diary.domain.action.Action

class AddDreamLocationCrossref(
    val crossrefDao: CrossrefDao
): Action<AddDreamLocationCrossref.Input, Unit>() {

    data class Input(

        val dreamId: Long,

        val locationId: Long

    )

    override suspend fun Input.compose() = crossrefDao
        .addDreamLocationCrossref(
            DreamLocationCrossref(dreamId, locationId)
        )

}