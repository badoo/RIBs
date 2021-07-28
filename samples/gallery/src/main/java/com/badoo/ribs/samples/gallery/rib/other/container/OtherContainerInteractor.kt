package com.badoo.ribs.samples.gallery.rib.other.container

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.ribs.clienthelper.childaware.childAware
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.samples.gallery.rib.other.container.routing.OtherContainerRouter.Configuration
import com.badoo.ribs.samples.gallery.rib.other.picker.OtherPicker
import io.reactivex.functions.Consumer

internal class OtherContainerInteractor(
    buildParams: BuildParams<*>,
    private val backStack: BackStack<Configuration>,
) : Interactor<OtherContainer, OtherContainerView>(
    buildParams = buildParams
) {

    override fun onCreate(nodeLifecycle: Lifecycle) {
        childAware(nodeLifecycle) {
            whenChildAttached<OtherPicker> { commonLifecycle, picker ->
                commonLifecycle.createDestroy {
                    bind(picker.output to Consumer { onPickerOutput(it) })
                }
            }
        }
    }

    private fun onPickerOutput(output: OtherPicker.Output) {
        val configuration: Configuration = when (output) {
            OtherPicker.Output.RetainedInstanceStoreSelected -> TODO()
        }

        backStack.push(configuration)
    }
}
