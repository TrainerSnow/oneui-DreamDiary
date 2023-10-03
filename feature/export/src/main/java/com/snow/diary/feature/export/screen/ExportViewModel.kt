package com.snow.diary.feature.export.screen

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.anggrayudi.storage.file.DocumentFileCompat
import com.anggrayudi.storage.media.FileDescription
import com.snow.diary.core.common.launchInBackground
import com.snow.diary.core.common.time.TimeFormat.formatFullDescription
import com.snow.diary.core.domain.action.io.GetIOData
import com.snow.diary.core.domain.viewmodel.EventViewModel
import com.snow.diary.core.io.ExportFiletype
import com.snow.diary.core.io.exporting.IExportAdapter
import com.snow.diary.feature.export.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
internal class ExportViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    val getIOData: GetIOData
) : EventViewModel<ExportEvent>() {

    private val _state = MutableStateFlow(ExportState())
    val state: StateFlow<ExportState> = _state

    override suspend fun handleEvent(event: ExportEvent) = when (event) {
        ExportEvent.Export -> handleExport()
        is ExportEvent.SelectFiletype -> handleFiletypeChange(event.filetype)
    }

    private fun handleExport() = viewModelScope.launchInBackground {
        val data = getIOData(Unit).stateIn(viewModelScope).value
        val now = LocalDate.now()
        val type = state.value.selectedFiletype

        val desc = FileDescription(
            context.resources.getString(R.string.export_file_name, now.formatFullDescription()),
            subFolder = "",
            mimeType = type.mimeType
        )
        val file = DocumentFileCompat.createDownloadWithMediaStoreFallback(context, desc)
            ?: return@launchInBackground
        val uri = file.uri
        val os = context.contentResolver.openOutputStream(uri) ?: return@launchInBackground
        IExportAdapter.getInstance(type)
            .export(data, os)
        os.close()
    }

    private fun handleFiletypeChange(type: ExportFiletype) = viewModelScope.launch {
        _state.emit(
            state.value.copy(
                selectedFiletype = type
            )
        )
    }

}