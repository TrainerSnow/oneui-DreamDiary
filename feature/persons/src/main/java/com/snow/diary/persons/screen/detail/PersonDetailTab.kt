package com.snow.diary.persons.screen.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.snow.diary.persons.R

enum class PersonDetailTab {

    General,

    Dreams

}


@Composable
fun PersonDetailTab.localizedName() = stringResource(
    when (this) {
        PersonDetailTab.General -> R.string.person_detail_loading
        PersonDetailTab.Dreams -> R.string.person_detail_dreams
    }
)