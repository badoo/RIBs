package com.badoo.ribs.test.util.ribs.child.builder

import android.view.ViewGroup
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.test.util.ribs.TestNode
import com.badoo.ribs.test.util.ribs.child.TestChildInteractor
import com.badoo.ribs.test.util.ribs.child.TestChildRouter
import com.badoo.ribs.test.util.ribs.child.TestChildView
import com.badoo.ribs.test.util.ribs.child.TestChildViewImpl
import com.badoo.ribs.test.util.ribs.root.TestRoot

class TestChildBuilder {
    val router = TestChildRouter()

    fun build(): TestNode<TestChildView> {
        return TestNode(
            identifier = object: TestRoot { },
            viewFactory = object : ViewFactory<Nothing?, TestChildView> {
                override fun invoke(deps: Nothing?): (ViewGroup) -> TestChildView = {
                    TestChildViewImpl(it.context)
                }
            },
            router = router,
            interactor = TestChildInteractor(router)
        )
    }
}
