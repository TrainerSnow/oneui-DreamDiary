package com.snow.diary.feature.export.screen

import android.net.Uri
import com.snow.diary.core.io.ExportFiletype

sealed class ExportEvent {

    data class SelectFiletype(
        val filetype: ExportFiletype
    ): ExportEvent()

    data object Export: ExportEvent()

    data class FileCreated(
        val uri: Uri?
    ): ExportEvent()

}

sealed class ExportUiEvent {

    data object ReturnSuccess: ExportUiEvent()

    data object ReturnFailure: ExportUiEvent()

    data object OpenFilePicker: ExportUiEvent()

}