package com.badoo.ribs.android.lifecycle

import android.support.test.espresso.Espresso
import com.badoo.common.ribs.RibsRule
import com.badoo.ribs.android.lifecycle.helper.ExpectedState
import com.badoo.ribs.test.util.ribs.root.TestRoot
import com.badoo.ribs.test.util.ribs.root.TestRootRouter
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Rule

abstract class BaseNodesTest {

    @get:Rule
    val ribsRule = RibsRule()

    data class When(
        val permanentParts: List<TestRootRouter.Configuration.Permanent> = emptyList(),
        val initialConfiguration: TestRootRouter.Configuration.Content = TestRootRouter.Configuration.Content.NoOp,
        val pushConfiguration1: TestRootRouter.Configuration? = null,
        val pushConfiguration2: TestRootRouter.Configuration? = null
    )

    protected fun test(setup: When, expectedState: ExpectedState, testBlock: (TestRootRouter) -> Unit) {
        val rootProvider = TestRoot.Provider(
            initialConfiguration = setup.initialConfiguration,
            permanentParts = setup.permanentParts
        )

        ribsRule.start { rootProvider.create(it.dialogLauncher()) }

        val router: TestRootRouter = rootProvider.rootNode?.getRouter() as TestRootRouter

        testBlock.invoke(router)

        rootProvider.makeAssertions(expectedState)
    }

    protected fun TestRootRouter.pushIt(configuration: TestRootRouter.Configuration) {
        when (configuration) {
            is TestRootRouter.Configuration.Content -> push(configuration)
            is TestRootRouter.Configuration.Overlay -> pushOverlay(configuration)
        }
    }

    private fun TestRoot.Provider.makeAssertions(expected: ExpectedState) {
        Espresso.onIdle()
        assertThat(permanentNode1?.isAttached).describedAs("is Permanent 1 Attached").isEqualTo(expected.permanentNode1?.attached)
        assertThat(permanentNode1?.isViewAttached).describedAs("is Permanent 1 View attached").isEqualTo(expected.permanentNode1?.viewAttached)
        assertThat(permanentNode2?.isAttached).describedAs("is Permanent 2 Attached").isEqualTo(expected.permanentNode2?.attached)
        assertThat(permanentNode2?.isViewAttached).describedAs("is Permanent 2 View attached").isEqualTo(expected.permanentNode2?.viewAttached)
        assertThat(childNode1?.isAttached).describedAs("is Child 1 Attached").isEqualTo(expected.node1?.attached)
        assertThat(childNode1?.isViewAttached).describedAs("is Child 1 View attached").isEqualTo(expected.node1?.viewAttached)
        assertThat(childNode2?.isAttached).describedAs("is Child 2 Attached").isEqualTo(expected.node2?.attached)
        assertThat(childNode2?.isViewAttached).describedAs("is Child 2 View attached").isEqualTo(expected.node2?.viewAttached)
        assertThat(childNode3?.isAttached).describedAs("is Child 3 Attached").isEqualTo(expected.node3?.attached)
        assertThat(childNode3?.isViewAttached).describedAs("is Child 3 View attached").isEqualTo(expected.node3?.viewAttached)
    }
}
