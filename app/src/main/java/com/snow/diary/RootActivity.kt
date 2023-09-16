package com.snow.diary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

            DiaryApplicationRoot(diaryState)
        }
    }


}