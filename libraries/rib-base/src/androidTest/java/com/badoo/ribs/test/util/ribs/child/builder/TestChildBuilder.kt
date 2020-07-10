package com.badoo.ribs.test.util.ribs.child.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.test.util.ribs.TestNode
import com.badoo.ribs.test.util.ribs.child.TestChildInteractor
import com.badoo.ribs.test.util.ribs.child.TestChildRouter
import com.badoo.ribs.test.util.ribs.child.TestChildView
import com.badoo.ribs.test.util.ribs.child.TestChildViewImpl

class TestChildBuilder : SimpleBuilder<TestNode<TestChildView>>() {

    private val dependency: Nothing? = null

    override fun build(buildParams: BuildParams<Nothing?>): TestNode<TestChildView> {
        val router = TestChildRouter(BuildParams.Empty())

        return TestNode(
            buildParams = buildParams,
            viewFactory = object : ViewFactory<Nothing?, TestChildView> {
                override fun invoke(deps: Nothing?): (RibView) -> TestChildView = {
                    TestChildViewImpl(it.context)
                }
            },
            router = router,
            interactor = TestChildInteractor(buildParams)
        )
    }
}
