package com.snow.diary.feature.preferences.screen.obfuscation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import org.oneui.compose.widgets.SwitchBar
import org.oneui.compose.widgets.buttons.IconButton
import dev.oneuiproject.oneui.R as IconR


@Composable
internal fun ObfuscationPreferencesScreen(
    viewModel: ObfuscatePreferencesViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val preferences by viewModel.preferences.collectAsStateWithLifecycle()

    ObfuscationPreferencesScreen(
        prefs = preferences,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}


@Composable
private fun ObfuscationPreferencesScreen(
    prefs: ObfuscationPreferences?,
    onEvent: (ObfuscationPreferencesEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    if(prefs == null) return //Show empty screen instead
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
        val childrenEnabled = !prefs.obfuscationEnabled

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
                    horizontal = 16.dp
                ),
            text = stringResource(
                if (prefs.obfuscationEnabled)
                    R.string.preferences_obfuscate_info_enabled
                else
                    R.string.preferences_obfuscate_info_disabled
            )
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
}