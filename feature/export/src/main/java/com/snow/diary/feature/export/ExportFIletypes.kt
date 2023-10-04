package com.snow.diary.feature.export

import androidx.annotation.StringRes
import com.snow.diary.core.io.ExportFiletype

@Suppress("SameReturnValue")
val ExportFiletype.suitableForImporting: Boolean
    get() = when (this) {
        ExportFiletype.JSON -> true
    }

val ExportFiletype.info: Int
    @StringRes get() = when (this) {
        ExportFiletype.JSON -> R.string.export_filetype_json_info
    }