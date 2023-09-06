package com.snow.diary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.snow.diary.export.navigation.exportScreen
import com.snow.diary.export.navigation.goToExport
import com.snow.feature.dreams.nav.addDream
import com.snow.feature.dreams.nav.dreamDetail
import com.snow.feature.dreams.nav.dreamList
import com.snow.feature.dreams.nav.goToAddDream
import com.snow.feature.dreams.nav.goToDreamDetail
import dagger.hilt.android.AndroidEntryPoint
import org.oneui.compose.theme.OneUITheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .background(OneUITheme.colors.seslRoundAndBgcolor),
                navController = navController,
                startDestination = "dream_list"
            ) {
                dreamList(
                    onAboutClick = { },
                    onAddClick = {
                        navController
                            .goToAddDream(null)
                    },
                    onSearchClick = { },
                    onDreamClick = { dream ->
                        navController
                            .goToDreamDetail(dream.id!!)
                    },
                    onExportClick = navController::goToExport,
                    onNavigateBack = { }
                )
                dreamDetail(
                    onNavigateBack = navController::navigateUp,
                    onLocationClick = { },
                    onPersonClick = { },
                    onRelationClick = { },
                    onEditClick = {
                        navController
                            .goToAddDream(it.id)
                    }
                )
                addDream(
                    dismissDream = navController::navigateUp
                )
                exportScreen(
                    onNavigateBack = navController::navigateUp
                )
            }
        }
    }


}