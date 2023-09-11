package com.snow.diary.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.snow.diary.app.ui.DiaryApplicationRoot
import com.snow.diary.app.ui.rememberDiaryState
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