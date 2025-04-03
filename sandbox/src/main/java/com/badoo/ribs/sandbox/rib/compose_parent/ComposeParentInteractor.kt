package com.badoo.ribs.sandbox.rib.compose_parent

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.sandbox.rib.compose_parent.routing.ComposeParentRouter
import com.badoo.ribs.sandbox.rib.compose_parent.routing.ComposeParentRouter.Configuration.Content
import io.reactivex.rxjava3.functions.Consumer

internal class ComposeParentInteractor(
    buildParams: BuildParams<*>,
    private val backStack: BackStack<ComposeParentRouter.Configuration>
) : Interactor<ComposeParent, ComposeParentView>(
    buildParams = buildParams
) {

    private val viewEventConsumer: Consumer<ComposeParentView.Event> = Consumer {
        when (it) {
            ComposeParentView.Event.NextClicked -> backStack.push(
                when (val ac = backStack.activeConfiguration) {
                    is Content.ComposeLeaf -> Content.ComposeLeaf(ac.i + 1)
                }
            )
        }
    }

    override fun onViewCreated(view: ComposeParentView, viewLifecycle: Lifecycle) {
        viewLifecycle.createDestroy {
            bind(view to viewEventConsumer)
        }
    }
}
