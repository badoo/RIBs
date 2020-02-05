package com.badoo.ribs.example.rib.small

import android.view.ViewGroup
import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.core.Node

class SmallNode(
    buildContext: BuildContext<Nothing?>,
    viewFactory: ((ViewGroup) -> SmallView?)?,
    private val router: SmallRouter,
    private val interactor: SmallInteractor
) : Node<SmallView>(
    buildContext = buildContext,
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
), Small.Workflow {

}
