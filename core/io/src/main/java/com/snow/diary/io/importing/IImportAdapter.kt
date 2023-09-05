package com.snow.diary.io.importing

import com.snow.diary.io.data.IOData
import java.io.InputStream

interface IImportAdapter {

    fun import(`is`: InputStream): IOData

}