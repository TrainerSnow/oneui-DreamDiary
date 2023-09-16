package com.snow.diary.feature.preferences.screen.obfuscation

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.diary.core.model.preferences.ObfuscationPreferences
import com.snow.diary.core.ui.preferences.PreferencesCategory
import com.snow.diary.feature.preferences.R
import org.oneui.compose.base.Icon
import org.oneui.compose.layout.toolbar.CollapsingToolbarCollapsedState
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.layout.toolbar.rememberCollapsingToolbarState
import org.oneui.compose.preference.SwitchPreference
import org.oneui.compose.progress.CircularProgressIndicatorSize
import org.oneui.compose.progress.ProgressIndicator
import org.oneui.compose.progress.ProgressIndicatorType
import org.oneui.compose.widgets.SwitchBar
import org.oneui.compose.widgets.buttons.IconButton
import dev.oneuiproject.oneui.R as IconR


@Composable
internal fun ObfuscationPreferencesScreen(
    viewModel: ObfuscatePreferencesViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val preferences by viewModel.preferences.collectAsStateWithLifecycle()
    val isObfuscating by viewModel.isObfuscating.collectAsStateWithLifecycle()

    ObfuscationPreferencesScreen(
        prefs = preferences,
        isObfuscating = isObfuscating,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}


@Composable
private fun ObfuscationPreferencesScreen(
    prefs: ObfuscationPreferences?,
    isObfuscating: Boolean,
    onEvent: (ObfuscationPreferencesEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    if (prefs == null) return //Show empty screen instead
    CollapsingToolbarLayout(
        modifier = Modifier
            .fillMaxSize(),
        state = rememberCollapsingToolbarState(CollapsingToolbarCollapsedState.COLLAPSED),
        appbarNavAction = {
            IconButton(
                icon = Icon.Resource(IconR.drawable.ic_oui_back),
                onClick = onNavigateBack
            )
        },
        toolbarTitle = stringResource(R.string.preferences_obfuscate_title)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val childrenEnabled = prefs.obfuscationEnabled

                SwitchBar(
                    modifier = Modifier
                        .fillMaxWidth(),
                    switched = prefs.obfuscationEnabled,
                    onSwitchedChange = {
                        onEvent(
                            ObfuscationPreferencesEvent.ChangeDoObfuscation(it)
                        )
                    }
                )

                Text(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp
                        )
                        .height(75.dp),
                    text = stringResource(R.string.preferences_obfuscate_info)
                )

                PreferencesCategory(
                    modifier = Modifier
                        .fillMaxWidth(),
                    preferences = listOf(
                        {
                            SwitchPreference(
                                title = stringResource(R.string.preferences_obfuscate_dreams),
                                switched = prefs.obfuscateDreams,
                                onSwitchedChange = {
                                    onEvent(
                                        ObfuscationPreferencesEvent.ChangeObfuscateDreams(it)
                                    )
                                },
                                enabled = childrenEnabled
                            )
                        },
                        {
                            SwitchPreference(
                                title = stringResource(R.string.preferences_obfuscate_persons),
                                switched = prefs.obfuscatePersons,
                                onSwitchedChange = {
                                    onEvent(
                                        ObfuscationPreferencesEvent.ChangeObfuscatePersons(it)
                                    )
                                },
                                enabled = childrenEnabled
                            )
                        },
                        {
                            SwitchPreference(
                                title = stringResource(R.string.preferences_obfuscate_locations),
                                switched = prefs.obfuscateLocations,
                                onSwitchedChange = {
                                    onEvent(
                                        ObfuscationPreferencesEvent.ChangeObfuscateLocations(it)
                                    )
                                },
                                enabled = childrenEnabled
                            )
                        },
                        {
                            SwitchPreference(
                                title = stringResource(R.string.preferences_obfuscate_relations),
                                switched = prefs.obfuscateRelations,
                                onSwitchedChange = {
                                    onEvent(
                                        ObfuscationPreferencesEvent.ChangeObfuscateRelations(it)
                                    )
                                },
                                enabled = childrenEnabled
                            )
                        }
                    )
                )
            }

            if (isObfuscating) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures { }
                            detectDragGestures { _, _ -> }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    ProgressIndicator(
                        type = ProgressIndicatorType.CircularIndeterminate(
                            size = CircularProgressIndicatorSize.Companion.XLarge
                        )
                    )
                }
            }
        }
    }
}