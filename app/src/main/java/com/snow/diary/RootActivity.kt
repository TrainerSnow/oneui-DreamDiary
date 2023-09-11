package com.snow.diary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.snow.diary.ui.DiaryApplicationRoot
import com.snow.diary.ui.rememberDiaryState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RootActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val diaryState = rememberDiaryState()

            DiaryApplicationRoot(diaryState)
        }
    }


}