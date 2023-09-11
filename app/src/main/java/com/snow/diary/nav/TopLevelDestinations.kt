package com.snow.diary.nav

import androidx.annotation.StringRes
import com.snow.diary.R
import org.oneui.compose.base.Icon
import dev.oneuiproject.oneui.R as IconR


//Screens/parts of the app navigable from the main nav component
enum class TopLevelDestinations(
    val icon: Icon,
    @StringRes val titleRes: Int
) {

    Dreams(
        icon = Icon.Resource(IconR.drawable.ic_oui_list),
        titleRes = R.string.nav_dreams
    ),

    Persons(
        icon = Icon.Resource(IconR.drawable.ic_oui_contact_outline),
        titleRes = R.string.nav_persons
    ),

    Locations(
        icon = Icon.Resource(IconR.drawable.ic_oui_location_outline),
        titleRes = R.string.nav_locations
    ),

    Statistics(
        icon = Icon.Resource(IconR.drawable.ic_oui_equalizer_2),
        titleRes = R.string.nav_stats
    ),

}
