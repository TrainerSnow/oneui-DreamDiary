package com.snow.diary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.snow.diary.core.domain.action.preferences.GetPreferences
import com.snow.diary.core.model.preferences.ColorMode
import com.snow.diary.ui.DiaryApplicationRoot
import com.snow.diary.ui.rememberDiaryState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.theme.color.OneUIColorTheme
import javax.inject.Inject


@AndroidEntryPoint
class RootActivity : ComponentActivity() {

    @Inject
    lateinit var getPreferences: GetPreferences

    private val viewModel: RootActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)

        var rootState: RootState by mutableStateOf(RootState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.rootState
                    .onEach {
                        rootState = it
                    }
                    .collect()
            }
        }

        splash.setKeepOnScreenCondition { rootState == RootState.Loading }

        setContent {
            val diaryState = rememberDiaryState(
                getPreferences = getPreferences
            )

            val isDarkMode = isDarkMode(
                system = isSystemInDarkTheme(),
                pref = (rootState as? RootState.Success)?.preferences?.colorMode ?: ColorMode.System
            )

            WindowCompat.setDecorFitsSystemWindows(window, false)

            OneUITheme(
                colorTheme = OneUIColorTheme.getTheme(dark = isDarkMode)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    DiaryApplicationRoot(diaryState)
                }
            }
        }
    }
}

private fun isDarkMode(
    system: Boolean,
    pref: ColorMode
): Boolean = when (pref) {
    ColorMode.Light -> false
    ColorMode.Dark -> true
    ColorMode.System -> system
}