package com.badoo.ribs.android.lifecycle

import androidx.test.espresso.Espresso
import com.badoo.ribs.android.lifecycle.helper.ExpectedState
import com.badoo.ribs.android.lifecycle.helper.NodeState
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.push
import com.badoo.ribs.routing.source.backstack.operation.pushOverlay
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.routing.transition.handler.defaultDuration
import com.badoo.ribs.test.RibsRule
import com.badoo.ribs.test.util.ribs.TestNode
import com.badoo.ribs.test.util.ribs.root.TestRoot
import com.badoo.ribs.test.util.ribs.root.TestRootRouter
import com.badoo.ribs.test.util.ribs.root.builder.TestRootBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import java.util.UUID

abstract class BaseNodesTest {

    @get:Rule
    val ribsRule = RibsRule<Rib>()

    protected open val transitionHandler: TransitionHandler<TestRootRouter.Configuration>? = null

    data class When(
        val permanentParts: List<TestRootRouter.Configuration.Permanent> = emptyList(),
        val initialConfiguration: TestRootRouter.Configuration.Content = TestRootRouter.Configuration.Content.NoOp,
        val configuration1: TestRootRouter.Configuration? = null,
        val configuration2: TestRootRouter.Configuration? = null
    )

    @SuppressWarnings("LongMethod")
    protected fun test(
        setup: When,
        expectedState: ExpectedState,
        testBlock: (BackStack<TestRootRouter.Configuration>, TestNode<*>) -> Unit
    ) {
        val rootProvider = TestRoot.Provider(
            initialConfiguration = setup.initialConfiguration,
            permanentParts = setup.permanentParts
        )

        var backStack: BackStack<TestRootRouter.Configuration>? = null

        ribsRule.start { activity, savedInstanceState ->
            val buildParams = BuildParams<TestRootBuilder.Params>(
                payload = TestRootBuilder.Params(false),
                buildContext = BuildContext.root(savedInstanceState),
                identifier = Rib.Identifier(
                    uuid = UUID.randomUUID()
                )
            )

            // SameThreadVerifier will check if it was created on the same thread it will be used on
            backStack = BackStack(
                buildParams = buildParams,
                initialConfiguration = setup.initialConfiguration
            )

            rootProvider.create(
                buildParams = buildParams,
                dialogLauncher = activity.integrationPoint.dialogLauncher, // TODO reconsider if we need direct dependency at all
                savedInstanceState = savedInstanceState,
                routingSource = backStack!!,
                transitionHandler = transitionHandler
            )
        }

        testBlock.invoke(backStack!!, rootProvider.rootNode!!)
        rootProvider.makeAssertions(expectedState)
    }

    protected fun BackStack<TestRootRouter.Configuration>.pushIt(configuration: TestRootRouter.Configuration) {
        when (configuration) {
            is TestRootRouter.Configuration.Content -> push(configuration)
            is TestRootRouter.Configuration.Overlay -> pushOverlay(configuration)
        }
    }

    private fun TestRoot.Provider.makeAssertions(expected: ExpectedState) {
        if (transitionHandler != null) Thread.sleep(defaultDuration * 2) // wait for animations
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
