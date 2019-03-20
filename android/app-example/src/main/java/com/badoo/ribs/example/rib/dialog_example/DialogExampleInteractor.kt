package com.badoo.ribs.example.rib.dialog_example

import android.arch.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.dialog.Dialog
import com.badoo.ribs.example.rib.dialog_example.DialogExampleRouter.Configuration
import com.badoo.ribs.example.rib.dialog_example.DialogExampleView.Event.ShowRibDialogClicked
import com.badoo.ribs.example.rib.dialog_example.DialogExampleView.Event.ShowSimpleDialogClicked
import com.badoo.ribs.example.rib.dialog_example.DialogExampleView.ViewModel
import com.badoo.ribs.example.rib.dialog_example.dialog.RibDialog
import com.badoo.ribs.example.rib.dialog_example.dialog.SimpleDialog
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsum
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.functions.Consumer

class DialogExampleInteractor(
    router: Router<Configuration, DialogExampleView>,
    private val simpleDialog: SimpleDialog,
    private val ribDialog: RibDialog
) : Interactor<Configuration, DialogExampleView>(
    router = router,
    disposables = null
) {

    private val dummyViewInput = BehaviorRelay.createDefault(
        ViewModel("Dialog examples")
    )

    override fun onViewCreated(view: DialogExampleView, viewLifecycle: Lifecycle) {
        viewLifecycle.createDestroy {
            bind(dummyViewInput to view)
            bind(view to viewEventConsumer)
            bind(simpleDialog to dialogEventConsumer)
            bind(ribDialog to dialogEventConsumer)
        }
    }

    private val viewEventConsumer: Consumer<DialogExampleView.Event> = Consumer {
        when (it) {
            ShowSimpleDialogClicked -> router.push(Configuration.SimpleDialog)
            ShowRibDialogClicked -> router.push(Configuration.RibDialog)
        }
    }

    private val dialogEventConsumer : Consumer<Dialog.Event> = Consumer {
        when (it) {
            Dialog.Event.Positive -> {
                // As dialogs are Router configuration based, the configuration needs to change
                // Options here:
                // 1. go back to previous configuration by popping, or
                // 2. replace current one with a different one
                // Pushing new is not recommended, since going back will show the dialog again in that case.
                dummyViewInput.accept(ViewModel("Dialog - Positive clicked"))
                router.popBackStack()
            }
            Dialog.Event.Negative -> {
                dummyViewInput.accept(ViewModel("Dialog - Negative clicked"))
                router.popBackStack()
            }
            Dialog.Event.Neutral -> {
                dummyViewInput.accept(ViewModel("Dialog - Neutral clicked"))
                router.popBackStack()
            }
            Dialog.Event.Cancelled ->{
                dummyViewInput.accept(ViewModel("Dialog - Cancelled"))
                router.popBackStack()
            }
        }
    }

    internal val loremIpsumOutputConsumer : Consumer<LoremIpsum.Output> = Consumer {
        dummyViewInput.accept(ViewModel("Button in Lorem Ipsum RIB clicked"))
        router.popBackStack()
    }
}
