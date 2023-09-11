package com.snow.diary.feature.export

import androidx.annotation.StringRes
import com.snow.diary.feature.export.R
import com.snow.diary.core.io.ExportFiletype

@Suppress("SameReturnValue")
val ExportFiletype.suitableForImporting: Boolean
    get() = when (this) {
        ExportFiletype.CSV -> true
        ExportFiletype.JSON -> true
    }

val ExportFiletype.info: Int
    @StringRes get() = when (this) {
        ExportFiletype.CSV -> R.string.export_filetype_csv_info
        ExportFiletype.JSON -> R.string.export_filetype_json_info
    }