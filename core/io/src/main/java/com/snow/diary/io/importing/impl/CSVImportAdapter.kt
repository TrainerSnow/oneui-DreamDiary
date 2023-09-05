package com.snow.diary.io.importing.impl;

import android.graphics.Color
import android.graphics.PointF
import com.snow.diary.csv.Row
import com.snow.diary.csv.config.csvConfig
import com.snow.diary.csv.reader.CSVReader
import com.snow.diary.io.data.Crossref
import com.snow.diary.io.data.IOData
import com.snow.diary.io.exporting.impl.TYPE_SEPARATOR
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
        val splitRows = rows.splitByTypeSeparator()

        val typeNum = 6
        require(splitRows.size == typeNum)

        return IOData(
            dreams = splitRows[0]
                .map(Row::toDream),
            persons = splitRows[1]
                .map(Row::toPerson),
            locations = splitRows[2]
                .map(Row::toLocation),
            relations = splitRows[3]
                .map(Row::toRelation),
            dreamPersonCrossrefs = splitRows[4]
                .map(Row::toCrossref),
            dreamLocationsCrossrefs = splitRows[5]
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

private fun List<List<String?>>.splitByTypeSeparator(): List<List<List<String?>>> {
    val separatorIndexes = mutableListOf<Int>()

    forEachIndexed { index, strings ->
        if (strings.size == 1) {
            if (strings.first() == TYPE_SEPARATOR) {
                separatorIndexes.add(index)
            }
        }
    }

    val rList = mutableListOf<List<List<String?>>>()

    var recentEmptyIndex = -1
    separatorIndexes.forEach { emptyIndex ->
        rList.add(
            subList(
                recentEmptyIndex + 1,
                emptyIndex
            )
        )
        recentEmptyIndex = emptyIndex
    }
    rList.add(
        subList(
            recentEmptyIndex + 1,
            size
        )
    )

    return rList
}