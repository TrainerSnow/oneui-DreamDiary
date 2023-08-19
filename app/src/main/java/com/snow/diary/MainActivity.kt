package com.snow.diary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.snow.feature.dreams.nav.dreamDetail
import com.snow.feature.dreams.nav.dreamList
import com.snow.feature.dreams.nav.goToDreamDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "dream_list"
            ) {
                dreamList(
                    onAboutClick = { },
                    onAddClick = { },
                    onSearchClick = { },
                    onDreamClick = { dream ->
                        navController
                            .goToDreamDetail(dream.id)
                    },
                    onExportClick = { },
                    onNavigateBack = { }
                )
                dreamDetail(
                    onNavigateBack = navController::navigateUp,
                    onLocationClick = { },
                    onPersonClick = { },
                    onRelationClick = { }
                )
            }
        }
    }


}