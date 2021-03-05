package com.badoo.ribs.test.util.ribs.child.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.test.emptyBuildParams
import com.badoo.ribs.test.util.ribs.TestNode
import com.badoo.ribs.test.util.ribs.child.TestChildInteractor
import com.badoo.ribs.test.util.ribs.child.TestChildRouter
import com.badoo.ribs.test.util.ribs.child.TestChildView
import com.badoo.ribs.test.util.ribs.child.TestChildViewImpl

class TestChildBuilder : SimpleBuilder<TestNode<TestChildView>>() {

    override fun build(buildParams: BuildParams<Nothing?>): TestNode<TestChildView> {
        val router = TestChildRouter(buildParams = emptyBuildParams())

        return TestNode(
            buildParams = buildParams,
            viewFactory = {
                ViewFactory {
                    TestChildViewImpl(it.parent.context)
                }
            },
            router = router,
            interactor = TestChildInteractor(buildParams)
        )
    }
}
