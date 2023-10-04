package com.snow.diary.feature.importing.nav.internal

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.snow.diary.core.io.ImportFiletype
import com.snow.diary.feature.importing.screen.result.ImportResultScreen

private const val uriArgId = "uri"
private const val typeArgId = "type"
private const val importResultRoute = "import_result/{$uriArgId}/{$typeArgId}"

private fun String.withArg(name: String, arg: String): String =
    replace("{$name}", arg)

internal class ImportResultArgs(
    val uri: Uri,
    val type: ImportFiletype
) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        uri = Uri.parse(savedStateHandle[uriArgId]),
        type = ImportFiletype.valueOf(savedStateHandle[typeArgId]!!)
    )
}


internal fun NavController.goToImportResult(
    uri: Uri,
    navOptions: NavOptions? = null
) = navigate(
    importResultRoute.withArg(uriArgId, uri.toString()),
    navOptions
)

internal fun NavGraphBuilder.importResult(
    onNavigateBack: () -> Unit
) {
    composable(
        route = importResultRoute,
        arguments = listOf(
            navArgument(
                name = uriArgId
            ) {
                type = NavType.StringType
            }
        )
    ) {
        ImportResultScreen(
            onNavigateBack = onNavigateBack
        )
    }
}