package com.snow.diary.feature.dreams.screen.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.snow.diary.feature.dreams.R

internal data class DreamDetailTabState(

    val tab: DreamDetailTab,

    val subtab: DreamDetailSubtab

)

internal enum class DreamDetailTab {

    General,

    Persons,

    Locations

    ;

    @Composable
    fun localizedName() = stringResource(
        id = when (this) {
            General -> R.string.dream_detail_tab_general
            Persons -> R.string.dream_detail_tab_persons
            Locations -> R.string.dream_detail_tab_locations
        }
    )

}

internal enum class DreamDetailSubtab {

    Content,

    Other

    ;

    @Composable
    fun localizedName() = stringResource(
        id = when (this) {
            Content -> R.string.dream_detail_tab_content
            Other -> R.string.dream_detail_tab_other
        }
    )

}