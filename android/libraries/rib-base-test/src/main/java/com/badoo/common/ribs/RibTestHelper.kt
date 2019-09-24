package com.badoo.common.ribs

import android.os.Parcelable
import android.view.ViewGroup
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.Router
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.view.RibView
import com.jakewharton.rxrelay2.Relay
import io.reactivex.ObservableSource
import io.reactivex.Observer
import kotlinx.android.parcel.Parcelize
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class RibTestHelper<View : RibView>(
    val interactor: Interactor<*, *, *, View>,
    val viewFactory: ((ViewGroup) -> View?)? = null,
    router: Router<*, *, *, *, View>? = null
) {

    val router: Router<*, *, *, *, View> = router ?: TestRouter.createTestRouter()

    var nodeCreator: () -> Node<View> = {
        Node(
            savedInstanceState = null,
            identifier = TestIdentifier,
            viewFactory = viewFactory,
            router = this.router,
            interactor = interactor
        )
    }

    fun onResumed(block: (Node<View>) -> Unit) {
        onStarted {
            it.onResume()
            block(it)
            it.onPause()
        }
    }

    fun onStarted(block: (Node<View>) -> Unit) {
        onViewCreated {
            it.onStart()
            block(it)
            it.onStop()
        }
    }

    fun onViewCreated(block: (Node<View>) -> Unit) {
        val node = nodeCreator()
        node.onAttach()
        node.attachToView(mock(ViewGroup::class.java))
        block(node)
        node.detachFromView()
        node.onDetach()
    }

    companion object {
        inline fun <reified View, ViewEvent> create(
            interactor: Interactor<*, *, *, View>,
            viewEventRelay: Relay<ViewEvent>,
            router: Router<*, *, *, *, View>? = null
        ): RibTestHelper<View> where View : RibView, View : ObservableSource<ViewEvent> {
            val view: View = viewEventRelay.subscribedView()
            return RibTestHelper(interactor, { view }, router)
        }
    }
}

inline fun <reified RView, ViewEvent> Relay<ViewEvent>.subscribedView(): RView where RView : RibView, RView : ObservableSource<ViewEvent> =
    mock(RView::class.java).apply {
        `when`(this.androidView).thenReturn(mock(ViewGroup::class.java))
        `when`(this.subscribe(ArgumentMatchers.any())).thenAnswer {
            val observer = it.getArgument<Observer<ViewEvent>>(0)
            this@subscribedView.subscribe(observer)
        }
    }

private object TestIdentifier : Rib

private class TestRouter<C : Parcelable, Permanent : C, Content : C, Overlay : C, V : RibView>(
    initialConfig: Content
) : Router<C, Permanent, Content, Overlay, V>(null, initialConfig) {

    var resolveConfiguration: (C) -> RoutingAction<V> = { RoutingAction.noop() }

    override fun resolveConfiguration(configuration: C): RoutingAction<V> =
        resolveConfiguration.invoke(configuration)

    companion object {
        fun <V : RibView> createTestRouter() =
            TestRouter<TestConfiguration, TestConfiguration, TestConfiguration, TestConfiguration, V>(TestConfiguration)
    }
}

@Parcelize
private object TestConfiguration : Parcelable
