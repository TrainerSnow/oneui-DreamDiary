package com.snow.diary.feature.importing.screen.result

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snow.diary.core.common.launchInBackground
import com.snow.diary.core.domain.action.io.ImportIOData
import com.snow.diary.core.io.importing.IImportAdapter
import com.snow.diary.feature.importing.nav.internal.ImportResultArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
internal class ImportResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext val context: Context,
    val importIOData: ImportIOData
) : ViewModel() {

    private val args: ImportResultArgs = ImportResultArgs(savedStateHandle)

    private val _state: MutableStateFlow<ImportResultState> =
        MutableStateFlow(ImportResultState.Importing)
    val state: StateFlow<ImportResultState> = _state

    init {
        val `is` = context.contentResolver.openInputStream(args.uri)
        val importAdapter = IImportAdapter.getInstance(args.type)

        val data = importAdapter.import(`is`!!)
        `is`.close()
        viewModelScope.launchInBackground {
            val problems = importIOData(data)

            val state = if (problems.isEmpty()) {
                ImportResultState.ImportSuccess(
                    dreamsNum = data.dreams.size,
                    personsNum = data.persons.size,
                    locationsNum = data.locations.size,
                    relationsNum = data.relations.size
                )
            } else {
                ImportResultState.ImportFailed(
                    problems = problems
                )
            }

            _state.emit(state)
        }
    }

}