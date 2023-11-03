package com.snow.diary.core.backup.model

sealed class BackupLoadingResult {

    data object Success: BackupLoadingResult()

    data object FileError: BackupLoadingResult()

    data object ImportError: BackupLoadingResult()

}