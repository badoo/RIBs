package com.badoo.ribs.samples.gallery.rib.routing.routing_container

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.ribs.clienthelper.childaware.childAware
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.samples.gallery.rib.routing.routing_container.routing.RoutingContainerRouter.Configuration
import com.badoo.ribs.samples.gallery.rib.routing.routing_picker.RoutingPicker
import io.reactivex.functions.Consumer

internal class RoutingContainerInteractor(
    buildParams: BuildParams<*>,
    private val backStack: BackStack<Configuration>,
) : Interactor<RoutingContainer, RoutingContainerView>(
    buildParams = buildParams
) {

    override fun onCreate(nodeLifecycle: Lifecycle) {
        childAware(nodeLifecycle) {
            whenChildAttached<RoutingPicker> { commonLifecycle, picker ->
                commonLifecycle.createDestroy {
                    bind(picker.output to Consumer { onPickerOutput(it) })
                }
            }
        }
    }

    private fun onPickerOutput(output: RoutingPicker.Output) {
        val configuration: Configuration = when (output) {
            RoutingPicker.Output.SimpleRoutingSelected -> Configuration.SimpleRoutingExample
            RoutingPicker.Output.BackStackSelected -> Configuration.BackStackExample
            RoutingPicker.Output.TransitionsSelected -> Configuration.TransitionsExample
        }

        backStack.push(configuration)
    }
}
