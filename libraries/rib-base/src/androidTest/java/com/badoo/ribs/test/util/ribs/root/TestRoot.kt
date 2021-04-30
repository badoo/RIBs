package com.badoo.ribs.test.util.ribs.root

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.dialog.DialogLauncher
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.source.impl.Empty
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.test.util.LifecycleObserver
import com.badoo.ribs.test.util.ribs.TestNode
import com.badoo.ribs.test.util.ribs.child.TestChildView
import com.badoo.ribs.test.util.ribs.child.builder.TestChildBuilder
import com.badoo.ribs.test.util.ribs.root.TestRootRouter.Configuration
import com.badoo.ribs.test.util.ribs.root.builder.TestRootBuilder
import io.reactivex.observers.TestObserver

interface TestRoot : Rib {

    interface Dependency {
        val viewLifecycleObserver: TestObserver<Lifecycle.Event>
        val nodeLifecycleObserver: TestObserver<Lifecycle.Event>
        val router: TestRootRouter
    }

    class Provider(
        private val initialConfiguration: Configuration.Content = Configuration.Content.NoOp,
        private val permanentParts: List<Configuration.Permanent> = emptyList()
    ) {

        val viewLifecycleObserver: LifecycleObserver = LifecycleObserver()
        val nodeLifecycleObserver: LifecycleObserver = LifecycleObserver()

        var permanentNode1: TestNode<*>? = null
            private set
        var permanentNode2: TestNode<*>? = null
            private set
        var childNode1: TestNode<*>? = null
            private set
        var childNode2: TestNode<*>? = null
            private set
        var childNode3: TestNode<*>? = null
            private set
        var rootNode: TestNode<*>? = null
            private set

        private fun builder(
            addEditText: Boolean,
            block: (TestNode<TestChildView>) -> Unit
        ): (BuildContext) -> TestNode<TestChildView> = {
            TestChildBuilder().build(it, TestChildBuilder.Params(addEditText)).also {
                block.invoke(it)
            }
        }

        fun create(
            buildParams: BuildParams<TestRootBuilder.Params>,
            dialogLauncher: DialogLauncher,
            savedInstanceState: Bundle?,
            routingSource: RoutingSource<Configuration> = Empty(),
            transitionHandler: TransitionHandler<Configuration>? = null
        ): TestNode<TestRootView> {
            val addEditText: Boolean = buildParams.payload.addEditText
            val router = TestRootRouter(
                buildParams = buildParams,
                routingSource = routingSource,
                builderPermanent1 = builder(addEditText) { permanentNode1 = it },
                builderPermanent2 = builder(addEditText) { permanentNode2 = it },
                builder1 = builder(addEditText) { childNode1 = it },
                builder2 = builder(addEditText) { childNode2 = it },
                builder3 = builder(addEditText) { childNode3 = it },
                permanentParts = permanentParts,
                dialogLauncher = dialogLauncher,
                transitionHandler = transitionHandler
            )

            val node = TestRootBuilder(
                object : Dependency {
                    override val viewLifecycleObserver = this@Provider.viewLifecycleObserver
                    override val nodeLifecycleObserver = this@Provider.nodeLifecycleObserver
                    override val router = router

                }
            ).build(
                BuildContext.root(savedInstanceState),
                buildParams.payload
            ) as TestNode<TestRootView>
            rootNode = node
            return node
        }
    }
}
