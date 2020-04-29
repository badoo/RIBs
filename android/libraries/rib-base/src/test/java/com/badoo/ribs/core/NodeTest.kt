package com.badoo.ribs.core

import android.os.Bundle
import android.os.Parcelable
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.ribs.core.Node.Companion.BUNDLE_KEY
import com.badoo.ribs.core.Node.Companion.KEY_VIEW_STATE
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.exception.RootNodeAttachedAsChildException
import com.badoo.ribs.core.helper.AnyConfiguration
import com.badoo.ribs.core.helper.TestInteractor
import com.badoo.ribs.core.helper.TestNode
import com.badoo.ribs.core.helper.TestNode2
import com.badoo.ribs.core.helper.TestRouter
import com.badoo.ribs.core.helper.TestView
import com.badoo.ribs.core.helper.testBuildParams
import com.badoo.ribs.core.routing.portal.AncestryInfo
import com.badoo.ribs.core.view.ViewPlugin
import com.badoo.ribs.util.RIBs
import com.jakewharton.rxrelay2.PublishRelay
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.observers.TestObserver
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NodeTest {

    interface RandomOtherNode1 : Rib
    interface RandomOtherNode2 : Rib
    interface RandomOtherNode3 : Rib
    interface RandomRootNode : Rib

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
    private lateinit var interactor: Interactor<TestView>
    private lateinit var child1: TestNode
    private lateinit var child2: TestNode
    private lateinit var child3: TestNode
    private lateinit var root1: TestNode
    private lateinit var allChildren: List<Node<*>>
    private lateinit var viewPlugins: Set<ViewPlugin>
    private lateinit var childAncestry: AncestryInfo

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
        childAncestry = AncestryInfo.Child(node, AnyConfiguration)

        addChildren()
        root1 = TestNode(
            buildParams = testBuildParams(object : RandomRootNode {}),
            viewFactory = null
        )
    }

    private fun createNode(
        buildParams: BuildParams<Nothing?> = testBuildParams(),
        viewFactory: TestViewFactory? = this@NodeTest.viewFactory,
        interactor: Interactor<TestView> = this@NodeTest.interactor
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
        child1 = TestNode(
            buildParams = testBuildParams(
                rib = object : RandomOtherNode1 {},
                ancestryInfo = childAncestry
            ),
            viewFactory = null
        )
        child2 = TestNode(
            buildParams = testBuildParams(
                rib = object : RandomOtherNode2 {},
                ancestryInfo = childAncestry
            ),
            viewFactory = null
        )
        child3 = TestNode(
            buildParams = testBuildParams(
                rib = object : RandomOtherNode3 {},
                ancestryInfo = childAncestry
            ),
            viewFactory = null
        )
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
        verify(interactor).onAttach(node.lifecycleManager.ribLifecycle.lifecycle)
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
//            val mockNode = mock<Node<*>>()
//            whenever(mockNode.identifier).thenReturn(identifiers[i])
//            mocks.add(mockNode)
            mocks.add(
                mock {
                    on { identifier }.thenReturn(identifiers[i])
                    on { lifecycleManager }.thenReturn(mock())
                }
            )
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
    fun `attachToView() results in children added to target defined by View`() {
        val n1 = TestNode(identifier = object : RandomOtherNode1 {})
        val n2 = TestNode(identifier = object : RandomOtherNode2 {})
        val n3 = TestNode(identifier = object : RandomOtherNode3 {})
        val testNodes = listOf(n1, n2, n3)

        whenever(view.getParentViewForChild(n1)).thenReturn(someViewGroup1)
        whenever(view.getParentViewForChild(n2)).thenReturn(someViewGroup2)
        whenever(view.getParentViewForChild(n3)).thenReturn(someViewGroup3)

        node.attachToView(parentViewGroup)
        testNodes.forEach { node.attachChildView(it) }
        assertEquals(someViewGroup1, n1.parentViewGroup)
        assertEquals(someViewGroup2, n2.parentViewGroup)
        assertEquals(someViewGroup3, n3.parentViewGroup)
    }

    @Test
    fun `attachChildNode() does not imply attachToView when Android view system is not available`() {
        val childViewFactory = mock<TestViewFactory>()
        val child = TestNode(
            viewFactory = childViewFactory,
            buildParams = testBuildParams(ancestryInfo = childAncestry)
        )
        node.attachChildNode(child)
        verify(childViewFactory, never()).invoke(parentViewGroup)
    }

    @Test
    fun `onAttach() results in lifecycle of node going to CREATED`() {
        node.onAttach()
        assertEquals(Lifecycle.State.CREATED, node.lifecycle.currentState)
    }

    @Test
    fun `attachChildNode() results in lifecycle of children going to CREATED`() {
        // by default it's not started, should be on INITIALIZED

        val child = TestNode(
            viewFactory = mock<TestViewFactory>(),
            buildParams = testBuildParams(ancestryInfo = childAncestry)
        )
        node.attachChildNode(child)

        assertEquals(Lifecycle.State.CREATED, child.lifecycle.currentState)
    }

    @Test
    fun `attachChildNode() results in lifecycle of grandchildren going to CREATED`() {
        // by default it's not started, should be on INITIALIZED

        val directChild = TestNode(
                viewFactory = mock<TestViewFactory>(),
                buildParams = testBuildParams(ancestryInfo = childAncestry)
        )
        val grandChildAncestryInfo: AncestryInfo =
                AncestryInfo.Child(directChild, AnyConfiguration)
        val grandChild = TestNode(
                viewFactory = mock<TestViewFactory>(),
                buildParams = testBuildParams(ancestryInfo = grandChildAncestryInfo)
        )
        directChild.attachChildNode(grandChild)
        node.attachChildNode(directChild)

        assertEquals(Lifecycle.State.CREATED, grandChild.lifecycle.currentState)
    }

    @Test
    fun `attachChildNode() passes on current lifecycle to direct children - CREATED`() {
        node.onStop()

        val child = TestNode(
            viewFactory = mock<TestViewFactory>(),
            buildParams = testBuildParams(ancestryInfo = childAncestry)
        )
        node.attachChildNode(child)

        assertEquals(Lifecycle.State.CREATED, child.lifecycleManager.externalLifecycle.currentState)
    }

    @Test
    fun `attachChildNode() passes on current lifecycle to children of children - CREATED`() {
        node.onStop()

        val directChild = TestNode(
            viewFactory = mock<TestViewFactory>(),
            buildParams = testBuildParams(ancestryInfo = childAncestry)
        )
        val grandChildAncestryInfo: AncestryInfo =
                AncestryInfo.Child(directChild, AnyConfiguration)
        val grandChild = TestNode(
            viewFactory = mock<TestViewFactory>(),
            buildParams = testBuildParams(ancestryInfo = grandChildAncestryInfo)
        )
        directChild.attachChildNode(grandChild)
        node.attachChildNode(directChild)

        assertEquals(Lifecycle.State.CREATED, grandChild.lifecycleManager.externalLifecycle.currentState)
    }

    @Test
    fun `attachChildNode() passes on current lifecycle to direct children - STARTED`() {
        node.onStart()

        val child = TestNode(
            viewFactory = mock<TestViewFactory>(),
            buildParams = testBuildParams(ancestryInfo = childAncestry)
        )
        node.attachChildNode(child)

        assertEquals(Lifecycle.State.STARTED, child.lifecycleManager.externalLifecycle.currentState)
    }

    @Test
    fun `attachChildNode() passes on current lifecycle to children of children - STARTED`() {
        node.onStart()

        val directChild = TestNode(
            viewFactory = mock<TestViewFactory>(),
            buildParams = testBuildParams(ancestryInfo = childAncestry)
        )
        val grandChild = TestNode(
            viewFactory = mock<TestViewFactory>(),
            buildParams = testBuildParams(ancestryInfo = childAncestry)
        )
        directChild.attachChildNode(grandChild)
        node.attachChildNode(directChild)

        assertEquals(Lifecycle.State.STARTED, grandChild.lifecycleManager.externalLifecycle.currentState)
    }

    @Test
    fun `attachChildNode() passes on current lifecycle to direct children - RESUMED`() {
        node.onResume()

        val child = TestNode(
            viewFactory = mock<TestViewFactory>(),
            buildParams = testBuildParams(ancestryInfo = childAncestry)
        )
        node.attachChildNode(child)

        assertEquals(Lifecycle.State.RESUMED, child.lifecycleManager.externalLifecycle.currentState)
    }

    @Test
    fun `attachChildNode() passes on current lifecycle to children of children - RESUMED`() {
        node.onResume()

        val directChild = TestNode(
            viewFactory = mock<TestViewFactory>(),
            buildParams = testBuildParams(ancestryInfo = childAncestry)
        )
        val grandChildAncestryInfo: AncestryInfo =
                AncestryInfo.Child(directChild, AnyConfiguration)
        val grandChild = TestNode(
                viewFactory = mock<TestViewFactory>(),
                buildParams = testBuildParams(ancestryInfo = grandChildAncestryInfo)
        )
        directChild.attachChildNode(grandChild)
        node.attachChildNode(directChild)

        assertEquals(Lifecycle.State.RESUMED, grandChild.lifecycleManager.externalLifecycle.currentState)
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
    fun `attachToView() = view lifecycle is in state CREATED`() {
        node.attachToView(parentViewGroup)
        assertEquals(Lifecycle.State.CREATED, node.lifecycleManager.viewLifecycle!!.lifecycle.currentState)
    }

    @Test
    fun `attachToView() + has view = sets view lifecycle to external lifecycle - when CREATED, view is in state CREATED`() {
        node.onStop()
        node.attachToView(parentViewGroup)
        assertEquals(Lifecycle.State.CREATED, node.lifecycleManager.viewLifecycle!!.lifecycle.currentState)
    }

    @Test
    fun `attachToView() + has view =  sets view lifecycle to external lifecycle - when STARTED, view is in state STARTED`() {
        node = createNode(viewFactory = viewFactory)
        node.onStart()
        node.attachToView(parentViewGroup)
        assertEquals(Lifecycle.State.STARTED, node.lifecycleManager.viewLifecycle!!.lifecycle.currentState)
    }

    @Test
    fun `attachToView() + has view =  sets view lifecycle to external lifecycle - when RESUMED, view is in state RESUMED`() {
        node = createNode(viewFactory = viewFactory)
        node.onResume()
        node.attachToView(parentViewGroup)
        assertEquals(
            Lifecycle.State.RESUMED,
            node.lifecycleManager.viewLifecycle!!.lifecycle.currentState
        )
//        assertEquals(Lifecycle.State.RESUMED, node.viewLifecycleRegistry!!.currentState)
//    }
//
//    @Test
//    fun `attachToView() + viewless = doesn't have view lifecycle`() {
//        node = createNode(viewFactory = null)
//        node.onResume()
//        node.attachToView(parentViewGroup)
//        assertNull(node.viewLifecycleRegistry)
//    }
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
        verify(interactor).onViewCreated(node.lifecycleManager.viewLifecycle!!.lifecycle, view)
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
        val testChildNode = TestNode2(buildParams = testBuildParams(ancestryInfo = childAncestry))

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

    @Test
    fun `When a child node has a root BuildContext, attachChildNode() invokes error handler`() {
        node = createNode(viewFactory = null)
        node.attachToView(parentViewGroup)

        val errorHandler = mock<RIBs.ErrorHandler>()
        RIBs.clearErrorHandler()
        RIBs.errorHandler = errorHandler

        node.attachChildNode(root1)

        verify(errorHandler).handleNonFatalError(any(), isA<RootNodeAttachedAsChildException>())
    }

    @Test
    fun `When a child node has a child BuildContext, attachChildNode() does not invoke any error`() {
        node = createNode(viewFactory = null)
        node.attachToView(parentViewGroup)

        val errorHandler = mock<RIBs.ErrorHandler>()
        RIBs.clearErrorHandler()
        RIBs.errorHandler = errorHandler

        node.attachChildNode(child1)

        verify(errorHandler, never()).handleNonFatalError(any(), isA<RootNodeAttachedAsChildException>())
        verifyNoMoreInteractions(errorHandler)
    }
}
