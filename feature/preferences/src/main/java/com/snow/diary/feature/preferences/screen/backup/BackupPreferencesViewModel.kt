package com.snow.diary.feature.preferences.screen.backup;

import androidx.lifecycle.viewModelScope
import com.snow.diary.core.domain.action.preferences.GetPreferences
import com.snow.diary.core.domain.action.preferences.UpdateBackupEnabled
import com.snow.diary.core.domain.action.preferences.UpdateBackupRule
import com.snow.diary.core.domain.viewmodel.EventViewModel
import com.snow.diary.core.model.preferences.BackupRule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.Period
import javax.inject.Inject

@HiltViewModel
internal class BackupPreferencesViewModel @Inject constructor(
    getPreferences: GetPreferences,
    val updateBackupEnabled: UpdateBackupEnabled,
    val updateBackupRule: UpdateBackupRule
) : EventViewModel<BackupPreferencesEvent>() {

    val state = getPreferences(Unit)
        .map {
            BackupPreferencesState(
                rule = it.backupPreferences.backupRule.toUiRule(),
                ruleValue = it.backupPreferences.backupRule.value(),
                backupEnabled = it.backupPreferences.backupEnabled
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    override suspend fun handleEvent(event: BackupPreferencesEvent): Any = when (event) {
        is BackupPreferencesEvent.ChangeBackupRule -> {
            state.value?.let {
                updateBackupRule(
                    createBackupRule(
                        event.rule, defaultValues[event.rule]
                    )
                )
            }
            true
        }

        is BackupPreferencesEvent.ChangeBackupValue -> {
            state.value?.let {
                updateBackupRule(
                    createBackupRule(
                        it.rule, event.value
                    )
                )
            }
            true
        }

        BackupPreferencesEvent.ToggleBackup -> {
            state.value?.backupEnabled?.let { enabled ->
                updateBackupEnabled(!enabled)
            }
            true
        }
    }

}

private val defaultValues = mapOf(
    UIBackupRule.AmountLimit to 20,
    UIBackupRule.TimeLimit to 30,
    UIBackupRule.StorageLimit to 50
)

private fun createBackupRule(rule: UIBackupRule, value: Int?): BackupRule = when (rule) {
    UIBackupRule.Infinite -> BackupRule.Infinite
    UIBackupRule.StorageLimit -> BackupRule.StorageLimit(value!!)
    UIBackupRule.TimeLimit -> BackupRule.TimeLimit(Period.ofDays(value!!))
    UIBackupRule.AmountLimit -> BackupRule.AmountLimit(value!!)
}

private fun BackupRule.toUiRule(): UIBackupRule = when (this) {
    is BackupRule.AmountLimit -> UIBackupRule.AmountLimit
    BackupRule.Infinite -> UIBackupRule.Infinite
    is BackupRule.StorageLimit -> UIBackupRule.StorageLimit
    is BackupRule.TimeLimit -> UIBackupRule.TimeLimit
}

private fun BackupRule.value(): Int? = when (this) {
    is BackupRule.AmountLimit -> backups
    BackupRule.Infinite -> null
    is BackupRule.StorageLimit -> megabytes
    is BackupRule.TimeLimit -> period.days
}