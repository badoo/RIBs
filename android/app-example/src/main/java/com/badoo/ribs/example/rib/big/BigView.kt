package com.badoo.ribs.example.rib.big

import androidx.annotation.LayoutRes
import android.view.ViewGroup
import android.widget.TextView
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.customisation.inflate
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.big.BigView.Event
import com.badoo.ribs.example.rib.big.BigView.ViewModel
import com.badoo.ribs.example.rib.small.Small
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface BigView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event

    data class ViewModel(
        val text: String
    )

    interface Factory : ViewFactory<Nothing?, BigView>
}


class BigViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : BigView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_big
    ) : BigView.Factory {
        override fun invoke(deps: Nothing?): (ViewGroup) -> BigView = {
            BigViewImpl(
                inflate(it, layoutRes)
            )
        }
    }

    private val idText = androidView.findViewById<TextView>(R.id.big_id)
    private val smallContainer = androidView.findViewById<ViewGroup>(R.id.small_container)

    override fun accept(vm: ViewModel) {
        idText.text = vm.text
    }

    override fun getParentViewForChild(child: Node<*>): ViewGroup? =
        when (child.identifier) {
            is Small -> smallContainer
            else -> null
        }
}
