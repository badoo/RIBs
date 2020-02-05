package com.badoo.ribs.example.rib.big

import androidx.lifecycle.Lifecycle
import com.badoo.ribs.core.BuildContext
import com.badoo.ribs.core.Interactor
import com.badoo.ribs.core.Router
import com.badoo.ribs.example.rib.big.BigRouter.Configuration
import com.badoo.ribs.example.rib.big.BigRouter.Configuration.Content
import com.badoo.ribs.example.rib.big.BigRouter.Configuration.Overlay

class BigInteractor(
    buildContext: BuildContext<Nothing?>,
    router: Router<Configuration, *, Content, Overlay, BigView>
) : Interactor<Configuration, Content, Overlay, BigView>(
    buildContext = buildContext,
    router = router,
    disposables = null
) {

    override fun onViewCreated(view: BigView, viewLifecycle: Lifecycle) {
        view.accept(BigView.ViewModel("My id: " + id.replace("${BigInteractor::class.java.name}.", "")))
    }
}
