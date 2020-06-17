package com.badoo.ribs.sandbox.rib.compose_leaf

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.Composable
import androidx.compose.Recomposer
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.sandbox.rib.compose_leaf.ComposeLeafView.Event
import com.badoo.ribs.sandbox.rib.compose_leaf.ComposeLeafView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface ComposeLeafView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event

    data class ViewModel(
        val text: String = "Initial view model text"
    )

    interface Factory : ViewFactory<Nothing?, ComposeLeafView>
}


class ComposeLeafViewImpl private constructor(
    context: Context,
    private val composable: @Composable() (ViewModel) -> Unit,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : ComposeLeafView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        private val composable: @Composable() (ViewModel) -> Unit = { Content(it) }
    ) : ComposeLeafView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> ComposeLeafView = {
            ComposeLeafViewImpl(
                it.context,
                composable
            )
        }
    }

    override val androidView: ViewGroup = FrameLayout(context)

    override fun accept(vm: ViewModel) {
        androidView.setContent(Recomposer.current()) {
            composable(vm)
        }
    }
}

@Composable
fun Content(vm: ViewModel) {
    Column {
        Text(text = "ComposeLeafView: ${vm.text}")
    }
}
