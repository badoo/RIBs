package com.badoo.ribs.test.util.ribs.child.builder

import com.badoo.ribs.builder.Builder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.test.emptyBuildParams
import com.badoo.ribs.test.util.ribs.TestNode
import com.badoo.ribs.test.util.ribs.child.TestChildInteractor
import com.badoo.ribs.test.util.ribs.child.TestChildRouter
import com.badoo.ribs.test.util.ribs.child.TestChildView
import com.badoo.ribs.test.util.ribs.child.TestChildViewImpl

class TestChildBuilder : Builder<TestChildBuilder.Params, TestNode<TestChildView>>() {

    class Params(
        val addEditText: Boolean = false
    )

    override fun build(buildParams: BuildParams<Params>): TestNode<TestChildView> {
        val router = TestChildRouter(buildParams = emptyBuildParams())

        return TestNode(
            buildParams = buildParams,
            viewFactory = {
                ViewFactory {
                    TestChildViewImpl(
                        context = it.parent.context,
                        addEditText = buildParams.payload.addEditText
                    )
                }
            },
            router = router,
            interactor = TestChildInteractor(buildParams)
        )
    }
}
