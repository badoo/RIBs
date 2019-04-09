package com.badoo.ribs.tutorials.tutorial3.rib.greetings_container

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.ViewGroup
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.tutorials.tutorial3.R
import com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.GreetingsContainerView.Event
import com.badoo.ribs.tutorials.tutorial3.rib.greetings_container.GreetingsContainerView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface GreetingsContainerView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event

    data class ViewModel(
        val i: Int = 0
    )

    val childContainer: ViewGroup
}

class GreetingsContainerViewImpl private constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0, private val events: PublishRelay<Event>
) : ConstraintLayout(context, attrs, defStyle),
    GreetingsContainerView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
    ) : this(context, attrs, defStyle, PublishRelay.create<Event>())

    override val androidView = this
    override val childContainer: ViewGroup by lazy { findViewById<ViewGroup>(R.id.greetings_container_child) }

    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    override fun accept(vm: ViewModel) {
    }
}
