package com.badoo.ribs.test.util.ribs

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.test.util.ribs.TestRibView.Event
import com.badoo.ribs.test.util.ribs.TestRibView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface TestRibView : RibView, ObservableSource<Event>, Consumer<ViewModel> {

    sealed class Event

    data class ViewModel(
        val text: String
    )
}


class TestRibViewImpl private constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0, private val events: PublishRelay<Event>
) : FrameLayout(context, attrs, defStyle),
    TestRibView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
    ) : this(context, attrs, defStyle, PublishRelay.create<Event>())

    override val androidView = this

    override fun accept(vm: TestRibView.ViewModel) {

    }
}
