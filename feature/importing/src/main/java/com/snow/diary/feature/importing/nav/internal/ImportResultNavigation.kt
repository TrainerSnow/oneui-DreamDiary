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
import java.util.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

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
        uri = savedStateHandle.get<String>(uriArgId)!!.let {
            val decoded = Base64.getDecoder().decode(it).toString(Charsets.UTF_8)
            val uri = Uri.parse(decoded)
            uri
        },
        type = ImportFiletype.valueOf(savedStateHandle.get<String>(typeArgId)!!)
    )
}


internal fun NavController.goToImportResult(
    type: ImportFiletype,
    uri: Uri,
    navOptions: NavOptions? = null
) {
    val encodedUri = Base64.getEncoder().encode(uri.toString().toByteArray()).toString(Charsets.UTF_8)
    navigate(
        importResultRoute
            .withArg(
                uriArgId,
                encodedUri
            )
            .withArg(
                typeArgId,
                type.name
            ),
        navOptions
    )
}

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
            },
            navArgument(
                name = typeArgId
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