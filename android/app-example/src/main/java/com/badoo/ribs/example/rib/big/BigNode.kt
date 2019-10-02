package com.badoo.ribs.example.rib.big

import com.badoo.ribs.core.BuildContext
import android.view.ViewGroup
import com.badoo.ribs.core.Node

class BigNode(
    buildContext: BuildContext.Resolved<*>,
    viewFactory: ((ViewGroup) -> BigView?)?,
    private val router: BigRouter,
    private val interactor: BigInteractor
) : Node<BigView>(
    buildContext = buildContext,
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
), Big.Workflow
