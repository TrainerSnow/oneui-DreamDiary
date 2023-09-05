package com.snow.diary.io.exporting

import com.snow.diary.io.data.IOData
import java.io.OutputStream

interface IExportAdapter {

    fun export(data: IOData, os: OutputStream)

}