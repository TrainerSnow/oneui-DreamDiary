package com.snow.diary.feature.statistics.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.snow.diary.feature.statistics.nav.StatisticsDestinations
import com.snow.diary.feature.statistics.nav.dreamNavigation
import com.snow.diary.feature.statistics.nav.goToDreamStatistics
import com.snow.diary.feature.statistics.nav.goToLocationsStatistics
import com.snow.diary.feature.statistics.nav.goToPersonsStatistics
import com.snow.diary.feature.statistics.nav.locationsStatistics
import com.snow.diary.feature.statistics.nav.personsStatistics
import org.oneui.compose.navigation.BottomNavigationBar
import org.oneui.compose.navigation.BottomNavigationBarItem

@Composable
internal fun MainStatistics(
    onNavigateBack: () -> Unit
) {
    val navController = rememberNavController()

    MainStatistics(
        onDestinationChange = {
            when (it) {
                StatisticsDestinations.Dreams -> navController.goToDreamStatistics()
                StatisticsDestinations.Persons -> navController.goToPersonsStatistics()
                StatisticsDestinations.Locations -> navController.goToLocationsStatistics()
            }
        },
        onNavigateBack = onNavigateBack,
        navController = navController
    )
}


@Composable
private fun MainStatistics(
    onDestinationChange: (StatisticsDestinations) -> Unit,
    onNavigateBack: () -> Unit,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        NavHost(
            modifier = Modifier
                .weight(1F)
                .fillMaxWidth(),
            navController = navController,
            startDestination = "dream_statistics"
        ) {
            dreamNavigation(onNavigateBack)
            personsStatistics(onNavigateBack)
            locationsStatistics(onNavigateBack)
        }

        BottomNavigationBar(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            StatisticsDestinations.entries.forEach { dest ->
                BottomNavigationBarItem(
                    modifier = Modifier.weight(1F),
                    onClick = { onDestinationChange(dest) },
                    label = stringResource(dest.title),
                    icon = dest.icon
                )
            }
        }
    }
}