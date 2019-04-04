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
        private val childNode1Builder: () -> TestNode<*> = { TestChildBuilder().build() },
        private val childNode2Builder: () -> TestNode<*> = { TestChildBuilder().build() }
    ) {
        var childNode1: TestNode<*>? = null
            private set
        var childNode2: TestNode<*>? = null
            private set
        var rootNode: TestNode<*>? = null
            private set

        operator fun invoke(): TestNode<TestRootView> {
            val router = TestRootRouter(
                builder1 = {
                    val node = childNode1Builder()
                    childNode1 = node
                    node
                },
                builder2 = {
                    val node = childNode2Builder()
                    childNode2 = node
                    node
                }
            )

            val node = TestRootBuilder(
                object : TestRoot.Dependency {
                    override fun viewLifecycleObserver() = viewLifecycleObserver
                    override fun nodeLifecycleObserver() = nodeLifecycleObserver
                    override fun router() = router

                }
            ).build() as TestNode<TestRootView>
            rootNode = node
            return node
        }
    }
}
