package com.samples.back_stack.parent

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.newRoot
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.routing.source.backstack.operation.pushOverlay
import com.badoo.ribs.routing.source.backstack.operation.replace
import com.badoo.ribs.routing.source.backstack.operation.singleTop
import com.samples.back_stack.parent.routing.ParentRouter

typealias ViewEventContent = ParentView.Event.RoutingContent.Content
typealias ViewEventOverlay = ParentView.Event.RoutingOverlay.Overlay

typealias RouterConfigurationContentA = ParentRouter.Configuration.Content.A
typealias RouterConfigurationContentB = ParentRouter.Configuration.Content.B
typealias RouterConfigurationContentC = ParentRouter.Configuration.Content.C
typealias RouterConfigurationContentD = ParentRouter.Configuration.Content.D
typealias RouterConfigurationOverlayE = ParentRouter.Configuration.Overlay.E
typealias RouterConfigurationOverlayF = ParentRouter.Configuration.Overlay.F

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
            is ParentView.Event.RoutingContent.Pop -> backStack.popBackStack()
            is ParentView.Event.RoutingContent.Push -> backStack.push(event.content.mapToRouterContent())
            is ParentView.Event.RoutingContent.Replace -> backStack.replace(event.content.mapToRouterContent())
            is ParentView.Event.RoutingContent.NewRoot -> backStack.newRoot(event.content.mapToRouterContent())
            is ParentView.Event.RoutingContent.SingleTop -> backStack.singleTop(event.content.mapToRouterContent())

            is ParentView.Event.RoutingOverlay.Pop -> backStack.popOverlay()
            is ParentView.Event.RoutingOverlay.Push -> backStack.pushOverlay(event.overlay.mapToRouterOverlay())
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

    private fun ViewEventContent.mapToRouterContent(): ParentRouter.Configuration = when (this) {
        ViewEventContent.A -> RouterConfigurationContentA
        ViewEventContent.B -> RouterConfigurationContentB
        ViewEventContent.C -> RouterConfigurationContentC
        ViewEventContent.D -> RouterConfigurationContentD
    }

    private fun ViewEventOverlay.mapToRouterOverlay(): ParentRouter.Configuration = when (this) {
        ViewEventOverlay.E -> RouterConfigurationOverlayE
        ViewEventOverlay.F -> RouterConfigurationOverlayF
    }

}