package com.snow.diary.ui.util

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.snow.diary.model.sort.SortConfig
import com.snow.diary.model.sort.SortDirection
import com.snow.diary.model.sort.SortMode
import com.snow.diary.ui.R
import org.oneui.compose.base.Icon
import org.oneui.compose.base.IconView
import org.oneui.compose.base.iconColors
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.util.OneUIPreview
import org.oneui.compose.widgets.menu.PopupMenu
import org.oneui.compose.widgets.menu.SelectableMenuItem
import dev.oneuiproject.oneui.R as IconR

@Composable
fun SortSection(
    modifier: Modifier = Modifier,
    sortConfig: SortConfig = SortConfig(
        direction = SortDirection.Ascending
    ),
    sortModes: List<SortMode> = SortMode.values().toList(),
    onSortChange: ((SortConfig) -> Unit)? = null
) {
    val density = LocalDensity.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var height by remember {
            mutableStateOf(0.dp)
        }
        ModeSection(
            modifier = Modifier
                .onSizeChanged {
                    height = with(density) { it.height.toDp() }
                },
            modes = sortModes,
            mode = sortConfig.mode,
            onModeChange = { mode ->
                onSortChange?.let {
                    it(
                        sortConfig.copy(
                            mode = mode
                        )
                    )
                }
            }
        )

        val dividerColor = OneUITheme.colors.seslListDividerColor
        Canvas(
            modifier = modifier
                .width(SortSectionDefaults.dividerWidth)
                .height(height)
        ) {
            drawLine(
                color = dividerColor,
                strokeWidth = SortSectionDefaults.dividerStrokeWidth,
                start = Offset(
                    x = size.width / 2,
                    y = SortSectionDefaults.sectionPadding.calculateTopPadding().toPx()
                ),
                end = Offset(
                    x = size.width / 2,
                    y = size.height - SortSectionDefaults.sectionPadding.calculateBottomPadding().toPx()
                )
            )
        }

        DirectionSection(
            direction = sortConfig.direction,
            onDirectionChange = { direction ->
                onSortChange?.let {
                    it(
                        sortConfig.copy(
                            direction = direction
                        )
                    )
                }
            }
        )
    }
}

@Composable
private fun DirectionSection(
    modifier: Modifier = Modifier,
    direction: SortDirection,
    onDirectionChange: (SortDirection) -> Unit
) {
    val iconRes = when (direction) {
        SortDirection.Ascending -> IconR.drawable.ic_oui_arrow_up
        SortDirection.Descending -> IconR.drawable.ic_oui_arrow_down
        SortDirection.Unspecified -> IconR.drawable.ic_oui_checkbox_unchecked
    }
    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    color = OneUITheme.colors.seslRippleColor
                ),
                onClick = {
                    onDirectionChange(
                        when (direction) {
                            SortDirection.Ascending -> SortDirection.Descending
                            else -> SortDirection.Ascending
                        }
                    )
                }
            )
            .padding(SortSectionDefaults.sectionPadding),
    ) {
        IconView(
            icon = Icon.Resource(iconRes),
            modifier = Modifier
                .size(
                    with(LocalDensity.current) { SortSectionDefaults.sectionIconSize.toDp() }
                ),
            colors = iconColors(
                tint = OneUITheme.colors.seslSecondaryTextColor
            )
        )
    }
}

@Composable
private fun ModeSection(
    modifier: Modifier = Modifier,
    modes: List<SortMode>,
    mode: SortMode,
    onModeChange: (SortMode) -> Unit
) {
    val style = TextStyle(
        color = OneUITheme.colors.seslSecondaryTextColor,
        fontSize = 14.sp
    )

    var showPopup by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    color = OneUITheme.colors.seslRippleColor
                ),
                onClick = { showPopup = true }
            )
            .padding(SortSectionDefaults.sectionPadding),
        contentAlignment = Alignment.TopStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement
                .spacedBy(SortSectionDefaults.sectionIconSpacing)
        ) {
            IconView(
                icon = Icon.Resource(IconR.drawable.ic_oui_list_sort),
                modifier = Modifier
                    .size(
                        with(LocalDensity.current) { SortSectionDefaults.sectionIconSize.toDp() }
                    ),
                colors = iconColors(
                    tint = OneUITheme.colors.seslSecondaryTextColor
                )
            )
            Text(
                text = mode.localizedString,
                style = style
            )
        }
        if (showPopup) {
            PopupMenu(
                onDismissRequest = { showPopup = false },
                visible = true
            ) {
                modes.forEach {
                    SelectableMenuItem(
                        label = it.localizedString,
                        onSelect = {
                            onModeChange(it)
                            showPopup = false
                                   },
                        selected = it == mode
                    )
                }
            }
        }
    }
}

private object SortSectionDefaults {

    val sectionPadding = PaddingValues(
        all = 6.dp
    )
    val sectionIconSpacing = 4.dp
    val sectionIconSize = 16.sp

    val dividerWidth = 12.dp
    const val dividerStrokeWidth = 2F
}

private val SortMode.localizedString: String
    @Composable get() = stringResource(
        when (this) {
            SortMode.Created -> R.string.sortmode_created
            SortMode.Updated -> R.string.sortmode_updated
            SortMode.Alphabetically -> R.string.sortmode_alphabetically
            SortMode.Happiness -> R.string.sortmode_happiness
            SortMode.Clearness -> R.string.sortmode_clearness
            SortMode.Relation -> R.string.sortmode_relation
            SortMode.Length -> R.string.sortmode_length
            SortMode.None -> R.string.sortmode_none
        }
    )

private val SortDirection.localizedString: String
    @Composable get() = stringResource(
        when (this) {
            SortDirection.Ascending -> R.string.sortdirection_ascending
            SortDirection.Descending -> R.string.sortdirection_descending
            SortDirection.Unspecified -> R.string.sortdirection_none
        }
    )

@Preview
@Composable
fun SortMode() = OneUIPreview(title = "SortMode") {
    ModeSection(
        modes = SortMode.values().toList(),
        mode = SortMode.Created,
        onModeChange = {}
    )
}

@Preview
@Composable
fun SortSectionPreview() = OneUIPreview(title = "SortSection") {
    var config by remember {
        mutableStateOf(SortConfig())
    }
    SortSection(
        sortConfig = config,
        onSortChange = { config = it }
    )
}