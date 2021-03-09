package com.badoo.ribs.core

import android.os.Bundle
import android.os.Parcelable
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.Node.Companion.BUNDLE_KEY
import com.badoo.ribs.core.Node.Companion.KEY_VIEW_STATE
import com.badoo.ribs.core.exception.RootNodeAttachedAsChildException
import com.badoo.ribs.core.helper.AnyConfiguration
import com.badoo.ribs.core.helper.TestNode
import com.badoo.ribs.core.helper.TestRib
import com.badoo.ribs.core.helper.TestRouter
import com.badoo.ribs.core.helper.TestView
import com.badoo.ribs.core.helper.testBuildParams
import com.badoo.ribs.core.modality.AncestryInfo
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.store.RetainedInstanceStore
import com.badoo.ribs.util.RIBs
import com.jakewharton.rxrelay2.PublishRelay
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.isA
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import io.reactivex.functions.Consumer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NodeTest {

    interface TestViewFactory : ViewFactory<TestView>

    private lateinit var node: Node<TestView>
    private lateinit var view: TestView
    private lateinit var androidView: ViewGroup
    private lateinit var parentView: RibView
    private lateinit var someViewGroup1: ViewGroup
    private lateinit var someViewGroup2: ViewGroup
    private lateinit var someViewGroup3: ViewGroup
    private lateinit var viewFactory: TestViewFactory
    private lateinit var router: Router<TestRouter.Configuration>
    private lateinit var interactor: Interactor<TestRib, TestView>
    private lateinit var child1: TestNode
    private lateinit var child2: TestNode
    private lateinit var child3: TestNode
    private lateinit var root1: TestNode
    private lateinit var childAncestry: AncestryInfo
    private lateinit var retainedInstanceStore: RetainedInstanceStore

    @Before
    fun setUp() {
        parentView = mock()
        someViewGroup1 = mock()
        someViewGroup2 = mock()
        someViewGroup3 = mock()
        androidView = mock()
        view = mock { on { androidView }.thenReturn(androidView) }
        viewFactory = mock { on { invoke(argThat { parent == parentView }) } doReturn view }
        router = mock()
        interactor = mock()
        retainedInstanceStore = mock()
        node = createNode(viewFactory = viewFactory)
        childAncestry = AncestryInfo.Child(node, Routing(AnyConfiguration))

        addChildren()
        root1 = TestNode(
            buildParams = testBuildParams(),
            viewFactory = null
        )
    }

    private fun createNode(
        buildParams: BuildParams<Nothing?> = testBuildParams(),
        viewFactory: TestViewFactory? = this@NodeTest.viewFactory,
        plugins: List<Plugin> = emptyList()
    ): Node<TestView> = Node(
        buildParams = buildParams,
        viewFactory = viewFactory,
        plugins = plugins,
        retainedInstanceStore = retainedInstanceStore
    )

    @After
    fun tearDown() {
        RIBs.clearErrorHandler()
    }

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
    fun `onDestroy() verifies view has been detached`() {
        val errorHandler = mock<RIBs.ErrorHandler>()
        RIBs.clearErrorHandler()
        RIBs.errorHandler = errorHandler
        node.onCreateView(parentView)
        node.onAttachToView()

        node.onDestroy(isRecreating = false)

        verify(errorHandler).handleNonFatalError(any(), isA<RuntimeException>())
    }

    @Test
    fun `onSaveInstanceState() saves view state as well`() {
        node.view = view
        node.onSaveInstanceState(mock())
        verify(androidView).saveHierarchyState(node.savedViewState)
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
    fun `isViewAttached flag is initially false`() {
        assertEquals(false, node.isAttachedToView)
    }

    @Test
    fun `onAttachToView() sets isViewAttached flag to true`() {
        node.onCreateView(parentView)
        node.onAttachToView()
        assertEquals(true, node.isAttachedToView)
    }

    @Test
    fun `onDetachFromView() resets isViewAttached flag to false`() {
        node.onDetachFromView()
        assertEquals(false, node.isAttachedToView)
    }

    private fun createAndAttachChildMocks(
        n: Int,
        identifiers: MutableList<Rib.Identifier> = mutableListOf()
    ): List<Node<*>> {
        if (identifiers.isEmpty()) {
            for (i in 0 until n) {
                identifiers.add(testBuildParams().identifier)
            }
        }
        val mocks = mutableListOf<Node<*>>()
        for (i in 0 until n) {
            mocks.add(
                mock {
                    on { identifier }.thenReturn(identifiers[i])
                    on { lifecycleManager }.thenReturn(mock())
                }
            )
        }

        mocks.forEach {
            node._children.add(it)
        }

        return mocks
    }

    // FIXME rework
    @Ignore("This should be tested on RibView impls, not here")
    @Test
    fun `attachChildView() results in children added to parentViewGroup given Router does not define something else `() {
//        whenever(view.getParentViewForChild(any())).thenReturn(null)
        val mocks = createAndAttachChildMocks(3)
        node.onAttachToView()
        mocks.forEach {
            node.attachChildView(it)
            verify(it).onAttachToView()
        }
    }

    // FIXME rework
    @Ignore("This should be tested on RibView impls, not here")
    @Test
    fun `attachToView() results in children added to target defined by View`() {
        val n1 = TestNode() // identifier = object : RandomOtherNode1 {}
        val n2 = TestNode() // identifier = object : RandomOtherNode2 {}
        val n3 = TestNode() // identifier = object : RandomOtherNode3 {}
        val testNodes = listOf(n1, n2, n3)

//        whenever(view.getParentViewForChild(n1)).thenReturn(someViewGroup1)
//        whenever(view.getParentViewForChild(n2)).thenReturn(someViewGroup2)
//        whenever(view.getParentViewForChild(n3)).thenReturn(someViewGroup3)

        node.onAttachToView()
        testNodes.forEach { node.attachChildView(it) }
        assertEquals(someViewGroup1, TODO("n1.parentViewGroup"))
        assertEquals(someViewGroup2, TODO("n2.parentViewGroup"))
        assertEquals(someViewGroup3, TODO("n3.parentViewGroup"))
    }

    @Test
    fun `attachChildNode() does not imply attachToView when Android view system is not available`() {
        val childViewFactory = mock<TestViewFactory>()
        val child = TestNode(
            viewFactory = childViewFactory,
            buildParams = testBuildParams(ancestryInfo = childAncestry)
        )
        node.attachChildNode(child)
        verify(childViewFactory, never()).invoke(any())
    }

    @Test
    fun `onCreate() results in lifecycle of node going to CREATED`() {
        node.onCreate()
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
            AncestryInfo.Child(directChild, Routing(AnyConfiguration))
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
            AncestryInfo.Child(directChild, Routing(AnyConfiguration))

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
            AncestryInfo.Child(directChild, Routing(AnyConfiguration))

        val grandChild = TestNode(
            viewFactory = mock<TestViewFactory>(),
            buildParams = testBuildParams(ancestryInfo = grandChildAncestryInfo)
        )
        directChild.attachChildNode(grandChild)
        node.attachChildNode(directChild)

        assertEquals(Lifecycle.State.RESUMED, grandChild.lifecycleManager.externalLifecycle.currentState)
    }

    @Ignore("This should be reworked to match new mechanism")
    @Test
    fun `attachChildView() implies attachToView() when Android view system is available`() {
        val child = mock<Node<*>>()
        node.onCreateView(parentView)
        node.onAttachToView()
        node.attachChildView(child)
        verify(child).onAttachToView()
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
    fun `onCreateView() restores view state`() {
        node.savedViewState = mock()
        node.onCreateView(parentView)
        verify(view.androidView).restoreHierarchyState(node.savedViewState)
    }

    @Test
    fun `onCreateView() invokes viewFactory`() {
        node.onCreateView(parentView)
        verify(viewFactory).invoke(argThat { parent == parentView })
    }

    @Test
    fun `onCreateView() = view lifecycle is in state CREATED`() {
        node.onCreateView(parentView)
        assertEquals(Lifecycle.State.CREATED, node.lifecycleManager.viewLifecycle!!.lifecycle.currentState)
    }

    @Test
    fun `attachToView() + has view = sets view lifecycle to external lifecycle - when CREATED, view is in state CREATED`() {
        node.onStop()
        node.onCreateView(parentView)
        node.onAttachToView()
        assertEquals(Lifecycle.State.CREATED, node.lifecycleManager.viewLifecycle!!.lifecycle.currentState)
    }

    @Test
    fun `attachToView() + has view =  sets view lifecycle to external lifecycle - when STARTED, view is in state STARTED`() {
        node = createNode(viewFactory = viewFactory)
        node.onStart()
        node.onCreateView(parentView)
        node.onAttachToView()
        assertEquals(Lifecycle.State.STARTED, node.lifecycleManager.viewLifecycle!!.lifecycle.currentState)
    }

    @Test
    fun `attachToView() + has view =  sets view lifecycle to external lifecycle - when RESUMED, view is in state RESUMED`() {
        node = createNode(viewFactory = viewFactory)
        node.onResume()
        node.onCreateView(parentView)
        node.onAttachToView()
        assertEquals(
            Lifecycle.State.RESUMED,
            node.lifecycleManager.viewLifecycle!!.lifecycle.currentState
        )
    }

    // FIXME rework
    @Ignore("This should be tested on RibView impls, not here")
    @Test
    fun `When current Node has a view, attachToView() adds view to parentViewGroup`() {
        node.onCreateView(parentView)
        node.onAttachToView()
//        verify(parentView).addView(view.androidView)
    }

    @Test
    fun `By the time onViewCreated is called, passed in lifecycle is ready for bindings`() {
        val trigger: PublishRelay<Unit> = PublishRelay.create()
        val receiver: Consumer<Unit> = mock()
        val viewAware: ViewAware<TestView> = object : ViewAware<TestView> {
            override fun onViewCreated(view: TestView, viewLifecycle: Lifecycle) {
                viewLifecycle.createDestroy {
                    bind(trigger to receiver)
                }

                trigger.accept(Unit)
            }
        }

        node = createNode(plugins = listOf(viewAware))
        node.onStart()
        node.onStop()
        node.onCreateView(parentView)
        node.onAttachToView()
        verify(receiver).accept(Unit)
    }

    // FIXME rework
    @Ignore("This should be tested on RibView impls, not here")
    @Test
    fun `When current Node doesn't have a view, attachToView() does not add anything to parentViewGroup`() {
        node = createNode(viewFactory = null)
        node.onCreateView(parentView)
        node.onAttachToView()
//        verify(parentView, never()).addView(anyOrNull())
    }

    @Test
    fun `When current Node doesn't have a view, attachToView() does not notify Interactor of view creation`() {
        node = createNode(viewFactory = null)
        node.onCreateView(parentView)
        node.onAttachToView()
        verify(interactor, never()).onViewCreated(anyOrNull(), anyOrNull())
    }

    @Test
    fun `When a child node has a root BuildContext, attachChildNode() invokes error handler`() {
        node = createNode(viewFactory = null)
        node.onAttachToView()

        val errorHandler = mock<RIBs.ErrorHandler>()
        RIBs.clearErrorHandler()
        RIBs.errorHandler = errorHandler

        node.attachChildNode(root1)

        verify(errorHandler).handleNonFatalError(any(), isA<RootNodeAttachedAsChildException>())
    }

    @Test
    fun `When a child node has a child BuildContext, attachChildNode() does not invoke any error`() {
        node = createNode(viewFactory = null)
        node.onAttachToView()

        val errorHandler = mock<RIBs.ErrorHandler>()
        RIBs.clearErrorHandler()
        RIBs.errorHandler = errorHandler

        node.attachChildNode(child1)

        verify(errorHandler, never()).handleNonFatalError(any(), isA<RootNodeAttachedAsChildException>())
        verifyNoMoreInteractions(errorHandler)
    }

    @Test
    fun `When current Node is destroyed and is not going to be recreated, clears RetainedInstanceStore`() {
        node.onDestroy(isRecreating = false)

        verify(retainedInstanceStore).removeAll(node.identifier)
    }

    @Test
    fun `When current Node is destroyed and is going to be recreated, keeps RetainedInstanceStore`() {
        node.onDestroy(isRecreating = true)

        verify(retainedInstanceStore, never()).removeAll(node.identifier)
    }
}
