package com.snow.diary.core.domain.pure

import com.snow.diary.core.common.sortedDirectional
import com.snow.diary.core.model.data.Dream
import com.snow.diary.core.model.data.Location
import com.snow.diary.core.model.data.Person
import com.snow.diary.core.model.data.Relation
import com.snow.diary.core.model.sort.SortConfig
import com.snow.diary.core.model.sort.SortMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@JvmName("sortWithFlowDream")
fun Flow<List<Dream>>.sortWith(sortConfig: SortConfig) = map { it.sortWith(sortConfig) }

@JvmName("sortWithDream")
fun List<Dream>.sortWith(
    sortConfig: SortConfig
): List<Dream> = when (sortConfig.mode) {
    SortMode.Created -> sortedBy { it.created }
    SortMode.Updated -> sortedBy { it.updated }
    SortMode.Happiness -> sortedBy { it.happiness ?: 0F }
    SortMode.Clearness -> sortedBy { it.clearness ?: 0F }
    SortMode.Length -> sortedBy { it.description.length }
    else -> this
}
    .sortedDirectional(sortConfig.direction)


@JvmName("sortWithFlowPerson")
fun Flow<List<Person>>.sortWith(sortConfig: SortConfig) = map { it.sortWith(sortConfig) }

@JvmName("sortWithPerson")
fun List<Person>.sortWith(
    sortConfig: SortConfig
): List<Person> = when (sortConfig.mode) {
    SortMode.Alphabetically -> sortedByDescending { it.name }
    SortMode.Length -> sortedBy { it.notes?.length ?: 0 }
    else -> this
}
    .sortedDirectional(sortConfig.direction)


@JvmName("sortWithFlowLocation")
fun Flow<List<Location>>.sortWith(sortConfig: SortConfig) = map { it.sortWith(sortConfig) }

@JvmName("sortWithLocation")
fun List<Location>.sortWith(
    sortConfig: SortConfig
): List<Location> = when (sortConfig.mode) {
    SortMode.Alphabetically -> sortedByDescending { it.name }
    SortMode.Length -> sortedBy { it.notes?.length ?: 0 }
    else -> this
}
    .sortedDirectional(sortConfig.direction)


@JvmName("sortWithFlowRelation")
fun Flow<List<Relation>>.sortWith(sortConfig: SortConfig) = map { it.sortWith(sortConfig) }

@JvmName("sortWithFlowRelation")
fun List<Relation>.sortWith(
    sortConfig: SortConfig
): List<Relation> = when (sortConfig.mode) {
    SortMode.Alphabetically -> sortedByDescending { it.name }
    SortMode.Length -> sortedBy { it.name.length }
    else -> this
}
    .sortedDirectional(sortConfig.direction)