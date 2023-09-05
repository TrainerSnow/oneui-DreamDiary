package com.snow.diary.io.importing

import com.snow.diary.io.ImportFiletype
import com.snow.diary.io.data.IOData
import com.snow.diary.io.importing.impl.CSVImportAdapter
import java.io.InputStream

interface IImportAdapter {

    fun import(`is`: InputStream): IOData


    companion object {

        fun getInstance(type: ImportFiletype): IImportAdapter = when (type) {
            ImportFiletype.CSV -> CSVImportAdapter()
        }

    }

}