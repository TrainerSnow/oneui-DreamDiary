package com.snow.diary.feature.preferences.screen.backup

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.diary.core.model.preferences.BackupPreferences
import com.snow.diary.core.ui.preferences.PreferencesCategory
import com.snow.diary.feature.preferences.R
import org.oneui.compose.base.Icon
import org.oneui.compose.base.IconView
import org.oneui.compose.input.InputFormField
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.preference.SingleSelectPreference
import org.oneui.compose.util.ListPosition
import org.oneui.compose.widgets.EditText
import org.oneui.compose.widgets.SwitchBar
import org.oneui.compose.widgets.buttons.IconButton
import org.oneui.compose.widgets.text.TextSeparator
import dev.oneuiproject.oneui.R as IconR

@Composable
internal fun BackupPreferencesScreen(
    viewModel: BackupPreferencesViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BackupPreferencesScreen(
        state = state,
        onNavigateBack = onNavigateBack,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun BackupPreferencesScreen(
    state: BackupPreferencesState?,
    onNavigateBack: () -> Unit,
    onEvent: (BackupPreferencesEvent) -> Unit
) {
    if(state == null) return

    CollapsingToolbarLayout(
        modifier = Modifier
            .fillMaxSize(),
        toolbarTitle = stringResource(R.string.preferences_backup),
        appbarNavAction = {
            IconButton(
                icon = Icon.Resource(IconR.drawable.ic_oui_back),
                onClick = onNavigateBack
            )
        }
    ) {
        SwitchBar(
            modifier = Modifier
                .fillMaxWidth(),
            onSwitchedChange = {
                onEvent(BackupPreferencesEvent.ToggleBackup)
            },
            switched = state.backupEnabled
        )

        RuleSection(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            rule = state.rule,
            ruleValue = state.ruleValue,
            onRuleChange = {
                onEvent(BackupPreferencesEvent.ChangeBackupRule(it))
            },
            onValueChange = {
                onEvent(BackupPreferencesEvent.ChangeBackupValue(it))
            },
            enabled = state.backupEnabled
        )
    }
}

@Composable
private fun RuleSection(
    modifier: Modifier = Modifier,
    rule: UIBackupRule,
    ruleValue: Int?,
    onRuleChange: (UIBackupRule) -> Unit,
    onValueChange: (Int) -> Unit,
    enabled: Boolean
) {
    val names = UIBackupRule.entries.map { it to stringResource(it.displayName) }.toMap()

    val ruleSelection = @Composable {
        SingleSelectPreference(
            title = stringResource(R.string.preferences_backup_rule),
            value = rule,
            values = UIBackupRule.entries,
            onValueChange = onRuleChange,
            nameFor = { names[it]!! },
            enabled = enabled
        )
    }

    val extraSection = @Composable {
        if (rule.extra != null) {
            InputFormField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = "[Placeholder]",
                onValueChange = { },
                leadingIcon = { focused ->
                    IconView(
                        icon = Icon.Resource(rule.extra.icon)
                    )
                },
                editText = {
                    EditText(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = ruleValue!!.toString(),
                        onValueChange = {
                            val num = it.toIntOrNull() ?: 0
                            onValueChange(num)
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )
                },
                listPosition = ListPosition.Last
            )
        }
    }

    val prefs = if (rule.extra == null) listOf(ruleSelection)
    else listOf(ruleSelection, extraSection)

    PreferencesCategory(
        modifier = modifier,
        preferences = prefs,
        title = {
            TextSeparator(
                text = stringResource(R.string.preferences_backup_rule)
            )
        }
    )
}

