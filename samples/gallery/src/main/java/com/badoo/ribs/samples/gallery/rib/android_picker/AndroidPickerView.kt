package com.badoo.ribs.samples.gallery.rib.android_picker

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.badoo.ribs.compose.ComposeRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactoryBuilder
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.samples.gallery.rib.android_picker.AndroidPickerView.Event
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource

interface AndroidPickerView : RibView,
    ObservableSource<Event> {

    sealed class Event {
        object LaunchingActivitiesClicked : Event()
        object PermissionsClicked : Event()
        object DialogsClicked : Event()
    }

    fun interface Factory : ViewFactoryBuilder<Nothing?, AndroidPickerView>
}


class AndroidPickerViewImpl private constructor(
    context: Context,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : ComposeRibView(context),
    AndroidPickerView,
    ObservableSource<Event> by events {

    class Factory : AndroidPickerView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<AndroidPickerView> = ViewFactory {
            AndroidPickerViewImpl(
                it.parent.context
            )
        }
    }

    override val composable: @Composable () -> Unit = {
        View(
            onLaunchingActivitiesClicked = { events.accept(Event.LaunchingActivitiesClicked) },
            onPermissionsClicked = { events.accept(Event.PermissionsClicked) },
            onDialogsClicked = { events.accept(Event.DialogsClicked) },
        )
    }
}

@Preview
@Composable
private fun View(
    onLaunchingActivitiesClicked: () -> Unit = {},
    onPermissionsClicked: () -> Unit = {},
    onDialogsClicked: () -> Unit = {},
) {
    Box(
        modifier = Modifier.fillMaxSize(1f),
        contentAlignment = Alignment.Center
    ) {
        val buttonModifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()

        Column(modifier = Modifier.width(IntrinsicSize.Min)) {
            Button(
                onClick = onLaunchingActivitiesClicked,
                modifier = buttonModifier
            ) {
                Text("Launching Activities")
            }
            Button(
                onClick = onPermissionsClicked,
                modifier = buttonModifier
            ) {
                Text("Permissions")
            }
            Button(
                onClick = onDialogsClicked,
                modifier = buttonModifier
            ) {
                Text("Dialogs")
            }
        }
    }
}
