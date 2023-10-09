package com.snow.diary.feature.appinfo.screen.about;

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.stateIn
import org.oneui.compose.layout.app.UpdateState
import javax.inject.Inject

@HiltViewModel
class AppInfoViewModel @Inject constructor(

): ViewModel() {

    //TODO: When published, update state accordingly here
    val updateState = emptyFlow<UpdateState>()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UpdateState.Loading
        )

}