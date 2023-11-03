package com.snow.diary.core.backup.model

import java.time.LocalDateTime

data class BackupInfo(

    val timestamp: LocalDateTime,

    val size: Long

) {

    fun fileName(): String = createFilename(timestamp)

    companion object {

        fun createFilename(timestamp: LocalDateTime): String = "Diary-Backup($timestamp).json"

        fun timestampFromName(name: String): LocalDateTime = name
            .replace("Diary-Backup(", "")
            .replace(").json", "")
            .let(LocalDateTime::parse)

    }

}
