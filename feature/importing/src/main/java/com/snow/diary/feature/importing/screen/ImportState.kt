package com.snow.diary.feature.importing.screen;

import com.snow.diary.core.domain.action.io.ImportIOData

internal sealed class ImportState {

    data object Importing: ImportState()

    data class ImportFailed(
        val problems: List<ImportIOData.ImportProblem>
    ): ImportState()

    data class ImportSuccess(
        val dreamsNum: Int,
        val personsNum: Int,
        val locationsNum: Int,
        val relationsNum: Int
    ): ImportState()

}