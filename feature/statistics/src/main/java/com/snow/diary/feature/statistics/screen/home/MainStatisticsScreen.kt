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
import com.snow.diary.feature.statistics.dreams.nav.dreamNavigation
import com.snow.diary.feature.statistics.dreams.nav.goToDreamStatistics
import com.snow.diary.feature.statistics.nav.StatisticsDestinations
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
                StatisticsDestinations.Persons -> TODO()
                StatisticsDestinations.Locations -> TODO()
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
            dreamNavigation(
                onNavigateBack = onNavigateBack
            )
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