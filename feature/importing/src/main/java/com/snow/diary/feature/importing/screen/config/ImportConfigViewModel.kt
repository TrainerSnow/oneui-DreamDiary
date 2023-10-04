package com.snow.diary.feature.importing.screen.config;

import com.snow.diary.core.domain.viewmodel.EventViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class ImportConfigViewModel @Inject constructor() : EventViewModel<ImportConfigEvent>() {

    private val _state = MutableStateFlow(ImportConfigState())
    val state: StateFlow<ImportConfigState> = _state

    override suspend fun handleEvent(event: ImportConfigEvent) = when (event) {
        is ImportConfigEvent.SelectType -> _state.update { it.copy(selectedType = event.type) }
    }

}