package com.snow.diary.feature.importing.screen;

import android.net.Uri

internal sealed class ImportEvent {

    data class SelectFile(
        val uri: Uri
    ): ImportEvent()

    data object OpenFilePicker: ImportEvent()

    data object Return: ImportEvent()

}