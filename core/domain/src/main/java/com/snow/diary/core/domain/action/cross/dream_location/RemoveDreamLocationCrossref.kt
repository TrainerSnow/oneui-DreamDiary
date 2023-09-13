package com.snow.diary.core.domain.action.cross.dream_location;

import com.snow.diary.core.database.cross.DreamLocationCrossref
import com.snow.diary.core.database.dao.CrossrefDao

class RemoveDreamLocationCrossref(
    val crossrefDao: CrossrefDao
): com.snow.diary.core.domain.action.Action<RemoveDreamLocationCrossref.Input, Unit>() {

    data class Input(

        val dreamId: Long,

        val locationId: Long

    )

    override suspend fun Input.compose() = crossrefDao
        .deleteDreamLocationCrossref(
            DreamLocationCrossref(dreamId, locationId)
        )

}