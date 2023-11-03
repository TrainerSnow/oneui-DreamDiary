package com.snow.diary.feature.preferences.screen.backup

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.snow.diary.core.model.preferences.BackupTiming
import com.snow.diary.feature.preferences.R
import dev.oneuiproject.oneui.R as IconR

internal data class BackupPreferencesState(

    val rule: UIBackupRule,

    val ruleValue: Int?,

    val backupEnabled: Boolean,

    val backupTiming: BackupTiming

)

val BackupTiming.localizedName: String
    @Composable get() = stringResource(
        when (this) {
            BackupTiming.Monthly -> R.string.preferences_backup_timing_monthly
            BackupTiming.Weekly -> R.string.preferences_backup_timing_weekly
            BackupTiming.Daily -> R.string.preferences_backup_timing_daily
            BackupTiming.Dynamic -> R.string.preferences_backup_timing_dynamic
        }
    )

internal enum class UIBackupRule(

    @StringRes val displayName: Int,

    val extra: BackupRuleValueInfo?

) {

    Infinite(
        displayName = R.string.preferences_backup_rule_infinite,
        extra = null
    ),

    StorageLimit(
        displayName = R.string.preferences_backup_data_limit,
        extra = BackupRuleValueInfo(
            icon = IconR.drawable.ic_oui_manage_storage,
            hint = R.string.preferences_backup_data_limit_hint
        )
    ),

    TimeLimit(
        displayName = R.string.preferences_backup_time_limit,
        extra = BackupRuleValueInfo(
            icon = IconR.drawable.ic_oui_calendar_day,
            hint = R.string.preferences_backup_time_limit_hint
        )
    ),

    AmountLimit(
        displayName = R.string.preferences_backup_amount_limit,
        BackupRuleValueInfo(
            icon = IconR.drawable.ic_oui_text_numbering,
            hint = R.string.preferences_backup_amount_limit_hint
        )
    )

}

internal data class BackupRuleValueInfo(

    @DrawableRes val icon: Int,

    @StringRes val hint: Int

)
