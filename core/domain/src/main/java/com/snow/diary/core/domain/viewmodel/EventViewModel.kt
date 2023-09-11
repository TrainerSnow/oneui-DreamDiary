package com.snow.diary.core.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


/**
 * Super class for any [ViewModel] that plans to receive events from the ui.
 *
 * @param Event
 */
abstract class EventViewModel <Event>: ViewModel() {

    private val events = MutableSharedFlow<Event>()

    init {
        viewModelScope.launch {
            events.collect(::handleEvent)
        }
    }

    fun onEvent(event: Event) = viewModelScope.launch {
        events.emit(event)
    }

    protected abstract suspend fun handleEvent(event: Event): Any

}