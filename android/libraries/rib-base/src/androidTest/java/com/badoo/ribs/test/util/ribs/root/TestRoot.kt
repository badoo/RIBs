package com.badoo.ribs.test.util.ribs.root

import android.arch.lifecycle.Lifecycle
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.dialog.DialogLauncher
import com.badoo.ribs.test.util.LifecycleObserver
import com.badoo.ribs.test.util.NoOpDialogLauncher
import com.badoo.ribs.test.util.ribs.TestNode
import com.badoo.ribs.test.util.ribs.child.builder.TestChildBuilder
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration
import com.badoo.ribs.test.util.ribs.root.builder.TestRootBuilder
import io.reactivex.observers.TestObserver

interface TestRoot : Rib {

    interface Dependency {
        fun viewLifecycleObserver(): TestObserver<Lifecycle.Event>
        fun nodeLifecycleObserver(): TestObserver<Lifecycle.Event>
        fun router(): TestRootRouter
    }

    class Provider(
        private val initialConfiguration: Configuration.Content = Configuration.Content.NoOp,
        private val permanentParts: List<Configuration.Permanent> = emptyList(),
        private val dialogLauncher: DialogLauncher = NoOpDialogLauncher()
    ) {

        val viewLifecycleObserver: LifecycleObserver = LifecycleObserver()
        val nodeLifecycleObserver: LifecycleObserver = LifecycleObserver()

        var childNode1: TestNode<*>? = null
            private set
        var childNode2: TestNode<*>? = null
            private set
        var rootNode: TestNode<*>? = null
            private set

        val childNode1Builder: () -> TestNode<*> = {
            val node = TestChildBuilder().build()
            childNode1 = node
            node
        }

        val childNode2Builder: () -> TestNode<*> = {
            val node = TestChildBuilder().build()
            childNode2 = node
            node
        }

        operator fun invoke(): TestNode<TestRootView> {
            val router = TestRootRouter(
                builder1 = childNode1Builder,
                builder2 = childNode2Builder,
                initialConfiguration = initialConfiguration,
                permanentParts = permanentParts,
                dialogLauncher = dialogLauncher
            )

            val node = TestRootBuilder(
                object : Dependency {
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
