package com.snow.diary.feature.importing.screen.result;

internal sealed class ImportResultState {

    data object Importing: ImportResultState()

    data class ImportFailed(
        val errors: List<ImportResultError>
    ): ImportResultState()

    data class ImportSuccess(
        val dreamsNum: Int,
        val personsNum: Int,
        val locationsNum: Int,
        val relationsNum: Int
    ): ImportResultState()

}