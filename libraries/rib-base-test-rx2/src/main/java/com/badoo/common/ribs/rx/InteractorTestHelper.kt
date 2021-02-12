package com.badoo.common.ribs.rx

import android.view.ViewGroup
import com.badoo.common.ribs.InteractorTestHelper
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.ObservableSource
import io.reactivex.Observer
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when` as whenever


inline fun <reified View, ViewEvent> createInteractorTestHelper(
    interactor: Interactor<*, View>,
    viewEventRelay: Relay<ViewEvent>
): InteractorTestHelper<View> where View : RibView, View : ObservableSource<ViewEvent> {
    val view: View = viewEventRelay.subscribedView()
    return InteractorTestHelper(interactor, { view })
}

inline fun <reified R, reified Input, reified Output> Interactor<R, *>.mockIO(
    inputRelay: Relay<Input> = PublishRelay.create(),
    outputRelay: Relay<Output> = PublishRelay.create()
) where R : Rib, R : Connectable<Input, Output> {
    val rib = mock(R::class.java).apply {
        whenever(input).thenReturn(inputRelay)
        whenever(output).thenReturn(outputRelay)
    }
    init(rib)
}

inline fun <reified RView, ViewEvent> Relay<ViewEvent>.subscribedView(): RView where RView : RibView, RView : ObservableSource<ViewEvent> =
    mock(RView::class.java).apply {
        Mockito.`when`(this.androidView).thenReturn(mock(ViewGroup::class.java))
        Mockito.`when`(this.subscribe(any())).thenAnswer {
            val observer = it.getArgument<Observer<ViewEvent>>(0)
            this@subscribedView.subscribe(observer)
        }
    }
