package com.snow.diary.core.io.exporting.impl

import com.snow.diary.core.io.data.IOData
import com.snow.diary.core.io.data.SerializableIOData
import com.snow.diary.core.io.exporting.IExportAdapter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.OutputStream

class JSONExportAdapter : IExportAdapter {
    override fun export(data: IOData, os: OutputStream) {
        val jsonData = SerializableIOData(data)

        val json = Json.encodeToString(jsonData)
        os.write(json.toByteArray())
    }

}