package com.snow.diary.core.ui.preferences

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.oneui.compose.util.ListPosition
import org.oneui.compose.widgets.box.RoundedCornerListItem

@Composable
fun PreferencesCategory(
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    preferences: List<@Composable () -> Unit>
) {
    Column(
        modifier = modifier
    ) {
        title?.let { it() }
        preferences.forEach { pref ->
            RoundedCornerListItem(
                modifier = Modifier
                    .fillMaxWidth(),
                listPosition = ListPosition.get(pref, preferences),
                padding = PaddingValues()
            ) {
                pref()
            }
        }
    }
}