package com.snow.diary.locations.screen.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.snow.diary.locations.R

enum class LocationDetailTab {

    General,

    Dreams

}


@Composable
fun LocationDetailTab.localizedName() = stringResource(
    when (this) {
        LocationDetailTab.General -> R.string.location_detail_tab_general
        LocationDetailTab.Dreams -> R.string.location_detail_tab_dreams
    }
)