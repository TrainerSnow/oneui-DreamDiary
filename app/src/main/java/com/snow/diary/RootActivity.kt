package com.snow.diary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.snow.diary.core.domain.action.preferences.GetPreferences
import com.snow.diary.ui.DiaryApplicationRoot
import com.snow.diary.ui.rememberDiaryState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class RootActivity : ComponentActivity() {

    @Inject
    lateinit var getPreferences: GetPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val diaryState = rememberDiaryState(
                getPreferences = getPreferences
            )

            WindowCompat.setDecorFitsSystemWindows(window, false)

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                DiaryApplicationRoot(diaryState)
            }
        }
    }


}