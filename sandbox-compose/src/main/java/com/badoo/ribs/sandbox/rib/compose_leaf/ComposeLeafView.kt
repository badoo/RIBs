package com.badoo.ribs.sandbox.rib.compose_leaf

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.badoo.ribs.compose.ComposeRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.view.ViewFactoryBuilder
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

    interface Factory : ViewFactoryBuilder<Nothing?, ComposeLeafView>
}


class ComposeLeafViewImpl private constructor(
    context: Context,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : ComposeRibView(context),
    ComposeLeafView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory : ComposeLeafView.Factory {
        override fun invoke(deps: Nothing?): ViewFactory<ComposeLeafView> = ViewFactory {
            ComposeLeafViewImpl(
                it.parent.context
            )
        }
    }

    private var viewModel: MutableState<ViewModel> = mutableStateOf(ViewModel())

    override fun accept(vm: ViewModel) {
        viewModel.value = vm
    }

    override val composable: @Composable () -> Unit = {
        Column {
            Text(text = "ComposeLeafView: ${viewModel.value.text}")
        }
    }
}
