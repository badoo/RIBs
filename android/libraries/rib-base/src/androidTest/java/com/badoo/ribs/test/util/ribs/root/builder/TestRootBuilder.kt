package com.badoo.ribs.test.util.ribs.root.builder

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.test.util.ribs.TestNode
import com.badoo.ribs.test.util.ribs.child.TestChildView
import com.badoo.ribs.test.util.ribs.child.TestChildViewImpl
import com.badoo.ribs.test.util.ribs.root.TestRoot
import com.badoo.ribs.test.util.ribs.root.TestRootInteractor
import com.badoo.ribs.test.util.ribs.root.TestRootView
import com.badoo.ribs.test.util.ribs.root.TestRootViewImpl

class TestRootBuilder(override val dependency: TestRoot.Dependency) : Builder<TestRoot.Dependency>() {

    fun build(savedInstanceState: Bundle?): Node<TestRootView> {
        return TestNode(
            savedInstanceState = savedInstanceState,
            identifier = object: TestRoot { },
            viewFactory = object : ViewFactory<Nothing?, TestRootView> {
                override fun invoke(deps: Nothing?): (ViewGroup) -> TestRootView = {
                    TestRootViewImpl(it.context)
                }
            },
            router = dependency.router(),
            interactor = TestRootInteractor(
                savedInstanceState = savedInstanceState,
                router = dependency.router(),
                viewLifecycleObserver = dependency.viewLifecycleObserver()
            )
        )
    }
}
