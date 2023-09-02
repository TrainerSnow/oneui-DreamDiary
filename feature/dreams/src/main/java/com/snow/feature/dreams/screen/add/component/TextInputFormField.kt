package com.snow.feature.dreams.screen.add.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import dev.oneuiproject.oneui.R
import org.oneui.compose.base.Icon
import org.oneui.compose.base.IconView
import org.oneui.compose.base.iconColors
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.util.ListPosition
import org.oneui.compose.widgets.EditText
import org.oneui.compose.widgets.box.RoundedCornerListItem
import org.oneui.compose.widgets.buttons.IconButton
import org.oneui.compose.widgets.buttons.iconButtonColors

//TODO: Possibly move this into the lib
@Composable
fun TextInputFormField(
    modifier: Modifier = Modifier,
    input: String,
    onInputChange: (String) -> Unit,
    hint: String? = null,
    onClearClick: (() -> Unit)? = null,
    icon: Icon? = null,
    listPosition: ListPosition = ListPosition.Single
) {
    var focused by remember {
        mutableStateOf(false)
    }
    val focusRequester = FocusRequester()

    RoundedCornerListItem(
        modifier = modifier
            .animateContentSize(),
        padding = TextInputFormFieldDefaults.padding,
        listPosition = listPosition
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement
                .spacedBy(TextInputFormFieldDefaults.iconTextSpacing)
        ) {
            icon?.let {
                IconView(
                    icon = icon,
                    colors = iconColors(
                        tint = if (focused)
                            OneUITheme.colors.seslPrimaryColor
                        else
                            OneUITheme.colors.seslEditTextHintColor
                    )
                )
            }

            Box(
                modifier = Modifier
                    .weight(1F)
            ) {
                EditText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged {
                            focused = (it.isFocused || it.isCaptured)
                        }
                        .focusRequester(focusRequester),
                    value = input,
                    hint = hint ?: "",
                    onValueChange = onInputChange
                )
            }

            onClearClick?.let { callback ->
                IconButton(
                    icon = Icon.Resource(R.drawable.ic_oui_minus),
                    colors = iconButtonColors(
                        tint = OneUITheme.colors.seslFunctionalRed
                    ),
                    onClick = {
                        callback()
                        focusRequester.freeFocus()
                    }
                )
            }
        }
    }
}

private object TextInputFormFieldDefaults {

    val iconTextSpacing = 16.dp

    val padding = PaddingValues(
        vertical = 16.dp,
        horizontal = 24.dp
    )

}