package com.badoo.ribs.sandbox.rib.dialog_example

import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.builder.BuildParams

class DialogExampleNode(
    buildParams: BuildParams<*>,
    viewFactory: ((ViewGroup) -> DialogExampleView?)?,
    router: DialogExampleRouter,
    interactor: DialogExampleInteractor
) : Node<DialogExampleView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
), DialogExample
