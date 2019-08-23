package com.badoo.ribs.example.rib.big

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.core.Node

class BigNode(
    savedInstanceState: Bundle?,
    viewFactory: ((ViewGroup) -> BigView?)?,
    private val router: BigRouter,
    private val interactor: BigInteractor
) : Node<BigView>(
    savedInstanceState = savedInstanceState,
    identifier = object : Big {},
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
), Big.Workflow
