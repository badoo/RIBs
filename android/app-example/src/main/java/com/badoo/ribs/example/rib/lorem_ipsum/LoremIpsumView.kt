package com.badoo.ribs.example.rib.lorem_ipsum

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.widget.Button
import com.jakewharton.rxrelay2.PublishRelay
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.example.R
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsumView.Event
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsumView.ViewModel
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface LoremIpsumView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object ButtonClicked : Event()
    }

    data class ViewModel(
        val i: Int = 0
    )
}

class LoremIpsumViewImpl private constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0, private val events: PublishRelay<Event>
) : ConstraintLayout(context, attrs, defStyle),
    LoremIpsumView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
    ) : this(context, attrs, defStyle, PublishRelay.create<Event>())

    override val androidView = this
    private val button: Button by lazy { findViewById<Button>(R.id.lorem_ipsum_button) }

    override fun onFinishInflate() {
        super.onFinishInflate()
        button.setOnClickListener { events.accept(Event.ButtonClicked) }
    }

    override fun accept(vm: ViewModel) {
    }
}
