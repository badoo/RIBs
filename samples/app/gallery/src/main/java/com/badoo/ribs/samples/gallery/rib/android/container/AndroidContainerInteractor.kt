package com.badoo.ribs.samples.gallery.rib.android.container

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.ribs.clienthelper.childaware.childAware
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.samples.gallery.rib.android.container.routing.AndroidContainerRouter.Configuration
import com.badoo.ribs.samples.gallery.rib.android.picker.AndroidPicker
import io.reactivex.functions.Consumer

internal class AndroidContainerInteractor(
    buildParams: BuildParams<*>,
    private val backStack: BackStack<Configuration>,
) : Interactor<AndroidContainer, AndroidContainerView>(
    buildParams = buildParams
) {

    override fun onCreate(nodeLifecycle: Lifecycle) {
        childAware(nodeLifecycle) {
            whenChildAttached<AndroidPicker> { commonLifecycle, picker ->
                commonLifecycle.createDestroy {
                    bind(picker.output to Consumer { onPickerOutput(it) })
                }
            }
        }
    }

    private fun onPickerOutput(output: AndroidPicker.Output) {
        val configuration: Configuration = when (output) {
            AndroidPicker.Output.ActivitiesExampleSelected -> Configuration.AcitivityExample
            AndroidPicker.Output.DialogsExampleSelected -> Configuration.DialogExample
            AndroidPicker.Output.PermissionsExampleSelected -> Configuration.PermissionsExample
        }

        backStack.push(configuration)
    }
}
