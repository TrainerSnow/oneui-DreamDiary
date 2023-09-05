package com.snow.diary.io.exporting.impl;

import com.snow.diary.csv.Row
import com.snow.diary.csv.config.csvConfig
import com.snow.diary.csv.writer.CSVWriter
import com.snow.diary.io.data.Crossref
import com.snow.diary.io.data.IOData
import com.snow.diary.io.exporting.IExportAdapter
import com.snow.diary.model.data.Dream
import com.snow.diary.model.data.Location
import com.snow.diary.model.data.Person
import com.snow.diary.model.data.Relation
import java.io.OutputStream

internal const val TYPE_SEPARATOR = "+"

class CSVExportAdapter : IExportAdapter {

    private lateinit var csvWriter: CSVWriter

    override fun export(data: IOData, os: OutputStream) {
        val config = csvConfig {
            encodeBase64 = true
        }

        csvWriter = CSVWriter(os, config)

        csvWriter.writeAndCommit {
            write(data.dreams.map(Dream::toRow))
            write(typeSeparatorRow)
            write(data.persons.map(Person::toRow))
            write(typeSeparatorRow)
            write(data.locations.map(Location::toRow))
            write(typeSeparatorRow)
            write(data.relations.map(Relation::toRow))
            write(typeSeparatorRow)
            write(data.dreamPersonCrossrefs.map(Crossref::toRow))
            write(typeSeparatorRow)
            write(data.dreamLocationsCrossrefs.map(Crossref::toRow))
        }
    }

}

private val typeSeparatorRow: Row = listOf(TYPE_SEPARATOR)

private fun Dream.toRow(): Row = listOf(
    id?.toString(),
    description,
    note,
    isFavourite.toString(),
    created.toString(),
    updated.toString(),
    clearness?.toString(),
    happiness?.toString()
)

private fun Person.toRow(): Row = listOf(
    id?.toString(),
    name,
    isFavourite.toString(),
    relationId.toString(),
    notes
)

private fun Location.toRow(): Row = listOf(
    id?.toString(),
    name,
    "${coordinates.x}|${coordinates.y}",
    notes
)

private fun Relation.toRow(): Row = listOf(
    id?.toString(),
    name,
    color.toArgb().toString()
)

private fun Crossref.toRow(): Row = listOf(
    first.toString(),
    second.toString()
)
