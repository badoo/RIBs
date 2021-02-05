package com.badoo.ribs.samples.dialogs.rib

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.android.text.Text
import com.badoo.ribs.clienthelper.interactor.BackStackInteractor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.operation.pushOverlay
import com.badoo.ribs.samples.dialogs.R
import com.badoo.ribs.samples.dialogs.dialogtypes.DialogTypes
import com.badoo.ribs.samples.dialogs.dummy.Dummy
import com.badoo.ribs.samples.dialogs.dummy.Dummy.Output
import com.badoo.ribs.samples.dialogs.rib.DialogsRouter.Configuration
import com.badoo.ribs.samples.dialogs.rib.DialogsRouter.Configuration.Content
import com.badoo.ribs.samples.dialogs.rib.DialogsRouter.Configuration.Overlay
import com.badoo.ribs.samples.dialogs.rib.DialogsView.Event.ShowThemedDialogClicked
import com.badoo.ribs.samples.dialogs.rib.DialogsView.Event.ShowSimpleDialogClicked
import com.badoo.ribs.samples.dialogs.rib.DialogsView.Event.ShowLazyDialogClicked
import com.badoo.ribs.samples.dialogs.rib.DialogsView.Event.ShowRibDialogClicked
import com.badoo.ribs.samples.dialogs.rib.DialogsView.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.functions.Consumer

class DialogsInteractor internal constructor(
        buildParams: BuildParams<Nothing?>,
        private val dialogs: DialogTypes
) : BackStackInteractor<Dialogs, DialogsView, Configuration>(
        buildParams = buildParams,
        initialConfiguration = Content.Default
) {

    private val dummyViewInput = BehaviorRelay.createDefault(
            ViewModel("Dialog examples")
    )

    override fun onViewCreated(view: DialogsView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(dummyViewInput to view)
            bind(view to viewEventConsumer)
            bind(dialogs.themedDialog to dialogEventConsumer)
            bind(dialogs.simpleDialog to dialogEventConsumer)
            bind(dialogs.lazyDialog to dialogEventConsumer)
            bind(dialogs.ribDialog to dialogEventConsumer)
        }
    }

    override fun onChildBuilt(child: Node<*>) {
        child.lifecycle.createDestroy {
            when (child) {
                is Dummy -> bind(child.output to dummyOutputConsumer)
            }
        }
    }

    private val viewEventConsumer: Consumer<DialogsView.Event> = Consumer {
        when (it) {
            ShowThemedDialogClicked -> backStack.pushOverlay(Overlay.ThemedDialog)
            ShowSimpleDialogClicked -> backStack.pushOverlay(Overlay.SimpleDialog)
            ShowLazyDialogClicked -> {
                initLazyDialog()
                backStack.pushOverlay(Overlay.LazyDialog)
            }
            ShowRibDialogClicked -> backStack.pushOverlay(Overlay.RibDialog)
        }
    }

    private fun initLazyDialog() {
        with(dialogs.lazyDialog) {
            title = Text.Resource(R.string.lazy_dialog_title)
            message = Text.Resource(R.string.dialog_text)
            buttons {
                neutral(Text.Resource(R.string.dialog_neutral_button), Dialog.Event.Neutral)
            }
            cancellationPolicy = Dialog.CancellationPolicy.Cancellable(
                    event = Dialog.Event.Cancelled,
                    cancelOnTouchOutside = true
            )
        }
    }

    private val dialogEventConsumer: Consumer<Dialog.Event> = Consumer {
        when (it) {
            Dialog.Event.Positive -> dummyViewInput.accept(ViewModel("Dialog - Positive clicked"))
            Dialog.Event.Negative -> dummyViewInput.accept(ViewModel("Dialog - Negative clicked"))
            Dialog.Event.Neutral -> dummyViewInput.accept(ViewModel("Dialog - Neutral clicked"))
            Dialog.Event.Cancelled -> dummyViewInput.accept(ViewModel("Dialog - Cancelled"))
        }
    }

    private val dummyOutputConsumer: Consumer<Output> = Consumer {
        dummyViewInput.accept(ViewModel("Button Dummy RIB clicked"))
        backStack.popBackStack()
    }
}
