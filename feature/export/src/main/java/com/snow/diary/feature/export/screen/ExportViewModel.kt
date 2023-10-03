package com.snow.diary.feature.export.screen

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.snow.diary.core.common.launchInBackground
import com.snow.diary.core.domain.action.io.GetIOData
import com.snow.diary.core.domain.viewmodel.EventViewModel
import com.snow.diary.core.io.ExportFiletype
import com.snow.diary.core.io.exporting.IExportAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
internal class ExportViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    val getIOData: GetIOData
) : EventViewModel<ExportEvent>() {

    private val _state = MutableStateFlow(ExportState())
    val state: StateFlow<ExportState> = _state

    private val _uiEvent = MutableSharedFlow<ExportUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    override suspend fun handleEvent(event: ExportEvent) = when (event) {
        ExportEvent.Export -> _uiEvent.emit(ExportUiEvent.OpenFilePicker)
        is ExportEvent.SelectFiletype -> handleFiletypeChange(event.filetype)
        is ExportEvent.FileCreated -> handleExport(event.uri)
    }

    private fun handleExport(uri: Uri?) = viewModelScope.launchInBackground {
        if (uri == null) return@launchInBackground

        val data = getIOData(Unit).stateIn(viewModelScope).value
        val type = state.value.selectedFiletype

        val os = context.contentResolver.openOutputStream(uri)
        if (os == null) {
            _uiEvent.emit(ExportUiEvent.ReturnFailure)
            return@launchInBackground
        }

        IExportAdapter.getInstance(type)
            .export(data, os)
        os.close()
        _uiEvent.emit(ExportUiEvent.ReturnSuccess)
    }

    private fun handleFiletypeChange(type: ExportFiletype) = viewModelScope.launch {
        _state.emit(
            state.value.copy(
                selectedFiletype = type
            )
        )
    }

}