package com.badoo.ribs.example.rib.small

import android.view.ViewGroup
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.Node

class SmallNode(
    buildParams: BuildParams<Nothing?>,
    viewFactory: ((ViewGroup) -> SmallView?)?,
    private val router: SmallRouter,
    private val interactor: SmallInteractor
) : Node<SmallView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
), Small.Workflow {

}
