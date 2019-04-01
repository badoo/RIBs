package com.badoo.ribs.test.util.ribs.builder

import android.view.ViewGroup
import com.badoo.ribs.core.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.test.util.ribs.*

class TestRibBuilder(dependency: TestRib.Dependency) : Builder<TestRib.Dependency>(dependency) {

    fun build(): Node<TestRibView> {
        val router = TestRibRouter()
        return Node(
            identifier = object: TestRib { },
            viewFactory = object : ViewFactory<TestRibView> {
                override fun invoke(viewGroup: ViewGroup): TestRibView =
                    TestRibViewImpl(viewGroup.context)
            },
            router = router,
            interactor = TestRibInteractor(router)
        )
    }
}
