package com.snow.diary.core.model.preferences

import java.time.Period

data class BackupPreferences(

    val backupEnabled: Boolean,

    val backupDirectoryUri: String?,

    val backupRule: BackupRule

)

sealed class BackupRule {

    /**
     * No limit to backups
     */
    data object Infinite: BackupRule()

    /**
     * Keep as many backups as possible with a given data amount as constraint
     */
    data class StorageLimit(val megabytes: Int): BackupRule()

    /**
     * Keep exact amount of backups
     */
    data class AmountLimit(val backups: Int): BackupRule()

    /**
     * Keep backups reaching back as far as [period]
     */
    data class TimeLimit(val period: Period): BackupRule()

}
