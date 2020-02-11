package com.badoo.ribs.test.util.ribs.child.builder

import android.view.ViewGroup
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.Builder
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.test.util.ribs.TestNode
import com.badoo.ribs.test.util.ribs.child.TestChildInteractor
import com.badoo.ribs.test.util.ribs.child.TestChildRouter
import com.badoo.ribs.test.util.ribs.child.TestChildView
import com.badoo.ribs.test.util.ribs.child.TestChildViewImpl

class TestChildBuilder : Builder<Nothing?, TestNode<TestChildView>>(
    rib = object : Rib {}
) {

    override val dependency: Nothing? = null

    override fun build(buildParams: BuildParams<Nothing?>): TestNode<TestChildView> {
        val router = TestChildRouter(BuildParams.Empty())

        return TestNode(
            buildParams = buildParams,
            viewFactory = object : ViewFactory<Nothing?, TestChildView> {
                override fun invoke(deps: Nothing?): (ViewGroup) -> TestChildView = {
                    TestChildViewImpl(it.context)
                }
            },
            router = router,
            interactor = TestChildInteractor(buildParams)
        )
    }
}
