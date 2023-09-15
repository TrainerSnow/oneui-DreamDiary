package com.snow.diary.feature.preferences.screen.main;

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.diary.core.model.preferences.ColorMode
import com.snow.diary.core.model.preferences.SecurityMode
import com.snow.diary.core.model.preferences.UserPreferences
import com.snow.diary.core.ui.preferences.PreferencesCategory
import com.snow.diary.feature.preferences.R
import com.snow.diary.feature.preferences.localization.localizedName
import org.oneui.compose.base.Icon
import org.oneui.compose.layout.toolbar.CollapsingToolbarCollapsedState
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.layout.toolbar.rememberCollapsingToolbarState
import org.oneui.compose.preference.DropdownPreference
import org.oneui.compose.preference.SingleSelectPreference
import org.oneui.compose.preference.SwitchPreferenceScreen
import org.oneui.compose.widgets.buttons.IconButton
import org.oneui.compose.widgets.text.TextSeparator
import dev.oneuiproject.oneui.R as IconR


@Composable
internal fun MainPreferencesScreen(
    viewModel: MainPreferencesViewModel = hiltViewModel(),
    onNavigateToObfuscationPreferences: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val prefs by viewModel.preferences.collectAsStateWithLifecycle()

    MainPreferencesScreen(
        prefs = prefs,
        onEvent = viewModel::onEvent,
        onNavigateToObfuscationPreferences = onNavigateToObfuscationPreferences,
        onNavigateBack = onNavigateBack
    )
}

@Composable
private fun MainPreferencesScreen(
    prefs: UserPreferences?,
    onEvent: (MainPreferencesEvent) -> Unit,
    onNavigateToObfuscationPreferences: () -> Unit,
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

        PreferencesCategory(
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
        )

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
                    SingleSelectPreference(
                        title = stringResource(R.string.preferences_main_securitymode),
                        value = prefs.securityMode,
                        values = SecurityMode.entries,
                        nameFor = {
                            ctx.getString(it.localizedName)
                        },
                        onValueChange = { onEvent(MainPreferencesEvent.ChangeSecurityMode(it)) }
                    )
                },
                {
                    SwitchPreferenceScreen(
                        onClick = onNavigateToObfuscationPreferences,
                        title = stringResource(R.string.preferences_main_obfuscation),
                        summary = stringResource(R.string.preferences_main_obfuscation_desc),
                        onSwitch = {
                            onEvent(
                                MainPreferencesEvent.ChangeObfuscationEnabled(it)
                            )
                        },
                        switched = prefs.obfuscationPreferences.obfuscationEnabled
                    )
                }
            )
        )
    }
}