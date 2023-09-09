package com.snow.diary.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

data class DiaryState(

    val navController: NavHostController

)

@Composable
fun rememberDiaryState(
    navController: NavHostController = rememberNavController()
) = DiaryState(navController)
