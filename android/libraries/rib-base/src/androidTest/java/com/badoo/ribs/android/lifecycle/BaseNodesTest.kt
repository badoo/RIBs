package com.badoo.ribs.android.lifecycle

import androidx.test.espresso.Espresso
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.android.lifecycle.helper.ExpectedState
import com.badoo.ribs.android.lifecycle.helper.NodeState
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.builder.BuildContext
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature
import com.badoo.ribs.core.routing.configuration.feature.operation.push
import com.badoo.ribs.core.routing.configuration.feature.operation.pushOverlay
import com.badoo.ribs.test.util.ribs.TestNode
import com.badoo.ribs.test.util.ribs.root.TestRoot
import com.badoo.ribs.test.util.ribs.root.TestRootRouter
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Rule
import java.util.UUID

abstract class BaseNodesTest {

    @get:Rule
    val ribsRule = RibsRule()

    data class When(
        val permanentParts: List<TestRootRouter.Configuration.Permanent> = emptyList(),
        val initialConfiguration: TestRootRouter.Configuration.Content = TestRootRouter.Configuration.Content.NoOp,
        val pushConfiguration1: TestRootRouter.Configuration? = null,
        val pushConfiguration2: TestRootRouter.Configuration? = null
    )

    protected fun test(
        setup: When,
        expectedState: ExpectedState,
        testBlock: (BackStackFeature<TestRootRouter.Configuration>, TestNode<*>) -> Unit
    ) {
        val rootProvider = TestRoot.Provider(
            initialConfiguration = setup.initialConfiguration,
            permanentParts = setup.permanentParts
        )

        var backStack: BackStackFeature<TestRootRouter.Configuration>? = null

        ribsRule.start { activity, savedInstanceState ->
            val buildParams = BuildParams(
                payload = null,
                buildContext = BuildContext.root(savedInstanceState),
                identifier = Rib.Identifier(
                    uuid = UUID.randomUUID()
                )
            )

            // SameThreadVerifier will check if it was created on the same thread it will be used on
            backStack = BackStackFeature(
                buildParams = buildParams,
                initialConfiguration = setup.initialConfiguration
            )

            rootProvider.create(
                buildParams = buildParams,
                dialogLauncher = activity.dialogLauncher(),
                savedInstanceState = savedInstanceState,
                routingSource = backStack!!
            )
        }

        testBlock.invoke(backStack!!, rootProvider.rootNode!!)
        rootProvider.makeAssertions(expectedState)
    }

    protected fun BackStackFeature<TestRootRouter.Configuration>.pushIt(configuration: TestRootRouter.Configuration) {
        when (configuration) {
            is TestRootRouter.Configuration.Content -> push(configuration)
            is TestRootRouter.Configuration.Overlay -> pushOverlay(configuration)
        }
    }

    private fun TestRoot.Provider.makeAssertions(expected: ExpectedState) {
        Espresso.onIdle()
        permanentNode1?.let {
            assertThat(it.toNodeState()).describedAs("Permanent node 1 state")
                .isEqualTo(expected.permanentNode1)
        }
        permanentNode2?.let {
            assertThat(it.toNodeState()).describedAs("Permanent node 2 state")
                .isEqualTo(expected.permanentNode2)
        }
        childNode1?.let {
            assertThat(it.toNodeState()).describedAs("Child node 1 state").isEqualTo(expected.node1)
        }
        childNode2?.let {
            assertThat(it.toNodeState()).describedAs("Child node 2 state").isEqualTo(expected.node2)
        }
        childNode3?.let {
            assertThat(it.toNodeState()).describedAs("Child node 3 state").isEqualTo(expected.node3)
        }
    }

    private fun TestNode<*>.toNodeState() =
        NodeState(
            attached = isAttached,
            viewAttached = isAttachedToView,
            ribLifeCycleState = lifecycleManager.ribLifecycle.lifecycle.currentState,
            viewLifeCycleState = lifecycleManager.viewLifecycle?.lifecycle?.currentState
        )
}
