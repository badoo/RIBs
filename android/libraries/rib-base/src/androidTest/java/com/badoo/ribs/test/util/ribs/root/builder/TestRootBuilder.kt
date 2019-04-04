package com.badoo.ribs.test.util.ribs.root.builder

import android.view.ViewGroup
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.test.util.ribs.TestNode
import com.badoo.ribs.test.util.ribs.root.TestRoot
import com.badoo.ribs.test.util.ribs.root.TestRootInteractor
import com.badoo.ribs.test.util.ribs.root.TestRootView
import com.badoo.ribs.test.util.ribs.root.TestRootViewImpl

class TestRootBuilder(dependency: TestRoot.Dependency) : Builder<TestRoot.Dependency>(dependency) {

    fun build(): Node<TestRootView> {
        return TestNode(
            identifier = object: TestRoot { },
            viewFactory = object : ViewFactory<TestRootView> {
                override fun invoke(viewGroup: ViewGroup): TestRootView =
                    TestRootViewImpl(viewGroup.context)
            },
            router = dependency.router(),
            interactor = TestRootInteractor(dependency.router(), dependency.viewLifecycleObserver())
        )
    }
}
