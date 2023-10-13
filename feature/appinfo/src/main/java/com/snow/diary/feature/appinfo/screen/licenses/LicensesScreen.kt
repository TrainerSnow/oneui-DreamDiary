package com.snow.diary.feature.appinfo.screen.licenses

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.util.withContext
import com.snow.diary.feature.appinfo.R
import org.oneui.compose.base.Icon
import org.oneui.compose.layout.toolbar.CollapsingToolbarCollapsedState
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.layout.toolbar.rememberCollapsingToolbarState
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.util.ListPosition
import org.oneui.compose.widgets.box.RoundedCornerListItem
import org.oneui.compose.widgets.buttons.IconButton
import dev.oneuiproject.oneui.R as IconR

@Composable
internal fun LicensesScreen(
    onNavigateBack: () -> Unit,
) {
    val libraries = Libs.Builder().withContext(LocalContext.current).build().libraries

    CollapsingToolbarLayout(
        modifier = Modifier
            .fillMaxSize(),
        toolbarTitle = stringResource(R.string.appinfo_licenses),
        appbarNavAction = {
            IconButton(
                icon = Icon.Resource(IconR.drawable.ic_oui_back),
                onClick = onNavigateBack
            )
        },
        state = rememberCollapsingToolbarState(CollapsingToolbarCollapsedState.COLLAPSED)
    ) {
        var expanded by remember { mutableIntStateOf(-1) }

        //TODO: Fixe once https://github.com/JetBrains/compose-multiplatform-core/pull/865 is merged
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            libraries.forEachIndexed { index, library ->
                LicenseCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    listPosition = ListPosition.get(library, libraries),
                    expanded = index == expanded,
                    library = library,
                    onExpand = {
                        expanded = if (expanded == index) -1 else index
                    }
                )
            }
        }
    }
}

@Composable
private fun LicenseCard(
    modifier: Modifier = Modifier,
    listPosition: ListPosition,
    expanded: Boolean,
    library: Library,
    onExpand: () -> Unit
) {
    val titleTextStyle = TextStyle(
        fontSize = 17.sp,
        fontWeight = FontWeight.SemiBold
    )
    val nameTextStyle = TextStyle(
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium
    )
    val authorTextStyle = TextStyle(
        color = OneUITheme.colors.seslSecondaryTextColor,
        fontSize = 12.sp
    )
    val contentTextStyle = TextStyle(
        fontSize = 13.sp
    )

    RoundedCornerListItem(
        modifier = modifier
            .animateContentSize(),
        onClick = onExpand,
        listPosition = listPosition
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = library.name,
                    style = titleTextStyle
                )
                Text(
                    text = library.organization?.name ?: "",
                    style = authorTextStyle
                )
            }
            Text(
                text = library.licenses.first().name,
                style = nameTextStyle
            )
            if (expanded) {
                Text(
                    text = library.licenses.first().licenseContent ?: "",
                    style = contentTextStyle
                )
            }
        }
    }
}