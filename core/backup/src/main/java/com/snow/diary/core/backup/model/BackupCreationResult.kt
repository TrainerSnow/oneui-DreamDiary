package com.snow.diary.core.backup.model

sealed class BackupCreationResult {

    data class Success(
        val info: BackupInfo
    ): BackupCreationResult()

    data object Error: BackupCreationResult()

    data object NoDirectorySet: BackupCreationResult()

}