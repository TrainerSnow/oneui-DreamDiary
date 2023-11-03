package com.snow.diary.core.backup.implementation;

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.snow.diary.core.backup.BackupRepository
import com.snow.diary.core.backup.model.BackupCreationResult
import com.snow.diary.core.backup.model.BackupInfo
import com.snow.diary.core.backup.model.BackupLoadingResult
import com.snow.diary.core.domain.action.io.GetIOData
import com.snow.diary.core.domain.action.io.ImportIOData
import com.snow.diary.core.domain.action.preferences.GetPreferences
import com.snow.diary.core.io.ExportFiletype
import com.snow.diary.core.io.ImportFiletype
import com.snow.diary.core.io.exporting.IExportAdapter
import com.snow.diary.core.io.importing.IImportAdapter
import com.snow.diary.core.model.preferences.BackupRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.time.LocalDateTime


private const val jsonMime = "application/json"

internal class FileBackupRepository(
    private val getPreferences: GetPreferences,
    private val getIOData: GetIOData,
    private val importIOData: ImportIOData,
    private val context: Context
) : BackupRepository {

    private val importer = IImportAdapter.getInstance(ImportFiletype.JSON)
    private val exporter = IExportAdapter.getInstance(ExportFiletype.JSON)

    override suspend fun createBackup(): BackupCreationResult = withContext(Dispatchers.IO) {
        val data = getIOData(Unit).first()
        val now = LocalDateTime.now()

        val filename = BackupInfo.createFilename(now)
        val directory = getDirectory() ?: return@withContext BackupCreationResult.NoDirectorySet

        require(directory.isDirectory) { "The uri saved in preferences does not point towards a directory, but a file." }

        val file = directory.createFile(jsonMime, filename)
            ?: return@withContext BackupCreationResult.Error
        val os = context.contentResolver.openOutputStream(file.uri)
            ?: return@withContext BackupCreationResult.Error
        exporter.export(data, os)
        os.close()

        val size = file.length()

        return@withContext BackupCreationResult.Success(BackupInfo(now, size))
    }

    override suspend fun loadBackup(backup: BackupInfo): BackupLoadingResult =
        withContext(Dispatchers.IO) {
            val directory = getDirectory() ?: return@withContext BackupLoadingResult.FileError
            val file = directory.findFile(backup.fileName())
                ?: return@withContext BackupLoadingResult.FileError
            val `is` = context.contentResolver.openInputStream(file.uri)
                ?: return@withContext BackupLoadingResult.FileError

            val data = try {
                importer.import(`is`)
            } catch (_: Exception) {
                return@withContext BackupLoadingResult.ImportError
            }
            `is`.close()

            importIOData(data)
            return@withContext BackupLoadingResult.Success
        }

    override suspend fun getAll(): List<BackupInfo> = getAllBackupFiles()?.map { file ->
            BackupInfo(
                BackupInfo.timestampFromName(file.name!!)!!, file.length()
            )
        } ?: emptyList()

    override suspend fun delete(backup: BackupInfo) {
        val directory = getDirectory()
        directory?.findFile(backup.fileName())?.delete()
    }

    override suspend fun ensureRule(backupRule: BackupRule) {
        when (backupRule) {
            is BackupRule.AmountLimit -> {
                val allSorted = getAllBackupFiles()?.sortedByDescending {
                        BackupInfo.timestampFromName(it.name!!)
                    } ?: return
                if (allSorted.size <= backupRule.backups) return
                val dif = allSorted.size - backupRule.backups
                allSorted.reversed().forEachIndexed { index, file ->
                        if (index < dif) {
                            file.delete()
                        }
                    }
            }

            is BackupRule.StorageLimit -> {
                val all = getAllBackupFiles()?.sortedBy {
                        BackupInfo.timestampFromName(it.name!!)
                    } ?: return

                var bytes = all.sumOf { it.length() }
                var index = 0
                while (bytes > (backupRule.megabytes * 1_000_000)) {
                    val toDelete = all[index]
                    bytes -= toDelete.length()
                    toDelete.delete()
                    index += 1
                }
            }

            is BackupRule.TimeLimit -> {
                val all = getAllBackupFiles()?.sortedByDescending {
                        BackupInfo.timestampFromName(it.name!!)
                    } ?: return
                val now = LocalDateTime.now()
                val limit = now.minusDays(backupRule.period.days.toLong())

                var firstInvalid = -1
                all.forEachIndexed { index, file ->
                    val timestamp = BackupInfo.timestampFromName(file.name!!)!!
                    if (timestamp < limit) {
                        firstInvalid = index
                        return@forEachIndexed
                    }
                }

                if (firstInvalid == -1) return
                all.subList(firstInvalid, all.size).forEach { it.delete() }
            }

            else -> {}
        }
    }

    private suspend fun getDirectory(): DocumentFile? = withContext(Dispatchers.IO) {
        val uri = getPreferences(Unit).first().backupPreferences.backupDirectoryUri
        return@withContext DocumentFile.fromTreeUri(context, Uri.parse(uri.toString()))
    }

    private suspend fun getAllBackupFiles(): List<DocumentFile>? =
        getDirectory()?.listFiles()?.filter { it.name != null }
            ?.filter { BackupInfo.timestampFromName(it.name!!) != null }


}