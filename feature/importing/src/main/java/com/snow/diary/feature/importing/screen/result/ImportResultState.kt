package com.snow.diary.feature.importing.screen.result;

import com.snow.diary.core.domain.action.io.ImportIOData

internal sealed class ImportResultState {

    data object Importing: ImportResultState()

    data class ImportFailed(
        val problems: List<ImportIOData.ImportProblem>
    ): ImportResultState()

    data class ImportSuccess(
        val dreamsNum: Int,
        val personsNum: Int,
        val locationsNum: Int,
        val relationsNum: Int
    ): ImportResultState()

}