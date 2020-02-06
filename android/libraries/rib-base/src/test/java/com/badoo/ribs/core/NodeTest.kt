
package com.badoo.ribs.core

import android.os.Bundle
import android.os.Parcelable
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.ribs.core.Node.Companion.BUNDLE_KEY
import com.badoo.ribs.core.Node.Companion.KEY_VIEW_STATE
import com.badoo.ribs.core.helper.TestInteractor
import com.badoo.ribs.core.helper.TestNode
import com.badoo.ribs.core.helper.TestNode2
import com.badoo.ribs.core.helper.TestRouter
import com.badoo.ribs.core.helper.TestView
import com.badoo.ribs.core.helper.testBuildParams
import com.badoo.ribs.core.view.ViewPlugin
import com.badoo.ribs.util.RIBs
import com.jakewharton.rxrelay2.PublishRelay
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.isA
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.observers.TestObserver
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NodeTest {

    interface RandomOtherNode1 : Rib
    interface RandomOtherNode2 : Rib
    interface RandomOtherNode3 : Rib

    interface TestViewFactory : (ViewGroup) -> TestView

    private lateinit var node: Node<TestView>
    private lateinit var view: TestView
    private lateinit var androidView: ViewGroup
    private lateinit var parentViewGroup: ViewGroup
    private lateinit var someViewGroup1: ViewGroup
    private lateinit var someViewGroup2: ViewGroup
    private lateinit var someViewGroup3: ViewGroup
    private lateinit var viewFactory: TestViewFactory
    private lateinit var router: Router<TestRouter.Configuration, Nothing, TestRouter.Configuration, Nothing, TestView>
    private lateinit var interactor: Interactor<TestRouter.Configuration, TestRouter.Configuration, Nothing, TestView>
    private lateinit var child1: TestNode
    private lateinit var child2: TestNode
    private lateinit var child3: TestNode
    private lateinit var allChildren: List<Node<*>>
    private lateinit var viewPlugins: Set<ViewPlugin>

    @Before
    fun setUp() {
        parentViewGroup = mock()
        someViewGroup1 = mock()
        someViewGroup2 = mock()
        someViewGroup3 = mock()
        androidView = mock()
        view = mock { on { androidView }.thenReturn(androidView) }
        viewFactory = mock { on { invoke(parentViewGroup) } doReturn view }
        router = mock()
        interactor = mock()
        viewPlugins = setOf(mock(), mock())
        node = createNode(viewFactory = viewFactory)

        addChildren()
    }

    private fun createNode(
        buildParams: BuildParams<Nothing?> = testBuildParams(),
        viewFactory: TestViewFactory? = this@NodeTest.viewFactory,
        interactor: Interactor<TestRouter.Configuration, TestRouter.Configuration, Nothing, TestView> = this@NodeTest.interactor
    ): Node<TestView> = Node(
        buildParams = buildParams,
        viewFactory = viewFactory,
        router = router,
        interactor = interactor,
        viewPlugins = viewPlugins
    )

    @After
    fun tearDown() {
        RIBs.clearErrorHandler()
    }

    private fun addChildren() {
        child1 = TestNode(testBuildParams(object : RandomOtherNode1 {}), viewFactory = null)
        child2 = TestNode(testBuildParams(object : RandomOtherNode2 {}), viewFactory = null)
        child3 = TestNode(testBuildParams(object : RandomOtherNode3 {}), viewFactory = null)
        allChildren = listOf(child1, child2, child3)
        node.children.addAll(allChildren)
    }


    private fun attachToViewAlongWithChildren() {
        node.attachToView(parentViewGroup)
        node.attachChildView(child1)
        node.attachChildView(child2)
        node.attachChildView(child3)
    }

    @Test
    fun `Router's node initialised after Node init`() {
        verify(router).init(node)
    }

    @Test
    fun `onAttach() notifies Router`() {
        node.onAttach()
        verify(router).onAttach()
    }

    @Test
    fun `onAttach() notifies Interactor`() {
        node.onAttach()
        verify(interactor).onAttach(node.ribLifecycleRegistry)
    }

    @Test
    fun `onDetach() notifies Router`() {
        node.onDetach()
        verify(router).onDetach()
    }

    @Test
    fun `onDetach() notifies Interactor`() {
        node.onDetach()
        verify(interactor).onDetach()
    }

    @Test
    fun `onDetach() verifies view has been detached`() {
        val errorHandler = mock<RIBs.ErrorHandler>()
        RIBs.clearErrorHandler()
        RIBs.errorHandler = errorHandler
        node.attachToView(parentViewGroup)

        node.onDetach()

        verify(errorHandler).handleNonFatalError(any(), isA<RuntimeException>())
    }

    @Test
    fun `onDetach() detaches view as a fail-safe mechanism`() {
        val errorHandler = mock<RIBs.ErrorHandler>()
        RIBs.clearErrorHandler()
        RIBs.errorHandler = errorHandler
        node.attachToView(parentViewGroup)

        node.onDetach()

        assertEquals(false, node.isAttachedToView)
    }

    @Test
    fun `onSaveInstanceState() saves view state as well`() {
        node.view = view
        node.onSaveInstanceState(mock())
        verify(androidView).saveHierarchyState(node.savedViewState)
    }

    @Test
    fun `onSaveInstanceState() is forwarded to Router`() {
        node.onSaveInstanceState(mock())
        verify(router).onSaveInstanceState(any())
    }

    @Test
    fun `Router's bundle from onSaveInstanceState() call is put inside original bundle`() {
        val bundle: Bundle = mock()
        node.onSaveInstanceState(bundle)
        verify(router).onSaveInstanceState(bundle)
    }

    @Test
    fun `onSaveInstanceState() is forwarded to Interactor`() {
        node.onSaveInstanceState(mock())
        verify(interactor).onSaveInstanceState(any())
    }

    @Test
    fun `Interactor's bundle from onSaveInstanceState call is put inside original bundle`() {
        val bundle: Bundle = mock()
        node.onSaveInstanceState(bundle)
        verify(interactor).onSaveInstanceState(bundle)
    }

    @Test
    fun `onStart() is forwarded to all children`() {
        val mocks = createAndAttachChildMocks(3)
        node.onStart()
        mocks.forEach {
            verify(it).onStart()
        }
    }

    @Test
    fun `onStop() is forwarded to all children`() {
        val mocks = createAndAttachChildMocks(3)
        node.onStop()
        mocks.forEach {
            verify(it).onStop()
        }
    }

    @Test
    fun `onPause() is forwarded to all children`() {
        val mocks = createAndAttachChildMocks(3)
        node.onPause()
        mocks.forEach {
            verify(it).onPause()
        }
    }

    @Test
    fun `onResume() is forwarded to all children`() {
        val mocks = createAndAttachChildMocks(3)
        node.onResume()
        mocks.forEach {
            verify(it).onResume()
        }
    }

    @Test
    fun `Back press handling is forwarded to all children attached to the view if none can handle it`() {
        attachToViewAlongWithChildren()
        node.detachChildView(child2) // this means child2 should not even be asked
        child1.handleBackPress = false
        child2.handleBackPress = false
        child3.handleBackPress = false

        node.handleBackPress()

        assertEquals(true, child1.handleBackPressInvoked)
        assertEquals(false, child2.handleBackPressInvoked)
        assertEquals(true, child3.handleBackPressInvoked)
    }

    @Test
    fun `Back press handling is forwarded to children only until first one handles it`() {
        attachToViewAlongWithChildren()
        child1.handleBackPress = false
        child2.handleBackPress = true
        child3.handleBackPress = false

        node.handleBackPress()

        assertEquals(true, child1.handleBackPressInvoked)
        assertEquals(true, child2.handleBackPressInvoked)
        assertEquals(false, child3.handleBackPressInvoked)
    }

    @Test
    fun `Back press handling is forwarded to Interactor if no children handled it`() {
        attachToViewAlongWithChildren()
        child1.handleBackPress = false
        child2.handleBackPress = false
        child3.handleBackPress = false

        node.handleBackPress()

        verify(interactor).handleBackPress()
    }

    @Test
    fun `Back press handling is not forwarded to Interactor if any children handled it`() {
        attachToViewAlongWithChildren()
        child1.handleBackPress = false
        child2.handleBackPress = true
        child3.handleBackPress = false

        node.handleBackPress()

        verify(interactor, never()).handleBackPress()
    }

    @Test
    fun `Router back stack popping is invoked if none of the children nor the Interactor handled back press`() {
        attachToViewAlongWithChildren()
        child1.handleBackPress = false
        child2.handleBackPress = false
        child3.handleBackPress = false
        whenever(interactor.handleBackPress()).thenReturn(false)

        node.handleBackPress()

        verify(router).popBackStack()
    }

    @Test
    fun `Router back stack popping is not invoked if any of the children handled back press`() {
        attachToViewAlongWithChildren()
        child1.handleBackPress = false
        child2.handleBackPress = true
        child3.handleBackPress = false
        whenever(interactor.handleBackPress()).thenReturn(false)

        node.handleBackPress()

        verify(router, never()).popBackStack()
    }

    @Test
    fun `Router back stack popping is not invoked if Interactor handled back press`() {
        whenever(interactor.handleBackPress()).thenReturn(true)

        node.handleBackPress()

        verify(router, never()).popBackStack()
    }

    @Test
    fun `isViewAttached flag is initially false`() {
        assertEquals(false, node.isAttachedToView)
    }

    @Test
    fun `attachToView() sets isViewAttached flag to true`() {
        node.attachToView(parentViewGroup)
        assertEquals(true, node.isAttachedToView)
    }

    @Test
    fun `detachFromView() resets isViewAttached flag to false`() {
        node.attachToView(parentViewGroup)
        node.detachFromView()
        assertEquals(false, node.isAttachedToView)
    }

    @Test
    fun `attachToView() forwards call to Router`() {
        node.attachToView(parentViewGroup)
        verify(router).onAttachView()
    }

    @Test
    fun `attachToView() notifies all ViewPlugins`() {
        node.attachToView(parentViewGroup)
        viewPlugins.forEach {
            verify(it).onAttachtoView(parentViewGroup)
        }
    }

    @Test
    fun `detachFromView() notifies all ViewPlugins`() {
        node.attachToView(parentViewGroup)
        node.detachFromView()
        viewPlugins.forEach {
            verify(it).onDetachFromView(parentViewGroup)
        }
    }

    private fun createAndAttachChildMocks(n: Int, identifiers: MutableList<Rib.Identifier> = mutableListOf()): List<Node<*>> {
        if (identifiers.isEmpty()) {
            for (i in 0 until n) {
                identifiers.add(testBuildParams().identifier)
            }
        }
        val mocks = mutableListOf<Node<*>>()
        for (i in 0 until n) {
            val mockNode = mock<Node<*>>()
            whenever(mockNode.identifier).thenReturn(identifiers[i])
            mocks.add(mockNode)

        }
        node.children.clear()
        node.children.addAll(mocks)
        return mocks
    }

    @Test
    fun `attachChildView() results in children added to parentViewGroup given Router does not define something else `() {
        whenever(view.getParentViewForChild(any())).thenReturn(null)
        val mocks = createAndAttachChildMocks(3)
        node.attachToView(parentViewGroup)
        mocks.forEach {
            node.attachChildView(it)
            verify(it).attachToView(parentViewGroup)
        }
    }

    @Test
    fun `attachToView() results in children added to target defined by Router`() {
        val id1 = testBuildParams(object : RandomOtherNode1 {}).identifier
        val id2 = testBuildParams(object : RandomOtherNode2 {}).identifier
        val id3 = testBuildParams(object : RandomOtherNode3 {}).identifier
        val mockChildNodes = createAndAttachChildMocks(3, mutableListOf(id1, id2, id3))
        assertEquals(id1, mockChildNodes[0].identifier)
        assertEquals(id2, mockChildNodes[1].identifier)
        assertEquals(id3, mockChildNodes[2].identifier)
        whenever(view.getParentViewForChild(id1)).thenReturn(someViewGroup1)
        whenever(view.getParentViewForChild(id2)).thenReturn(someViewGroup2)
        whenever(view.getParentViewForChild(id3)).thenReturn(someViewGroup3)

        node.attachToView(parentViewGroup)

        mockChildNodes.forEach {
            node.attachChildView(it)
            verify(it, never()).attachToView(parentViewGroup)
        }

        verify(mockChildNodes[0]).attachToView(someViewGroup1)
        verify(mockChildNodes[1]).attachToView(someViewGroup2)
        verify(mockChildNodes[2]).attachToView(someViewGroup3)
    }

    @Test
    fun `attachChildNode() does not imply attachToView when Android view system is not available`() {
        val childViewFactory = mock<TestViewFactory>()
        val child = TestNode(
            viewFactory = childViewFactory
        )
        node.attachChildNode(child)
        verify(childViewFactory, never()).invoke(parentViewGroup)
    }

    @Test
    fun `attachChildNode() passes on current lifecycle to direct children - INITIALIZED`() {
        // by default it's not started, should be on INITIALIZED

        val child = TestNode(
            viewFactory = mock<TestViewFactory>()
        )
        node.attachChildNode(child)

        assertEquals(Lifecycle.State.CREATED, child.externalLifecycleRegistry.currentState)
    }

    @Test
    fun `attachChildNode() passes on current lifecycle to children of children - INITIALIZED`() {
        // by default it's not started, should be on INITIALIZED

        val directChild = TestNode(
            viewFactory = mock<TestViewFactory>()
        )
        val grandChild = TestNode(
            viewFactory = mock<TestViewFactory>()
        )
        directChild.attachChildNode(grandChild)
        node.attachChildNode(directChild)

        assertEquals(Lifecycle.State.INITIALIZED, grandChild.externalLifecycleRegistry.currentState)
    }

    @Test
    fun `attachChildNode() passes on current lifecycle to direct children - CREATED`() {
        node.onStop()

        val child = TestNode(
            viewFactory = mock<TestViewFactory>()
        )
        node.attachChildNode(child)

        assertEquals(Lifecycle.State.CREATED, child.externalLifecycleRegistry.currentState)
    }

    @Test
    fun `attachChildNode() passes on current lifecycle to children of children - CREATED`() {
        node.onStop()

        val directChild = TestNode(
            viewFactory = mock<TestViewFactory>()
        )
        val grandChild = TestNode(
            viewFactory = mock<TestViewFactory>()
        )
        directChild.attachChildNode(grandChild)
        node.attachChildNode(directChild)

        assertEquals(Lifecycle.State.CREATED, grandChild.externalLifecycleRegistry.currentState)
    }

    @Test
    fun `attachChildNode() passes on current lifecycle to direct children - STARTED`() {
        node.onStart()

        val child = TestNode(
            viewFactory = mock<TestViewFactory>()
        )
        node.attachChildNode(child)

        assertEquals(Lifecycle.State.STARTED, child.externalLifecycleRegistry.currentState)
    }

    @Test
    fun `attachChildNode() passes on current lifecycle to children of children - STARTED`() {
        node.onStart()

        val directChild = TestNode(
            viewFactory = mock<TestViewFactory>()
        )
        val grandChild = TestNode(
            viewFactory = mock<TestViewFactory>()
        )
        directChild.attachChildNode(grandChild)
        node.attachChildNode(directChild)

        assertEquals(Lifecycle.State.STARTED, grandChild.externalLifecycleRegistry.currentState)
    }

    @Test
    fun `attachChildNode() passes on current lifecycle to direct children - RESUMED`() {
        node.onResume()

        val child = TestNode(
            viewFactory = mock<TestViewFactory>()
        )
        node.attachChildNode(child)

        assertEquals(Lifecycle.State.RESUMED, child.externalLifecycleRegistry.currentState)
    }

    @Test
    fun `attachChildNode() passes on current lifecycle to children of children - RESUMED`() {
        node.onResume()

        val directChild = TestNode(
            viewFactory = mock<TestViewFactory>()
        )
        val grandChild = TestNode(
            viewFactory = mock<TestViewFactory>()
        )
        directChild.attachChildNode(grandChild)
        node.attachChildNode(directChild)

        assertEquals(Lifecycle.State.RESUMED, grandChild.externalLifecycleRegistry.currentState)
    }

    @Test
    fun `attachChildView() implies attachToView() when Android view system is available`() {
        val child = mock<Node<*>>()
        node.attachToView(parentViewGroup)
        node.attachChildView(child)
        verify(child).attachToView(parentViewGroup)
    }

    @Test
    fun `Tag is saved to bundle`() {
        val outState = Bundle()
        node.onSaveInstanceState(outState)
        assertEquals(node.identifier.uuid, outState.getSerializable(Rib.Identifier.KEY_UUID))
    }

    @Test
    fun `View state saved to bundle`() {
        val outState = Bundle()
        node.savedViewState = mock()
        node.onSaveInstanceState(outState)
        interactor.onSaveInstanceState(outState)
        val inner = outState.getBundle(BUNDLE_KEY)
        assertNotNull(inner)
        assertEquals(node.savedViewState, inner!!.getSparseParcelableArray<Parcelable>(KEY_VIEW_STATE))
    }

    @Test
    fun `View state is saved and restored from bundle`() {
        node = createNode(testBuildParams(savedInstanceState = null))

        val outState = Bundle()
        node.onSaveInstanceState(outState)
        val savedViewState = node.savedViewState

        node = createNode(testBuildParams(savedInstanceState = outState))
        assertEquals(savedViewState, node.savedViewState)
    }

    @Test
    fun `saveViewState() does its job`() {
        node.view = view
        node.saveViewState()
        verify(androidView).saveHierarchyState(node.savedViewState)
    }

    @Test
    fun `attachToView() restores view state`() {
        node.savedViewState = mock()
        node.attachToView(parentViewGroup)
        verify(view.androidView).restoreHierarchyState(node.savedViewState)
    }

    @Test
    fun `attachToView() invokes viewFactory`() {
        node.attachToView(parentViewGroup)
        verify(viewFactory).invoke(parentViewGroup)
    }

    @Test
    fun `attachToView() + has view = sets view lifecycle to external lifecycle - when INITIALIZED, view is in state INITIALIZED`() {
        // by default it's not started, should be on INITIALIZED
        node.attachToView(parentViewGroup)
        assertEquals(Lifecycle.State.INITIALIZED, node.viewLifecycleRegistry!!.currentState)
    }

    @Test
    fun `attachToView() + has view = sets view lifecycle to external lifecycle - when CREATED, view is in state CREATED`() {
        node.onStop()
        node.attachToView(parentViewGroup)
        assertEquals(Lifecycle.State.CREATED, node.viewLifecycleRegistry!!.currentState)
    }

    @Test
    fun `attachToView() + has view =  sets view lifecycle to external lifecycle - when STARTED, view is in state STARTED`() {
        node = createNode(viewFactory = viewFactory)
        node.onStart()
        node.attachToView(parentViewGroup)
        assertEquals(Lifecycle.State.STARTED, node.viewLifecycleRegistry!!.currentState)
    }

    @Test
    fun `attachToView() + has view =  sets view lifecycle to external lifecycle - when RESUMED, view is in state RESUMED`() {
        node = createNode(viewFactory = viewFactory)
        node.onResume()
        node.attachToView(parentViewGroup)
        assertEquals(Lifecycle.State.RESUMED, node.viewLifecycleRegistry!!.currentState)
    }

    @Test
    fun `attachToView() + viewless = doesn't have view lifecycle`() {
        node = createNode(viewFactory = null)
        node.onResume()
        node.attachToView(parentViewGroup)
        assertNull(node.viewLifecycleRegistry)
    }

    @Test
    fun `When current Node has a view, attachToView() adds view to parentViewGroup`() {
        node.attachToView(parentViewGroup)
        verify(parentViewGroup).addView(view.androidView)
    }

    @Test
    fun `When current Node has a view, attachToView() notifies Interactor of view creation when external lifecycle goes above INITIALIZED`() {
        node.onStart()
        node.onStop()
        node.attachToView(parentViewGroup)
        verify(interactor).onViewCreated(node.viewLifecycleRegistry!!, view)
    }

    @Test
    fun `By the time onViewCreated is called, passed in lifecycle is ready for bindings`() {
        val trigger: PublishRelay<Unit> = PublishRelay.create()
        val receiver: Consumer<Unit> = mock()
        val onViewCreated: (TestView, Lifecycle) -> Unit = { _, viewLifecycle ->
            viewLifecycle.createDestroy {
                bind(trigger to receiver)
            }

            trigger.accept(Unit)
        }

        node = createNode(
            interactor = TestInteractor(
                onViewCreated = onViewCreated
            )
        )

        node.onStart()
        node.onStop()
        node.attachToView(parentViewGroup)
        verify(receiver).accept(Unit)
    }

    @Test
    fun `When current Node doesn't have a view, attachToView() does not add anything to parentViewGroup`() {
        node = createNode(viewFactory = null)
        node.attachToView(parentViewGroup)
        verify(parentViewGroup, never()).addView(anyOrNull())
    }

    @Test
    fun `When current Node doesn't have a view, attachToView() does not notify Interactor of view creation`() {
        node = createNode(viewFactory = null)
        node.attachToView(parentViewGroup)
        verify(interactor, never()).onViewCreated(anyOrNull(), anyOrNull())
    }

    @Test
    fun `executeWorkflow executes action on subscribe`() {
        var actionInvoked = false
        val action = { actionInvoked = true }
        val workflow: Single<Node<*>> = node.executeWorkflowInternal(action)
        val testObserver = TestObserver<Node<*>>()
        workflow.subscribe(testObserver)

        assertEquals(true, actionInvoked)
        testObserver.assertValue(node)
        testObserver.assertComplete()
    }

    @Test
    fun `executeWorkflow never executes action on lifecycle terminate before subscribe`() {
        node.onDetach()

        var actionInvoked = false
        val action = { actionInvoked = true }
        val workflow: Single<Node<*>> = node.executeWorkflowInternal(action)
        val testObserver = TestObserver<Node<*>>()
        workflow.subscribe(testObserver)

        assertEquals(false, actionInvoked)
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

        assertEquals(true, actionInvoked)
        testObserver.assertValue(child3)
        testObserver.assertComplete()
    }

    @Test
    fun `attachWorkflow never executes action on lifecycle terminate before subscribe`() {
        node.onDetach()

        var actionInvoked = false
        val action = { actionInvoked = true }
        val workflow: Single<TestNode> = node.attachWorkflowInternal(action)
        val testObserver = TestObserver<TestNode>()
        workflow.subscribe(testObserver)

        assertEquals(false, actionInvoked)
        testObserver.assertNever(child1)
        testObserver.assertNever(child2)
        testObserver.assertNever(child3)
        testObserver.assertNotComplete()
    }

    @Test
    fun `waitForChildAttached emits expected child immediately if it's already attached`() {
        val workflow: Single<TestNode2> = node.waitForChildAttachedInternal()
        val testObserver = TestObserver<TestNode2>()
        val testChildNode = TestNode2()

        node.attachChildNode(testChildNode)
        workflow.subscribe(testObserver)

        testObserver.assertValue(testChildNode)
        testObserver.assertComplete()
    }

    @Test
    fun `waitForChildAttached never executes action on lifecycle terminate before subscribe`() {
        node.onDetach()

        val workflow: Single<TestNode2> = node.waitForChildAttachedInternal()
        val testObserver = TestObserver<TestNode2>()
        workflow.subscribe(testObserver)

        testObserver.assertNoValues()
        testObserver.assertNotComplete()
    }
}
