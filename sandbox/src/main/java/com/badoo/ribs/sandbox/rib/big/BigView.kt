package com.badoo.ribs.sandbox.rib.big

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.sandbox.R
import com.badoo.ribs.sandbox.rib.big.BigView.Event
import com.badoo.ribs.sandbox.rib.big.BigView.ViewModel
import com.badoo.ribs.sandbox.rib.small.Small
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
) : AndroidRibView(),
    BigView,
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

    override fun getParentViewForChild(child: Node<*>): ViewGroup =
        when (child) {
            is Small -> smallContainer
            else -> super.getParentViewForChild(child)
        }
}
