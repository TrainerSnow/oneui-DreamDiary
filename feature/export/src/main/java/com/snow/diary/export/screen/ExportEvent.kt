package com.snow.diary.export.screen;

import com.snow.diary.io.ExportFiletype

sealed class ExportEvent {

    data class SelectFiletype(
        val filetype: ExportFiletype
    ): ExportEvent()

    data object Export: ExportEvent()

}