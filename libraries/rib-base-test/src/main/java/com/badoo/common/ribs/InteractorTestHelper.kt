package com.badoo.common.ribs

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.view.RibView
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.ObservableSource
import io.reactivex.Observer
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when` as whenever

class InteractorTestHelper<View : RibView>(
    val interactor: Interactor<*, View>,
    val viewFactory: ((ViewGroup) -> View?)? = null
) {
    private val buildParams = BuildParams.Empty()

    var nodeCreator: () -> Node<View> = {
        Node(
            buildParams = buildParams,
            viewFactory = viewFactory,
            plugins = listOf(interactor)
        )
    }

    fun moveToStateAndCheck(state: Lifecycle.State, block: (Node<View>) -> Unit) {
        when (state) {
            Lifecycle.State.DESTROYED,
            Lifecycle.State.INITIALIZED -> throw IllegalArgumentException("Unsupported state: $state")
            Lifecycle.State.CREATED -> toAttachViewState(block)
            Lifecycle.State.STARTED -> toStartState(block)
            Lifecycle.State.RESUMED -> toResumeState(block)
        }
    }

    private fun toResumeState(block: (Node<View>) -> Unit) {
        toStartState {
            it.onResume()
            block(it)
            it.onPause()
        }
    }

    private fun toStartState(block: (Node<View>) -> Unit) {
        toAttachViewState {
            it.onStart()
            block(it)
            it.onStop()
        }
    }

    private fun toAttachViewState(block: (Node<View>) -> Unit) {
        val node = nodeCreator()
        node.onAttach()
        node.attachToView(mock(ViewGroup::class.java))
        block(node)
        node.detachFromView()
        node.onDetach()
    }

    companion object {
        inline fun <reified View, ViewEvent> create(
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
    }
}

inline fun <reified RView, ViewEvent> Relay<ViewEvent>.subscribedView(): RView where RView : RibView, RView : ObservableSource<ViewEvent> =
    mock(RView::class.java).apply {
        whenever(this.androidView).thenReturn(mock(ViewGroup::class.java))
        whenever(this.subscribe(any())).thenAnswer {
            val observer = it.getArgument<Observer<ViewEvent>>(0)
            this@subscribedView.subscribe(observer)
        }
    }
