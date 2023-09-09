package com.snow.diary.export.screen;

import androidx.lifecycle.viewModelScope
import com.snow.diary.domain.action.cross.AllDreamLocationCrossrefs
import com.snow.diary.domain.action.cross.AllDreamPersonCrossrefs
import com.snow.diary.domain.action.dream.AllDreams
import com.snow.diary.domain.action.location.AllLocations
import com.snow.diary.domain.action.person.AllPersons
import com.snow.diary.domain.action.relation.AllRelations
import com.snow.diary.domain.viewmodel.EventViewModel
import com.snow.diary.io.ExportFiletype
import com.snow.diary.io.data.IOData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class ExportViewModel @Inject constructor(
    val allDreams: AllDreams,
    val allPersons: AllPersons,
    val allLocations: AllLocations,
    val allRelations: AllRelations,
    val allDreamLocationCrossrefs: AllDreamLocationCrossrefs,
    val allDreamPersonCrossrefs: AllDreamPersonCrossrefs
) : EventViewModel<ExportEvent>() {

    private val _state = MutableStateFlow(ExportState())
    val state: StateFlow<ExportState> = _state

    override suspend fun handleEvent(event: ExportEvent) = when (event) {
        ExportEvent.Export -> TODO()
        is ExportEvent.SelectFiletype -> handleFiletypeChange(event.filetype)
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