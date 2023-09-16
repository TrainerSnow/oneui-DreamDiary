package com.snow.diary.core.domain.action.dream;

import com.snow.diary.core.database.dao.DreamDao
import com.snow.diary.core.database.model.DreamEntity
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.obfuscation.db.dao.ObfuscationInfoDao

class DeleteDream(
    val dreamDao: DreamDao,
    val obfuscationDao: ObfuscationInfoDao
): com.snow.diary.core.domain.action.Action<Dream, Unit>() {
    override suspend fun Dream.compose() {
        dreamDao
            .delete(
                DreamEntity(this)
            )
        obfuscationDao
            .deleteDreamInfoById(id!!)
    }

}