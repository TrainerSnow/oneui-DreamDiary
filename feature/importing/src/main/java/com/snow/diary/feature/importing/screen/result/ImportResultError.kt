package com.snow.diary.feature.importing.screen.result;

import androidx.annotation.StringRes
import com.snow.diary.core.domain.action.io.ImportIOData
import com.snow.diary.feature.importing.R

enum class ImportResultError(
    @StringRes val descriptionRes: Int
) {

    NonUniqueId(R.string.import_problem_nonunique),

    InvalidCrossref(R.string.import_problem_invalidref),

    CorruptedFile(R.string.import_problem_corrupted)

    ;

    companion object {

        fun fromDomain(problem: ImportIOData.ImportProblem): ImportResultError = when (problem) {
            ImportIOData.ImportProblem.InvalidCrossrefReference -> InvalidCrossref
            ImportIOData.ImportProblem.NonUniqueIds -> NonUniqueId
        }

    }

}