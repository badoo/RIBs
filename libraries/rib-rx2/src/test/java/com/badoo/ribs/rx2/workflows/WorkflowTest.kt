package com.badoo.ribs.rx2.workflows

import android.os.Parcelable
import android.view.ViewGroup
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.customisation.RibCustomisationDirectoryImpl
import com.badoo.ribs.core.modality.AncestryInfo
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.rx2.workflows.RxWorkflowNode.NodeIsNotAvailableForWorkflowException
import com.badoo.ribs.util.RIBs
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Maybe
import io.reactivex.Single
import kotlinx.parcelize.Parcelize
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WorkflowTest {
    private lateinit var node: TestRxWorkflowNode<TestView>
    private lateinit var view: TestView
    private lateinit var androidView: ViewGroup
    private lateinit var parentView: RibView
    private lateinit var viewFactory: ViewFactory<TestView>
    private lateinit var child1: TestNode
    private lateinit var child2: TestNode
    private lateinit var child3: TestNode
    private lateinit var childAncestry: AncestryInfo

    @BeforeEach
    fun setUp() {
        parentView = mock()
        androidView = mock()
        view = mock { on { androidView }.thenReturn(androidView) }
        viewFactory = mock { on { invoke(argThat { parent == parentView }) } doReturn view }
        node = createNode(viewFactory = viewFactory)
        childAncestry = AncestryInfo.Child(node, Routing(AnyConfiguration))

        addChildren()
    }

    @AfterEach
    fun tearDown() {
        RIBs.clearErrorHandler()
    }

    private fun createNode(
        buildParams: BuildParams<Nothing?> = testBuildParams(),
        viewFactory: ViewFactory<TestView> = this.viewFactory,
        plugins: List<Plugin> = emptyList()
    ): TestRxWorkflowNode<TestView> = TestRxWorkflowNode(
        buildParams = buildParams,
        viewFactory = viewFactory,
        plugins = plugins
    )

    private fun addChildren() {
        child1 = TestNode(
            buildParams = testBuildParams(
                ancestryInfo = childAncestry
            ),
            viewFactory = null
        )
        child2 = TestNode(
            buildParams = testBuildParams(
                ancestryInfo = childAncestry
            ),
            viewFactory = null
        )
        child3 = TestNode(
            buildParams = testBuildParams(
                ancestryInfo = childAncestry
            ),
            viewFactory = null
        )
        node.attachChildNode(child1)
        node.attachChildNode(child2)
        node.attachChildNode(child3)
    }

    @Test
    fun `executeWorkflow executes action on subscribe`() {
        val action = InvokableStub()
        val workflow: Single<Node<*>> = node.executeWorkflowInternal(action)
        val testObserver = workflow.test()

        action.assertInvoked()
        testObserver.assertResult(node)
    }

    @Test
    fun `executeWorkflow never executes action on lifecycle terminate before subscribe`() {
        node.onDestroy(isRecreating = false)

        val action = InvokableStub()
        val workflow: Single<Node<*>> = node.executeWorkflowInternal(action)
        val testObserver = workflow.test()

        action.assertNotInvoked()
        testObserver.assertFailure(NodeIsNotAvailableForWorkflowException::class.java)
    }

    @Test
    fun `executeWorkflow does not interrupt further actions on lifecycle terminate`() {
        val action = InvokableStub()
        val workflow: Single<Any> =
            node
                .executeWorkflowInternal<Node<*>>(action)
                .flatMap { Single.never<Any>() }
        val testObserver = workflow.test()

        node.onDestroy(isRecreating = false)

        action.assertInvoked()
        testObserver.assertNoValues()
        testObserver.assertNotTerminated()
    }

    @Test
    fun `maybeExecuteWorkflow never executes action on lifecycle terminate before subscribe`() {
        node.onDestroy(isRecreating = false)

        val action = InvokableStub()
        val workflow: Maybe<Node<*>> = node.maybeExecuteWorkflowInternal(action)
        val testObserver = workflow.test()

        action.assertNotInvoked()
        testObserver.assertResult()
    }

    @Test
    fun `attachWorkflow executes action on subscribe`() {
        val action = InvokableStub()
        val workflow: Single<TestNode> = node.attachWorkflowInternal(action)
        val testObserver = workflow.test()

        action.assertInvoked()
        testObserver.assertResult(child3)
    }

    @Test
    fun `attachWorkflow never executes action on lifecycle terminate before subscribe`() {
        node.onDestroy(isRecreating = false)

        val action = InvokableStub()
        val workflow: Single<TestNode> = node.attachWorkflowInternal(action)
        val testObserver = workflow.test()

        action.assertNotInvoked()
        testObserver.assertFailure(NodeIsNotAvailableForWorkflowException::class.java)
    }

    @Test
    fun `attachWorkflow does not interrupt further actions on lifecycle terminate`() {
        val action = InvokableStub()
        val workflow: Single<Any> =
            node
                .attachWorkflowInternal<Node<*>>(action)
                .flatMap { Single.never<Any>() }
        val testObserver = workflow.test()

        node.onDestroy(isRecreating = false)

        action.assertInvoked()
        testObserver.assertNoValues()
        testObserver.assertNotTerminated()
    }

    @Test
    fun `maybeAttachWorkflow never executes action on lifecycle terminate before subscribe`() {
        node.onDestroy(isRecreating = false)

        val action = InvokableStub()
        val workflow: Maybe<TestNode> = node.maybeAttachWorkflowInternal(action)
        val testObserver = workflow.test()

        action.assertNotInvoked()
        testObserver.assertResult()
    }

    @Test
    fun `waitForChildAttached emits expected child immediately if it's already attached`() {
        val testChildNode = TestNode2(buildParams = testBuildParams(ancestryInfo = childAncestry))

        node.attachChildNode(testChildNode)
        val workflow: Single<TestNode2> = node.waitForChildAttachedInternal()
        val testObserver = workflow.test()

        testObserver.assertResult(testChildNode)
    }

    @Test
    fun `waitForChildAttached emits expected child after it is attached`() {
        val testChildNode = TestNode2(buildParams = testBuildParams(ancestryInfo = childAncestry))
        val workflow: Single<TestNode2> = node.waitForChildAttachedInternal()
        val testObserver = workflow.test()

        node.attachChildNode(testChildNode)

        testObserver.assertResult(testChildNode)
    }

    @Test
    fun `waitForChildAttached never executes action on lifecycle terminate before subscribe`() {
        node.onDestroy(isRecreating = false)

        val workflow: Single<TestNode2> = node.waitForChildAttachedInternal()
        val testObserver = workflow.test()

        testObserver.assertFailure(NodeIsNotAvailableForWorkflowException::class.java)
    }

    @Test
    fun `waitForChildAttached does not interrupt further actions on lifecycle terminate`() {
        val workflow: Single<Any> =
            node
                .waitForChildAttachedInternal<Node<*>>()
                .flatMap { Single.never<Any>() }
        val testObserver = workflow.test()

        node.onDestroy(isRecreating = false)

        testObserver.assertNoValues()
        testObserver.assertNotTerminated()
    }

    @Test
    fun `maybeWaitForChildAttached never executes action on lifecycle terminate before subscribe`() {
        node.onDestroy(isRecreating = false)

        val workflow: Maybe<TestNode2> = node.maybeWaitForChildAttachedInternal()
        val testObserver = workflow.test()

        testObserver.assertResult()
    }

    private class InvokableStub : () -> Unit {
        var isInvoked: Boolean = false

        override fun invoke() {
            isInvoked = true
        }

        fun assertInvoked() {
            Assert.assertEquals("Expected to be invoked", true, isInvoked)
        }

        fun assertNotInvoked() {
            Assert.assertEquals("Expected to be not invoked", false, isInvoked)
        }
    }

    private class TestNode(
        buildParams: BuildParams<*> = testBuildParams(),
        viewFactory: ViewFactory<TestView>? = TestViewFactory(),
        router: Router<*> = mock(),
        plugins: List<Plugin> = emptyList()
    ) : Node<TestView>(
        buildParams = buildParams,
        viewFactory = viewFactory,
        plugins = plugins + listOf(router)
    )

    private class TestNode2(
        buildParams: BuildParams<*> = testBuildParams(),
        viewFactory: ViewFactory<TestView>? = TestViewFactory(),
        router: Router<*> = mock(),
        plugins: List<Plugin> = emptyList()
    ) : Node<TestView>(
        buildParams = buildParams,
        viewFactory = viewFactory,
        plugins = plugins + listOf(router)
    )

    private class TestRxWorkflowNode<V : RibView>(
        buildParams: BuildParams<*>,
        viewFactory: ViewFactory<V>?,
        plugins: List<Plugin>,
    ) : RxWorkflowNode<V>(buildParams, viewFactory, plugins) {
        inline fun <reified T : Any> waitForChildAttachedInternal(): Single<T> =
            waitForChildAttached()

        inline fun <reified T : Any> maybeWaitForChildAttachedInternal(): Maybe<T> =
            maybeWaitForChildAttached()

        inline fun <reified T : Any> executeWorkflowInternal(
            noinline action: () -> Unit
        ): Single<T> = executeWorkflow(action)

        inline fun <reified T : Any> maybeExecuteWorkflowInternal(
            noinline action: () -> Unit
        ): Maybe<T> = maybeExecuteWorkflow(action)

        inline fun <reified T : Any> attachWorkflowInternal(
            noinline action: () -> Unit
        ): Single<T> = attachWorkflow(action)

        inline fun <reified T : Any> maybeAttachWorkflowInternal(
            noinline action: () -> Unit
        ): Maybe<T> = maybeAttachWorkflow(action)

    }

    private class TestView : AndroidRibView() {
        override val androidView: ViewGroup = mock()
    }

    private class TestViewFactory : ViewFactory<TestView> {
        override fun invoke(context: ViewFactory.Context): TestView = TestView()
    }

    @Parcelize
    private object AnyConfiguration : Parcelable {
        override fun toString(): String = "AnyConfiguration"
    }

    companion object {
        private fun testBuildParams(ancestryInfo: AncestryInfo? = null) =
            BuildParams(
                null,
                ancestryInfo?.let {
                    BuildContext(
                        ancestryInfo,
                        savedInstanceState = null,
                        customisations = RibCustomisationDirectoryImpl()
                    )
                } ?: BuildContext.root(null)
            )
    }
}
