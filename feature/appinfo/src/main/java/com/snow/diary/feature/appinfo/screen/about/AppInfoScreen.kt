package com.snow.diary.feature.appinfo.screen.about

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.diary.core.ui.util.windowSizeClass
import com.snow.diary.feature.appinfo.R
import org.oneui.compose.layout.app.AppInfoLayout
import org.oneui.compose.layout.app.AppInfoLayoutButton
import org.oneui.compose.layout.app.UpdateState

private const val GithubLink = "https://github.com/TrainerSnow/oneui-DreamDary"

@Composable
internal fun AppInfoScreen(
    viewModel: AppInfoViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onLicensesClick: () -> Unit
) {
    val updateState by viewModel.updateState.collectAsStateWithLifecycle()

    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    AppInfoScreen(
        onNavigateBack = onNavigateBack,
        onInfoClick = {
            context.startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.packageName, null)
                )
            )
        },
        updateState = updateState,
        onGithubClick = {
            uriHandler.openUri(GithubLink)
        },
        onLicensesClick = onLicensesClick
    )
}

@Composable
private fun AppInfoScreen(
    onNavigateBack: () -> Unit,
    onInfoClick: () -> Unit,
    updateState: UpdateState,
    onGithubClick: () -> Unit,
    onLicensesClick: () -> Unit
) {
    AppInfoLayout(
        modifier = Modifier
            .fillMaxSize(),
        horizontalLayout = windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact,
        updateState = updateState,
        onNavigateBackClick = onNavigateBack,
        onInfoClick = onInfoClick,
        buttons = {
            AppInfoLayoutButton(
                onClick = onGithubClick
            ) {
                Text(
                    text = stringResource(R.string.appinfo_github)
                )
            }
            AppInfoLayoutButton(
                onClick = onLicensesClick
            ) {
                Text(
                    text = stringResource(R.string.appinfo_licenses)
                )
            }
        }
    )
}