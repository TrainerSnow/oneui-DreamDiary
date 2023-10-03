package com.snow.diary.core.io.importing.impl

import com.snow.diary.core.io.data.IOData
import com.snow.diary.core.io.exporting.impl.JSONIOData
import com.snow.diary.core.io.importing.IImportAdapter
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStream

class JSONImportAdapter: IImportAdapter {
    override fun import(`is`: InputStream): IOData {
        val reader = BufferedReader(`is`.reader())
        val jsonString = reader.readText()
        reader.close()

        val obj = Json.decodeFromString<JSONIOData>(jsonString)
        return obj.toIOData()
    }
}

private fun JSONIOData.toIOData(): IOData = IOData(
    dreams = dreams.map { it.toModel() },
    persons = persons.map { it.toModel() },
    locations = locations.map { it.toModel() },
    relations = relations.map { it.toModel() },
    dreamPersonCrossrefs = dreamPersonCrossref,
    dreamLocationsCrossrefs = dreamLocationCrossref,
    personRelationCrossrefs = personRelationCrossrefs
)