package com.snow.diary.locations.screen.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.diary.locations.R
import com.snow.diary.ui.component.TextInputFormField
import org.oneui.compose.base.Icon
import org.oneui.compose.layout.toolbar.CollapsingToolbarCollapsedState
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.layout.toolbar.rememberCollapsingToolbarState
import org.oneui.compose.widgets.buttons.ColoredButton
import org.oneui.compose.widgets.buttons.IconButton
import org.oneui.compose.widgets.buttons.TransparentButton
import dev.oneuiproject.oneui.R as IconR

@Composable
internal fun AddLocation(
    viewModel: AddLocationViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val inputState by viewModel.inputState.collectAsStateWithLifecycle()
    val isEdit = viewModel.isEdit

    AddLocation(
        state = inputState,
        isEdit = isEdit,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}


//TODO: When available, this screen should use a fullscreen dialog (= PopOverActivity). FOr now, it uses a simple CTL
@Composable
private fun AddLocation(
    state: AddLocationState,
    isEdit: Boolean,
    onEvent: (AddLocationEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    CollapsingToolbarLayout(
        toolbarTitle = stringResource(
            if (isEdit) R.string.location_edit_title
            else R.string.location_add_title
        ),
        expandable = false,
        state = rememberCollapsingToolbarState(
            CollapsingToolbarCollapsedState.COLLAPSED,
            with(LocalDensity.current) { 100.dp.toPx() } //TODO Remove when lib is ready
        ),
        appbarNavAction = {
            IconButton(
                icon = Icon.Resource(IconR.drawable.ic_oui_close),
                onClick = onNavigateBack
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement
                .spacedBy(AddLocationScreenDefaults.columnSpacing)
        ) {
            TextInputFormField(
                modifier = Modifier
                    .fillMaxWidth(),
                input = state.name.input,
                hint = stringResource(R.string.location_add_name),
                onInputChange = { onEvent(AddLocationEvent.ChangeName(it)) },
                icon = Icon.Resource(IconR.drawable.ic_oui_location_outline)
            )
            TextInputFormField(
                modifier = Modifier
                    .fillMaxWidth(),
                input = state.note.input,
                hint = stringResource(R.string.location_add_note),
                onInputChange = { onEvent(AddLocationEvent.ChangeNote(it)) },
                icon = Icon.Resource(IconR.drawable.ic_oui_memo_outline)
            )

            //TODO: Do something fancy with maybe google maps api location picker here!

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TransparentButton(
                    label = stringResource(R.string.location_add_cancel),
                    onClick = onNavigateBack
                )
                ColoredButton(
                    label = stringResource(R.string.location_add_save),
                    onClick = {
                        onEvent(
                            AddLocationEvent.Save
                        )
                        onNavigateBack()
                    }
                )
            }
        }
    }
}

//TODO: Many of these screen default values are duplicated per screen. Move to lib/central file (maybe :ui)
private object AddLocationScreenDefaults {

    val columnSpacing = 12.dp

}