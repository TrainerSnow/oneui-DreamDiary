package com.snow.diary.core.domain.action.cross;

import com.snow.diary.core.database.cross.DreamLocationCrossref
import com.snow.diary.core.database.dao.CrossrefDao
import com.snow.diary.core.domain.action.Action

class AddDreamLocationCrossref(
    val crossrefDao: CrossrefDao
): com.snow.diary.core.domain.action.Action<AddDreamLocationCrossref.Input, Unit>() {

    data class Input(

        val dreamId: Long,

        val locationId: Long

    )

    override suspend fun Input.compose() = crossrefDao
        .addDreamLocationCrossref(
            DreamLocationCrossref(dreamId, locationId)
        )

}