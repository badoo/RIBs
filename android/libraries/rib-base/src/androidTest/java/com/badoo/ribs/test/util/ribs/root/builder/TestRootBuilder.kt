package com.badoo.ribs.test.util.ribs.root.builder

import com.badoo.ribs.core.builder.BuildParams
import android.view.ViewGroup
import com.badoo.ribs.core.builder.SimpleBuilder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.test.util.ribs.TestNode
import com.badoo.ribs.test.util.ribs.root.TestRoot
import com.badoo.ribs.test.util.ribs.root.TestRootInteractor
import com.badoo.ribs.test.util.ribs.root.TestRootView
import com.badoo.ribs.test.util.ribs.root.TestRootViewImpl

class TestRootBuilder(
    private val dependency: TestRoot.Dependency
) : SimpleBuilder<Node<TestRootView>>(object : TestRoot {}) {

    override fun build(buildParams: BuildParams<Nothing?>): Node<TestRootView> {
        return TestNode(
            buildParams = buildParams,
            viewFactory = object : ViewFactory<Nothing?, TestRootView> {
                override fun invoke(deps: Nothing?): (ViewGroup) -> TestRootView = {
                    TestRootViewImpl(it.context)
                }
            },
            router = dependency.router(),
            interactor = TestRootInteractor(
                buildParams = buildParams,
                viewLifecycleObserver = dependency.viewLifecycleObserver()
            )
        )
    }
}
