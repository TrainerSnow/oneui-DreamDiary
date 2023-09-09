package com.snow.diary.export.screen

import com.snow.diary.io.ExportFiletype


data class ExportState(

    val selectedFiletype: ExportFiletype = ExportFiletype.JSON,

    val isExporting: Boolean = false

)