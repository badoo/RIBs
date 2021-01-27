package com.badoo.ribs.samples.back_stack.rib.parent

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.newRoot
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.routing.source.backstack.operation.pushOverlay
import com.badoo.ribs.routing.source.backstack.operation.replace
import com.badoo.ribs.routing.source.backstack.operation.singleTop
import com.badoo.ribs.samples.back_stack.rib.parent.ParentView.Event.ContentAction
import com.badoo.ribs.samples.back_stack.rib.parent.ParentView.Event.OverlayAction
import com.badoo.ribs.samples.back_stack.rib.parent.routing.ParentRouter
import com.badoo.ribs.samples.back_stack.rib.parent.routing.ParentRouter.Configuration.Content
import com.badoo.ribs.samples.back_stack.rib.parent.routing.ParentRouter.Configuration.Overlay

typealias ViewEventContent = ContentAction.Content
typealias ViewEventOverlay = OverlayAction.Overlay

interface ParentPresenter {

    fun onEventClicked(event: ParentView.Event)

}

internal class ParentPresenterImpl(
    private val backStack: BackStack<ParentRouter.Configuration>
) : ViewAware<ParentView>,
    ParentPresenter {

    private var view: ParentView? = null

    override fun onViewCreated(view: ParentView, viewLifecycle: Lifecycle) {
        super.onViewCreated(view, viewLifecycle)
        viewLifecycle.subscribe(
            onCreate = {
                this.view = view
                this.view?.setBackStack(getBackStackString())
            },
            onDestroy = { this.view = null }
        )

        backStack.observe { routingHistory ->
            if (routingHistory is BackStack.State) this.view?.setBackStack(getBackStackString())
        }
    }

    override fun onEventClicked(event: ParentView.Event) {
        when (event) {
            is ContentAction.Pop -> backStack.popBackStack()
            is ContentAction.Push -> backStack.push(event.content.toConfiguration())
            is ContentAction.Replace -> backStack.replace(event.content.toConfiguration())
            is ContentAction.NewRoot -> backStack.newRoot(event.content.toConfiguration())
            is ContentAction.SingleTop -> backStack.singleTop(event.content.toConfiguration())

            is OverlayAction.Pop -> backStack.popOverlay()
            is OverlayAction.Push -> backStack.pushOverlay(event.overlay.toConfiguration())
        }
    }

    private fun getBackStackString() =
        backStack.state.elements.map {
            val overlays = it.overlays.map { overlay -> overlay.configuration::class.java.simpleName }

            if (overlays.isEmpty()) {
                it.routing.configuration::class.java.simpleName
            } else {
                it.routing.configuration::class.java.simpleName + " + " + overlays.toString().replace('[', '{').replace(']', '}')
            }
        }.toString()

    private fun ViewEventContent.toConfiguration(): ParentRouter.Configuration = when (this) {
        ViewEventContent.A -> Content.A
        ViewEventContent.B -> Content.B
        ViewEventContent.C -> Content.C
        ViewEventContent.D -> Content.D
    }

    private fun ViewEventOverlay.toConfiguration(): ParentRouter.Configuration = when (this) {
        ViewEventOverlay.E -> Overlay.E
        ViewEventOverlay.F -> Overlay.F
    }

}
