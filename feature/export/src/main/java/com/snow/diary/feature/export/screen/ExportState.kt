package com.snow.diary.feature.export.screen

import com.snow.diary.core.io.ExportFiletype


data class ExportState(

    val selectedFiletype: ExportFiletype = ExportFiletype.JSON,

    val isExporting: Boolean = false

)