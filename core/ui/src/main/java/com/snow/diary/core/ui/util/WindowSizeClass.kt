package com.snow.diary.core.ui.util

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
val windowSizeClass: WindowSizeClass
    @Composable get() = WindowSizeClass.calculateFromSize(
        size = DpSize(
            width = LocalConfiguration.current.screenWidthDp.dp,
            height = LocalConfiguration.current.screenHeightDp.dp
        )
    )

val WindowSizeClass.useNavigationRail
    get() = widthSizeClass in listOf(WindowWidthSizeClass.Expanded, WindowWidthSizeClass.Medium)

val WindowSizeClass.useNavigationDrawer
    get() = widthSizeClass == WindowWidthSizeClass.Compact