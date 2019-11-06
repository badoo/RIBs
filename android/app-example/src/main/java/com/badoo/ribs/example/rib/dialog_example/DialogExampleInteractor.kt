package com.badoo.ribs.example.rib.dialog_example

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.ribs.android.Text
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.dialog.Dialog
import com.badoo.ribs.dialog.Dialog.CancellationPolicy.Cancellable
import com.badoo.ribs.example.rib.dialog_example.DialogExampleRouter.Configuration
import com.badoo.ribs.example.rib.dialog_example.DialogExampleRouter.Configuration.Content
import com.badoo.ribs.example.rib.dialog_example.DialogExampleRouter.Configuration.Overlay
import com.badoo.ribs.example.rib.dialog_example.DialogExampleView.Event.ShowLazyDialog
import com.badoo.ribs.example.rib.dialog_example.DialogExampleView.Event.ShowRibDialogClicked
import com.badoo.ribs.example.rib.dialog_example.DialogExampleView.Event.ShowSimpleDialogClicked
import com.badoo.ribs.example.rib.dialog_example.DialogExampleView.ViewModel
import com.badoo.ribs.example.rib.dialog_example.dialog.LazyDialog
import com.badoo.ribs.example.rib.dialog_example.dialog.RibDialog
import com.badoo.ribs.example.rib.dialog_example.dialog.SimpleDialog
import com.badoo.ribs.example.rib.lorem_ipsum.LoremIpsum
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.functions.Consumer

class DialogExampleInteractor(
    savedInstanceState: Bundle?,
    router: Router<Configuration, Nothing, Content, Overlay, DialogExampleView>,
    private val simpleDialog: SimpleDialog,
    private val lazyDialog: LazyDialog,
    private val ribDialog: RibDialog
) : Interactor<Configuration, Content, Overlay, DialogExampleView>(
    savedInstanceState = savedInstanceState,
    router = router,
    disposables = null
) {

    private val dummyViewInput = BehaviorRelay.createDefault(
        ViewModel("Dialog examples")
    )

    override fun onViewCreated(view: DialogExampleView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(dummyViewInput to view)
            bind(view to viewEventConsumer)
            bind(simpleDialog to dialogEventConsumer)
            bind(lazyDialog to dialogEventConsumer)
            bind(ribDialog to dialogEventConsumer)
        }
    }

    private val viewEventConsumer: Consumer<DialogExampleView.Event> = Consumer {
        when (it) {
            ShowSimpleDialogClicked -> router.pushOverlay(Overlay.SimpleDialog)
            ShowLazyDialog -> {
                initLazyDialog()
                router.pushOverlay(Overlay.LazyDialog)
            }

            ShowRibDialogClicked -> router.pushOverlay(Overlay.RibDialog)
        }
    }

    private fun initLazyDialog() {
        with(lazyDialog) {
            title = Text.Plain("Lazy dialog title")
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

    internal val loremIpsumOutputConsumer : Consumer<LoremIpsum.Output> = Consumer {
        dummyViewInput.accept(ViewModel("Button in Lorem Ipsum RIB clicked"))
        router.popBackStack()
    }
}
