package com.badoo.ribs.samples.back_stack.rib.back_stack_example

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.newRoot
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.routing.source.backstack.operation.pushOverlay
import com.badoo.ribs.routing.source.backstack.operation.replace
import com.badoo.ribs.routing.source.backstack.operation.singleTop
import com.badoo.ribs.samples.back_stack.rib.back_stack_example.BackStackExampleView.Event.Child
import com.badoo.ribs.samples.back_stack.rib.back_stack_example.BackStackExampleView.Event.Content
import com.badoo.ribs.samples.back_stack.rib.back_stack_example.BackStackExampleView.Event.Overlay
import com.badoo.ribs.samples.back_stack.rib.back_stack_example.routing.BackStackExampleRouter.Configuration

interface BackStackExamplePresenter {

    fun handle(event: BackStackExampleView.Event)

}

internal class BackStackExamplePresenterImpl(
    private val backStack: BackStack<Configuration>
) : ViewAware<BackStackExampleView>,
    BackStackExamplePresenter {

    private var view: BackStackExampleView? = null

    override fun onViewCreated(view: BackStackExampleView, viewLifecycle: Lifecycle) {
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

    override fun handle(event: BackStackExampleView.Event) {
        when (event) {
            is Content.Pop -> backStack.popBackStack()
            is Content.Push -> backStack.push(event.child.toConfiguration())
            is Content.Replace -> backStack.replace(event.child.toConfiguration())
            is Content.NewRoot -> backStack.newRoot(event.child.toConfiguration())
            is Content.SingleTop -> backStack.singleTop(event.child.toConfiguration())
            is Overlay.Pop -> backStack.popOverlay()
            is Overlay.Push -> backStack.pushOverlay(event.child.toConfiguration())
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

    private fun Child.toConfiguration(): Configuration = when (this) {
        Child.A -> Configuration.Content.A
        Child.B -> Configuration.Content.B
        Child.C -> Configuration.Content.C
        Child.D -> Configuration.Content.D
        Child.E -> Configuration.Overlay.E
        Child.F -> Configuration.Overlay.F
    }

}
