package com.snow.diary.core.domain.action.location

import com.snow.diary.core.database.dao.LocationDao
import com.snow.diary.core.database.model.LocationEntity
import com.snow.diary.core.model.data.Location
import com.snow.diary.core.obfuscation.db.dao.ObfuscationInfoDao

class DeleteLocation(
    private val locationDao: LocationDao,
    private val obfuscationDao: ObfuscationInfoDao
) : com.snow.diary.core.domain.action.Action<Location, Unit>() {
    override suspend fun Location.compose() {
        locationDao
            .delete(LocationEntity(this))
        obfuscationDao
            .deletePersonInfoById(id!!)
    }
}