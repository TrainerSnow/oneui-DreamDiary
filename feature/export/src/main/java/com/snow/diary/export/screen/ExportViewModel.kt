package com.snow.diary.export.screen;

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import androidx.lifecycle.viewModelScope
import com.snow.diary.domain.action.cross.AllDreamLocationCrossrefs
import com.snow.diary.domain.action.cross.AllDreamPersonCrossrefs
import com.snow.diary.domain.action.dream.AllDreams
import com.snow.diary.domain.action.location.AllLocations
import com.snow.diary.domain.action.person.AllPersons
import com.snow.diary.domain.action.relation.AllRelations
import com.snow.diary.domain.viewmodel.EventViewModel
import com.snow.diary.export.R
import com.snow.diary.io.ExportFiletype
import com.snow.diary.io.data.IOData
import com.snow.diary.io.exporting.IExportAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
internal class ExportViewModel @Inject constructor(
    @SuppressLint("StaticFieldLeak")
    @ApplicationContext val context: Context,
    val allDreams: AllDreams,
    val allPersons: AllPersons,
    val allLocations: AllLocations,
    val allRelations: AllRelations,
    val allDreamLocationCrossrefs: AllDreamLocationCrossrefs,
    val allDreamPersonCrossrefs: AllDreamPersonCrossrefs
) : EventViewModel<ExportEvent>() {

    private val _state = MutableStateFlow(ExportState())
    val state: StateFlow<ExportState> = _state

    @Suppress("IMPLICIT_CAST_TO_ANY")
    override suspend fun handleEvent(event: ExportEvent) = when (event) {
        ExportEvent.Export -> handleExport()
        is ExportEvent.SelectFiletype -> handleFiletypeChange(event.filetype)
    }

    private suspend fun handleExport() {
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileName = context.getString(R.string.export_file_name, LocalDateTime.now().toString())

        val file = File(dir, fileName)
        val os = file.outputStream()
        val data = getData()
        val exporter = IExportAdapter.getInstance(state.value.selectedFiletype)
        exporter.export(data, os)
    }

    private suspend fun getData(): IOData = withContext(Dispatchers.IO) {
        val dreams = async { allDreams(AllDreams.Input()).stateIn(this).value }
        val persons = async { allPersons(AllPersons.Input()).stateIn(this).value }
        val locations = async { allLocations(AllLocations.Input()).stateIn(this).value }
        val relations = async { allRelations(AllRelations.Input()).stateIn(this).value }
        val dreamPersonCrossrefs = async { allDreamPersonCrossrefs(Unit).stateIn(this).value }
        val dreamLocationsCrossrefs = async { allDreamLocationCrossrefs(Unit).stateIn(this).value }

        return@withContext IOData(
            dreams = dreams.await(),
            persons = persons.await(),
            locations = locations.await(),
            relations = relations.await(),
            dreamPersonCrossrefs = dreamPersonCrossrefs.await().map {
                Pair(it.first.toInt(), it.second.toInt())
            },
            dreamLocationsCrossrefs = dreamLocationsCrossrefs.await().map {
                Pair(it.first.toInt(), it.second.toInt())
            }
        )
    }

    private fun handleFiletypeChange(type: ExportFiletype) = viewModelScope.launch {
        _state.emit(
            state.value.copy(
                selectedFiletype = type
            )
        )
    }

}