package com.badoo.ribs.test.util.ribs.root.builder

import com.badoo.ribs.core.BuildContext
import android.view.ViewGroup
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.test.util.ribs.TestNode
import com.badoo.ribs.test.util.ribs.root.TestRoot
import com.badoo.ribs.test.util.ribs.root.TestRootInteractor
import com.badoo.ribs.test.util.ribs.root.TestRootView
import com.badoo.ribs.test.util.ribs.root.TestRootViewImpl

class TestRootBuilder(
    override val dependency: TestRoot.Dependency
) : Builder<TestRoot.Dependency, Nothing?, Node<TestRootView>>() {

    override fun build(params: BuildContext.ParamsWithData<Nothing?>): Node<TestRootView> {
        return TestNode(
            buildContext = resolve(object : TestRoot {}, params),
            viewFactory = object : ViewFactory<Nothing?, TestRootView> {
                override fun invoke(deps: Nothing?): (ViewGroup) -> TestRootView = {
                    TestRootViewImpl(it.context)
                }
            },
            router = dependency.router(),
            interactor = TestRootInteractor(
                buildContext = resolve(object : TestRoot {}, params),
                router = dependency.router(),
                viewLifecycleObserver = dependency.viewLifecycleObserver()
            )
        )
    }
}
