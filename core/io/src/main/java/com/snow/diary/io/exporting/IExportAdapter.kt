package com.snow.diary.io.exporting

import com.snow.diary.io.ExportFiletype
import com.snow.diary.io.data.IOData
import com.snow.diary.io.exporting.impl.CSVExportAdapter
import com.snow.diary.io.exporting.impl.JSONExportAdapter
import java.io.OutputStream

interface IExportAdapter {

    fun export(data: IOData, os: OutputStream)

    companion object {

        fun getInstance(type: ExportFiletype): IExportAdapter = when (type) {
            ExportFiletype.CSV -> CSVExportAdapter()
            ExportFiletype.JSON -> JSONExportAdapter()
        }

    }

}