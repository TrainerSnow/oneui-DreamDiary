package com.snow.diary.core.io.importing

import com.snow.diary.core.io.ImportFiletype
import com.snow.diary.core.io.data.IOData
import com.snow.diary.core.io.importing.impl.JSONImportAdapter
import java.io.InputStream

interface IImportAdapter {

    fun import(`is`: InputStream): IOData


    companion object {

        fun getInstance(type: ImportFiletype): IImportAdapter = when (type) {
            ImportFiletype.JSON -> JSONImportAdapter()
        }

    }

}