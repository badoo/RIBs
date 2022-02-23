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
import com.badoo.ribs.util.RIBs
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import kotlinx.parcelize.Parcelize
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class WorkflowTest {
    private lateinit var node: RxWorkflowNode<TestView>
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
        viewFactory = mock { on { invoke(any()) } doReturn view }
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
    ): RxWorkflowNode<TestView> = RxWorkflowNode(
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
        var actionInvoked = false
        val action = { actionInvoked = true }
        val workflow: Single<Node<*>> = node.executeWorkflowInternal(action)
        val testObserver = TestObserver<Node<*>>()
        workflow.subscribe(testObserver)

        Assert.assertEquals(true, actionInvoked)
        testObserver.assertValue(node)
        testObserver.assertComplete()
    }

    @Test
    fun `executeWorkflow never executes action on lifecycle terminate before subscribe`() {
        node.onDestroy(isRecreating = false)

        var actionInvoked = false
        val action = { actionInvoked = true }
        val workflow: Single<Node<*>> = node.executeWorkflowInternal(action)
        val testObserver = TestObserver<Node<*>>()
        workflow.subscribe(testObserver)

        Assert.assertEquals(false, actionInvoked)
        testObserver.assertNever(node)
        testObserver.assertNotComplete()
    }

    @Test
    fun `attachWorkflow executes action on subscribe`() {
        var actionInvoked = false
        val action = { actionInvoked = true }
        val workflow: Single<TestNode> = node.attachWorkflowInternal(action)
        val testObserver = TestObserver<TestNode>()
        workflow.subscribe(testObserver)

        Assert.assertEquals(true, actionInvoked)
        testObserver.assertValue(child3)
        testObserver.assertComplete()
    }

    @Test
    fun `attachWorkflow never executes action on lifecycle terminate before subscribe`() {
        node.onDestroy(isRecreating = false)

        var actionInvoked = false
        val action = { actionInvoked = true }
        val workflow: Single<TestNode> = node.attachWorkflowInternal(action)
        val testObserver = TestObserver<TestNode>()
        workflow.subscribe(testObserver)

        Assert.assertEquals(false, actionInvoked)
        testObserver.assertNever(child1)
        testObserver.assertNever(child2)
        testObserver.assertNever(child3)
        testObserver.assertNotComplete()
    }

    @Test
    fun `waitForChildAttached emits expected child immediately if it's already attached`() {
        val workflow: Single<TestNode2> = node.waitForChildAttachedInternal()
        val testObserver = TestObserver<TestNode2>()
        val testChildNode = TestNode2(buildParams = testBuildParams(ancestryInfo = childAncestry))

        node.attachChildNode(testChildNode)
        workflow.subscribe(testObserver)

        testObserver.assertValue(testChildNode)
        testObserver.assertComplete()
    }

    @Test
    fun `waitForChildAttached never executes action on lifecycle terminate before subscribe`() {
        node.onDestroy(isRecreating = false)

        val workflow: Single<TestNode2> = node.waitForChildAttachedInternal()
        val testObserver = TestObserver<TestNode2>()
        workflow.subscribe(testObserver)

        testObserver.assertNoValues()
        testObserver.assertNotComplete()
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
