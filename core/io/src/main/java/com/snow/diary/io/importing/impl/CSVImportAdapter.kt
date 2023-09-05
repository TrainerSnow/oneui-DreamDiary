package com.snow.diary.io.importing.impl;

import android.graphics.Color
import android.graphics.PointF
import com.snow.diary.csv.Row
import com.snow.diary.csv.config.csvConfig
import com.snow.diary.csv.reader.CSVReader
import com.snow.diary.io.data.Crossref
import com.snow.diary.io.data.IOData
import com.snow.diary.io.importing.IImportAdapter
import com.snow.diary.model.data.Dream
import com.snow.diary.model.data.Location
import com.snow.diary.model.data.Person
import com.snow.diary.model.data.Relation
import java.io.InputStream
import java.time.LocalDate

class CSVImportAdapter : IImportAdapter {

    private lateinit var csvReader: CSVReader

    override fun import(`is`: InputStream): IOData {
        val config = csvConfig {
            encodeBase64 = true
        }
        csvReader = CSVReader(`is`, config)

        val rows = csvReader
            .readRows()
            .splitByEmpty()

        val typeNum = 6
        require(rows.size == typeNum)

        return IOData(
            dreams = rows[0]
                .map(Row::toDream),
            persons = rows[1]
                .map(Row::toPerson),
            locations = rows[2]
                .map(Row::toLocation),
            relations = rows[3]
                .map(Row::toRelation),
            dreamPersonCrossrefs = rows[4]
                .map(Row::toCrossref),
            dreamLocationsCrossrefs = rows[5]
                .map(Row::toCrossref)
        )
    }

}

private fun Row.toDream(): Dream = Dream(
    id = this[0]!!.toLong(),
    description = this[1] ?: "",
    note = this[2],
    isFavourite = this[3]?.toBoolean() ?: false,
    created = LocalDate.parse(this[4]),
    updated = LocalDate.parse(this[5]),
    clearness = this[6]?.toFloat(),
    happiness = this[7]?.toFloat()
)

private fun Row.toPerson(): Person = Person(
    id = this[0]!!.toLong(),
    name = this[1] ?: "",
    isFavourite = this[2]?.toBoolean() ?: false,
    relationId = this[3]!!.toLong(),
    notes = this[4]
)

private fun Row.toLocation(): Location = Location(
    id = this[0]!!.toLong(),
    name = this[1] ?: "",
    coordinates = this[2]?.let {
        val arr = it.split("|")
        if (arr.size != 2) null
        else PointF(arr[0].toFloat(), arr[1].toFloat())
    } ?: PointF(0F, 0F),
    notes = this[3] ?: ""
)

private fun Row.toRelation(): Relation = Relation(
    id = this[0]!!.toLong(),
    name = this[1] ?: "",
    color = this[2]?.let { Color.valueOf(it.toInt()) } ?: Color.valueOf(Color.TRANSPARENT)
)

private fun Row.toCrossref(): Crossref = Pair(
    first = this[0]!!.toInt(),
    second = this[1]!!.toInt()
)

private fun List<List<String?>>.splitByEmpty(): List<List<List<String?>>> {
    val emptyIndices = mutableListOf<Int>()

    forEachIndexed { index, strings ->
        if (strings.isEmpty()) emptyIndices.add(index)
    }

    val rList = mutableListOf<List<List<String?>>>()

    var recentEmptyIndex = -1
    emptyIndices.forEach { emptyIndex ->
        rList.add(
            subList(
                recentEmptyIndex + 1,
                emptyIndex
            )
        )
        recentEmptyIndex = emptyIndex
    }

    return rList
}