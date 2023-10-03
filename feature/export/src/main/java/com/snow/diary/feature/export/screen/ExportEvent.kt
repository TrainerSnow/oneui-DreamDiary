package com.snow.diary.feature.export.screen

import com.snow.diary.core.io.ExportFiletype

sealed class ExportEvent {

    data class SelectFiletype(
        val filetype: ExportFiletype
    ): ExportEvent()

    data object Export: ExportEvent()

}

sealed class ExportUiEvent {

    data object ReturnSuccess: ExportUiEvent()

    data object ReturnFailure: ExportUiEvent()

}