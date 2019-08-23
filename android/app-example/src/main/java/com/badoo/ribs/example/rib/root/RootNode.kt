package com.badoo.ribs.example.rib.root

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.example.rib.root.RootRouter.Configuration.Content
import com.badoo.ribs.example.rib.switcher.Switcher
import io.reactivex.Single

class RootNode(
    savedInstanceState: Bundle?,
    viewFactory: ((ViewGroup) -> RootView?)?,
    private val router: RootRouter,
    private val interactor: RootInteractor
) : Node<RootView>(
    savedInstanceState = savedInstanceState,
    identifier = object : Root {},
    viewFactory = viewFactory,
    router = router,
    interactor = interactor
), Root.Workflow {

    override fun goToSwitcher(): Single<Switcher.Workflow> =
        attachWorkflow {
            router.newRoot(Content.Default) // TODO consider this
        }
}
