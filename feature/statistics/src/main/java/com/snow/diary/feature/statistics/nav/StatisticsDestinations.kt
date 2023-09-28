package com.snow.diary.feature.statistics.nav

import androidx.annotation.StringRes
import com.snow.diary.feature.statistics.R
import org.oneui.compose.base.Icon
import dev.oneuiproject.oneui.R as IconR

internal enum class StatisticsDestinations(
    val icon: Icon,
    @StringRes val title: Int
) {

    Dreams(
        icon = Icon.Resource(IconR.drawable.ic_oui_remove_2), //TODO: Proper icon
        title = R.string.stats_nav_dreams
    ),

    Persons(
        icon = Icon.Resource(IconR.drawable.ic_oui_contact_outline),
        title = R.string.stats_nav_persons
    ),

    Locations(
        icon = Icon.Resource(IconR.drawable.ic_oui_location_outline),
        title = R.string.stats_nav_locations
    )

}