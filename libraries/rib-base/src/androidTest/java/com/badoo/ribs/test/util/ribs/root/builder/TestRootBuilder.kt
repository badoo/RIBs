package com.badoo.ribs.test.util.ribs.root.builder

import com.badoo.ribs.builder.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.test.util.ribs.TestNode
import com.badoo.ribs.test.util.ribs.root.TestRoot
import com.badoo.ribs.test.util.ribs.root.TestRootInteractor
import com.badoo.ribs.test.util.ribs.root.TestRootView
import com.badoo.ribs.test.util.ribs.root.TestRootViewImpl

class TestRootBuilder(
    private val dependency: TestRoot.Dependency
) : Builder<TestRootBuilder.Params, Node<TestRootView>>() {

    class Params(
        val addEditText: Boolean = false
    )

    override fun build(buildParams: BuildParams<Params>): Node<TestRootView> {
        return TestNode(
            buildParams = buildParams,
            viewFactory = {
                ViewFactory {
                    TestRootViewImpl(it.parent.context)
                }
            },
            router = dependency.router,
            interactor = TestRootInteractor(
                buildParams = buildParams,
                viewLifecycleObserver = dependency.viewLifecycleObserver
            )
        )
    }
}
