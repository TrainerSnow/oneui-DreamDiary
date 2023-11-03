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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.last
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
        val data = getIOData(Unit).last()
        val now = LocalDateTime.now()

        val filename = BackupInfo.createFilename(now)
        val directory = getDirectory() ?: return@withContext BackupCreationResult.Error

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

    override suspend fun getAll(): List<BackupInfo> {
        val directory = getDirectory() ?: return emptyList()
        return directory
            .listFiles()
            .filter { it.name != null }
            .map { file ->
                BackupInfo(
                    BackupInfo.timestampFromName(file.name!!),
                    file.length()
                )
            }
    }

    override suspend fun delete(backup: BackupInfo) {
        val directory = getDirectory()
        directory?.findFile(backup.fileName())?.delete()
    }

    private suspend fun getDirectory(): DocumentFile? = withContext(Dispatchers.IO) {
        val uri = getPreferences(Unit).last().backupPreferences.backupDirectoryUri
        return@withContext DocumentFile.fromTreeUri(context, Uri.parse(uri.toString()))
    }


}