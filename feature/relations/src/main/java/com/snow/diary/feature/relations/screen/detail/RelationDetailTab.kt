package com.snow.diary.feature.relations.screen.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.snow.diary.feature.relations.R

enum class RelationDetailTab {

    General,

    Persons

}


@Composable
fun RelationDetailTab.localizedName() = stringResource(
    when (this) {
        RelationDetailTab.General -> R.string.relation_detail_tab_general
        RelationDetailTab.Persons -> R.string.relation_detail_tab_persons
    }
)