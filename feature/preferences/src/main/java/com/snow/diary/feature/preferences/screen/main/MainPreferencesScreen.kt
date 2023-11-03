package com.snow.diary.feature.preferences.screen.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.diary.core.model.preferences.UserPreferences
import com.snow.diary.core.ui.preferences.PreferencesCategory
import com.snow.diary.feature.preferences.R
import org.oneui.compose.base.Icon
import org.oneui.compose.layout.toolbar.CollapsingToolbarCollapsedState
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.layout.toolbar.rememberCollapsingToolbarState
import org.oneui.compose.preference.BasePreference
import org.oneui.compose.preference.SwitchPreference
import org.oneui.compose.widgets.buttons.IconButton
import org.oneui.compose.widgets.text.TextSeparator
import dev.oneuiproject.oneui.R as IconR


@Composable
internal fun MainPreferencesScreen(
    viewModel: MainPreferencesViewModel = hiltViewModel(),
    onNavigateToObfuscationPreferences: () -> Unit,
    onNavigateToBackupPreferences: () -> Unit,
    onAboutClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val prefs by viewModel.preferences.collectAsStateWithLifecycle()

    MainPreferencesScreen(
        prefs = prefs,
        onEvent = viewModel::onEvent,
        onNavigateToObfuscationPreferences = onNavigateToObfuscationPreferences,
        onNavigateToBackupPreferences = onNavigateToBackupPreferences,
        onAboutClick = onAboutClick,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun MainPreferencesScreen(
    prefs: UserPreferences?,
    onEvent: (MainPreferencesEvent) -> Unit,
    onNavigateToObfuscationPreferences: () -> Unit,
    onNavigateToBackupPreferences: () -> Unit,
    onAboutClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
    if (prefs == null) return //Simply blank screen. IDK if this is good
    CollapsingToolbarLayout(
        toolbarTitle = stringResource(R.string.preferences_main_title),
        state = rememberCollapsingToolbarState(CollapsingToolbarCollapsedState.COLLAPSED),
        appbarNavAction = {
            IconButton(
                icon = Icon.Resource(IconR.drawable.ic_oui_back),
                onClick = onNavigateBack
            )
        }
    ) {
        val ctx = LocalContext.current

        /*PreferencesCategory(
            modifier = Modifier
                .fillMaxWidth(),
            title = {
                TextSeparator(
                    text = stringResource(R.string.preferences_main_section_visuals)
                )
            },
            preferences = listOf {
                DropdownPreference(
                    title = stringResource(R.string.preferences_main_colormode),
                    item = prefs.colorMode,
                    items = ColorMode.entries,
                    nameFor = {
                        ctx.getString(it.localizedName)
                    },
                    onItemSelected = { onEvent(MainPreferencesEvent.ChangeColorMode(it)) }
                )
            }
        )*/ //TODO: Possibly re-add the option to customize dark/light mode

        PreferencesCategory(
            modifier = Modifier
                .fillMaxWidth(),
            title = {
                TextSeparator(
                    text = stringResource(R.string.preferences_main_section_privacy)
                )
            },
            preferences = listOf(
                {
                    SwitchPreference(
                        title = stringResource(R.string.preferences_main_requireauth),
                        summary = stringResource(R.string.preferences_main_requireauth_desc),
                        switched = prefs.requireAuth,
                        onSwitchedChange = {
                            onEvent(
                                MainPreferencesEvent.ChangeRequireAuth(it)
                            )
                        }
                    )
                },
                {
                    BasePreference(
                        onClick = onNavigateToObfuscationPreferences,
                        title = {
                            Text(
                                text = stringResource(R.string.preferences_main_obfuscation)
                            )
                        },
                        summary = {
                            Text(
                                text = stringResource(R.string.preferences_main_obfuscation_desc)
                            )
                        }
                    )
                }
            )
        )

        PreferencesCategory(
            modifier = Modifier
                .fillMaxWidth(),
            title = {
                TextSeparator(
                    text = stringResource(R.string.preferences_main_section_data_security)
                )
            },
            preferences = listOf {
                BasePreference(
                    onClick = onNavigateToBackupPreferences,
                    title = {
                        Text(
                            text = stringResource(R.string.preferences_main_backup)
                        )
                    },
                    summary = {
                        Text(
                            text = stringResource(R.string.preferences_main_backup_desc)
                        )
                    }
                )
            }
        )

        PreferencesCategory(
            title = {
                TextSeparator(
                    text = stringResource(R.string.preferences_main_section_other)
                )
            },
            preferences = listOf {
                BasePreference(
                    onClick = onAboutClick,
                    title = {
                        Text(
                            text = stringResource(R.string.about_this_app)
                        )
                    }
                )
            }
        )
    }
}