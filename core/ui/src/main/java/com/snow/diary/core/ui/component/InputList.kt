package com.snow.diary.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import org.oneui.compose.widgets.menu.PopupMenu


@Composable
fun <T> InputList(
    modifier: Modifier = Modifier,
    popupItems: List<T>,
    showPopup: Boolean,
    onPopupDismiss: () -> Unit,
    selectedItems: List<T>,
    item: @Composable (item: T, index: Int) -> Unit,
    popupItem: @Composable (T) -> Unit,
    searchText: @Composable (selectedItems: List<T>) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        val posProvider = object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize
            ): IntOffset {
                val spaceTop = anchorBounds.top
                val popupHeight = popupContentSize.height

                return if (popupHeight <= spaceTop) {
                    IntOffset(
                        x = 0,
                        y = anchorBounds.top - popupHeight
                    )
                } else IntOffset(
                    x = 0,
                    y = anchorBounds.bottom
                )
            }
        }

        selectedItems.forEachIndexed { index, item ->
            item(item, index)
        }
        Box {
            searchText(selectedItems)

            if (showPopup) {
                PopupMenu(
                    modifier = Modifier
                        .heightIn(max = 200.dp),
                    onDismissRequest = onPopupDismiss,
                    properties = PopupProperties(),
                    popupPositionProvider = posProvider
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                    ) {
                        popupItems.forEach { item ->
                            popupItem(item)
                        }
                    }
                }
            }
        }
    }
}