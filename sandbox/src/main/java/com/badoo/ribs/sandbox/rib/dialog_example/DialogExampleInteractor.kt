package com.badoo.ribs.sandbox.rib.dialog_example

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.android.dialog.Dialog.CancellationPolicy.Cancellable
import com.badoo.ribs.android.text.Text
import com.badoo.ribs.clienthelper.interactor.BackStackInteractor
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.operation.pushOverlay
import com.badoo.ribs.sandbox.R
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleView.Event.ShowLazyDialogClicked
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleView.Event.ShowRibDialogClicked
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleView.Event.ShowSimpleDialogClicked
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleView.Event.ShowThemedDialogClicked
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleView.ViewModel
import com.badoo.ribs.sandbox.rib.dialog_example.dialog.Dialogs
import com.badoo.ribs.sandbox.rib.dialog_example.routing.DialogExampleRouter.Configuration
import com.badoo.ribs.sandbox.rib.dialog_example.routing.DialogExampleRouter.Configuration.Content
import com.badoo.ribs.sandbox.rib.dialog_example.routing.DialogExampleRouter.Configuration.Overlay
import com.badoo.ribs.sandbox.rib.lorem_ipsum.LoremIpsum
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.functions.Consumer

class DialogExampleInteractor internal constructor(
    buildParams: BuildParams<Nothing?>,
    private val dialogs: Dialogs
) : BackStackInteractor<DialogExample, DialogExampleView, Configuration>(
    buildParams = buildParams,
    initialConfiguration = Content.Default
) {

    private val dummyViewInput = BehaviorRelay.createDefault(
        ViewModel("Dialog examples")
    )

    override fun onViewCreated(view: DialogExampleView, viewLifecycle: Lifecycle) {
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
                is LoremIpsum -> bind(child.output to loremIpsumOutputConsumer)
            }
        }
    }

    private val viewEventConsumer: Consumer<DialogExampleView.Event> = Consumer {
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
            message = Text.Plain("Lazy dialog message")
            buttons {
                neutral(Text.Plain("Lazy neutral button"), Dialog.Event.Neutral)
            }
            cancellationPolicy = Cancellable(
                event = Dialog.Event.Cancelled,
                cancelOnTouchOutside = true
            )
        }
    }

    private val dialogEventConsumer : Consumer<Dialog.Event> = Consumer {
        when (it) {
            Dialog.Event.Positive -> {
                dummyViewInput.accept(ViewModel("Dialog - Positive clicked"))
            }
            Dialog.Event.Negative -> {
                dummyViewInput.accept(ViewModel("Dialog - Negative clicked"))
            }
            Dialog.Event.Neutral -> {
                dummyViewInput.accept(ViewModel("Dialog - Neutral clicked"))
            }
            Dialog.Event.Cancelled ->{
                dummyViewInput.accept(ViewModel("Dialog - Cancelled"))
            }
        }
    }

    private val loremIpsumOutputConsumer : Consumer<LoremIpsum.Output> = Consumer {
        dummyViewInput.accept(ViewModel("Button in Lorem Ipsum RIB clicked"))
        backStack.popBackStack()
    }
}
