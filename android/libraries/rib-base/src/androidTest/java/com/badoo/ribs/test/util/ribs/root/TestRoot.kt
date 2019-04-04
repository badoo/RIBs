package com.badoo.ribs.test.util.ribs.root

import android.arch.lifecycle.Lifecycle
import com.badoo.ribs.core.Rib
import com.badoo.ribs.test.util.LifecycleObserver
import com.badoo.ribs.test.util.ribs.TestNode
import com.badoo.ribs.test.util.ribs.child.builder.TestChildBuilder
import com.badoo.ribs.test.util.ribs.root.builder.TestRootBuilder
import io.reactivex.observers.TestObserver

interface TestRoot : Rib {

    interface Dependency {
        fun viewLifecycleObserver(): TestObserver<Lifecycle.Event>
        fun nodeLifecycleObserver(): TestObserver<Lifecycle.Event>
        fun router(): TestRootRouter
    }

    class Provider(
        val viewLifecycleObserver: LifecycleObserver = LifecycleObserver(),
        val nodeLifecycleObserver: LifecycleObserver = LifecycleObserver(),
        val childNode1: TestNode<*> = TestChildBuilder().build(),
        val childNode2: TestNode<*> = TestChildBuilder().build()
    ) {
        val router = TestRootRouter(
            builder1 = { childNode1 },
            builder2 = { childNode2 }
        )

        operator fun invoke(): TestNode<TestRootView> =
            TestRootBuilder(
                object : TestRoot.Dependency {
                    override fun viewLifecycleObserver() = viewLifecycleObserver
                    override fun nodeLifecycleObserver() = nodeLifecycleObserver
                    override fun router() = router

                }
            ).build() as TestNode<TestRootView>
    }
}
