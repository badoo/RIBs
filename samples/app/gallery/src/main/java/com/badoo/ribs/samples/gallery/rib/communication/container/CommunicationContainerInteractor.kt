package com.badoo.ribs.samples.gallery.rib.communication.container

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.ribs.clienthelper.childaware.childAware
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.samples.gallery.rib.communication.container.routing.CommunicationContainerRouter.Configuration
import com.badoo.ribs.samples.gallery.rib.communication.picker.CommunicationPicker
import io.reactivex.functions.Consumer

internal class CommunicationContainerInteractor(
    buildParams: BuildParams<*>,
    private val backStack: BackStack<Configuration>,
) : Interactor<CommunicationContainer, CommunicationContainerView>(
    buildParams = buildParams
) {

    override fun onCreate(nodeLifecycle: Lifecycle) {
        childAware(nodeLifecycle) {
            whenChildAttached<CommunicationPicker> { commonLifecycle, picker ->
                commonLifecycle.createDestroy {
                    bind(picker.output to Consumer { onPickerOutput(it) })
                }
            }
        }
    }

    private fun onPickerOutput(output: CommunicationPicker.Output) {
        val configuration: Configuration = when (output) {
            CommunicationPicker.Output.MenuExampleSelected -> Configuration.MenuExample
            CommunicationPicker.Output.CoordinateMultipleSelected -> Configuration.CoordinateMultipleExample
        }

        backStack.push(configuration)
    }
}
