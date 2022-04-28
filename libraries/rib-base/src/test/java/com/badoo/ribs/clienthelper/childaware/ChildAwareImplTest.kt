package com.badoo.ribs.clienthelper.childaware

import android.os.Parcelable
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.builder.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisationDirectoryImpl
import com.badoo.ribs.core.lifecycle.TestLifecycle
import com.badoo.ribs.core.modality.AncestryInfo
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.routing.Routing
import kotlinx.parcelize.Parcelize
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.Test

class ChildAwareImplTest {

    private val registry = ChildAwareImpl()
    private val rootNode = createViaBuilder(isRoot = true) { buildContext ->
        Node<RibView>(
            buildParams = BuildParams(payload = null, buildContext = buildContext),
            viewFactory = null,
            plugins = listOf(registry),
        )
    }.apply {
        onCreate()
    }
    private val lifecycle: Lifecycle
        get() = registry.node.lifecycle

    // region Single

    @Test
    fun `whenChildBuilt is invoked if registered before onChildBuilt`() {
        var capturedNode: Rib? = null
        registry.whenChildBuilt<Child1>(lifecycle) { _, child -> capturedNode = child }
        val child1 = createChild1(attach = false)
        assertEquals(child1, capturedNode)
    }

    @Test
    fun `whenChildBuilt is invoked if registered after onChildBuilt`() {
        val child1 = createChild1()
        var capturedNode: Rib? = null
        registry.whenChildBuilt<Child1>(lifecycle) { _, child -> capturedNode = child }
        assertEquals(child1, capturedNode)
    }

    @Test
    fun `whenChildAttached is invoked if registered before onChildAttached`() {
        var capturedNode: Rib? = null
        registry.whenChildAttached<Child1>(lifecycle) { _, child -> capturedNode = child }
        val child1 = createChild1()
        assertEquals(child1, capturedNode)
    }

    @Test
    fun `whenChildAttached is invoked if registered after onChildAttached`() {
        val child1 = createChild1()
        var capturedNode: Rib? = null
        registry.whenChildAttached<Child1>(lifecycle) { _, child -> capturedNode = child }
        assertEquals(child1, capturedNode)
    }

    @Test
    fun `every whenChildBuilt is invoked`() {
        var capturedNode1: Rib? = null
        var capturedNode2: Rib? = null
        registry.childAware(lifecycle) {
            whenChildBuilt<Child1> { _, child -> capturedNode1 = child }
            whenChildBuilt<Child1> { _, child -> capturedNode2 = child }
        }
        val child1 = createChild1(attach = false)
        assertEquals(child1, capturedNode1)
        assertEquals(child1, capturedNode2)
    }

    @Test
    fun `whenChildBuilt is invoked multiple times for each instance`() {
        val child1 = createChild1()
        val child2 = createChild1()
        val capturedNodes = ArrayList<Rib>()
        registry.whenChildBuilt<Child1>(lifecycle) { _, child -> capturedNodes += child }
        assertEquals(listOf(child1, child2), capturedNodes)
    }

    @Test
    fun `whenChildAttached is not invoked for unrelated child`() {
        createChild1()
        var capturedNode: Rib? = null
        registry.whenChildAttached<Child2>(lifecycle) { _, child -> capturedNode = child }
        assertNull(capturedNode)
    }

    // endregion

    // region Double

    @Test
    fun `whenChildrenBuilt is invoked if registered before onChildBuilt`() {
        val capturedNodes = ArrayList<Rib>()
        registry.whenChildrenBuilt<Child1, Child2>(lifecycle) { _, c1, c2 ->
            capturedNodes += c1
            capturedNodes += c2
        }
        val child1 = createChild1()
        val child2 = createChild2(attach = false)
        assertEquals(listOf(child1, child2), capturedNodes)
    }

    @Test
    fun `whenChildrenBuilt is invoked if registered after onChildBuilt`() {
        val child1 = createChild1()
        val child2 = createChild2()
        val capturedNodes = ArrayList<Rib>()
        registry.whenChildrenBuilt<Child1, Child2>(lifecycle) { _, c1, c2 ->
            capturedNodes += c1
            capturedNodes += c2
        }
        assertEquals(listOf(child1, child2), capturedNodes)
    }

    @Test
    fun `whenChildrenAttached is invoked if registered before onChildAttached`() {
        val capturedNodes = ArrayList<Rib>()
        registry.whenChildrenAttached<Child1, Child2>(lifecycle) { _, c1, c2 ->
            capturedNodes += c1
            capturedNodes += c2
        }
        val child1 = createChild1()
        val child2 = createChild2()
        assertEquals(listOf(child1, child2), capturedNodes)
    }

    @Test
    fun `whenChildrenAttached is invoked if registered after onChildAttached`() {
        val child1 = createChild1()
        val child2 = createChild2()
        val capturedNodes = ArrayList<Rib>()
        registry.whenChildrenAttached<Child1, Child2>(lifecycle) { _, c1, c2 ->
            capturedNodes += c1
            capturedNodes += c2
        }
        assertEquals(listOf(child1, child2), capturedNodes)
    }

    @Test
    fun `whenChildrenAttached is not invoked for unrelated children`() {
        createChild1()
        createChild3()
        val capturedNodes = ArrayList<Rib>()
        registry.whenChildrenAttached<Child1, Child2>(lifecycle) { _, c1, c2 ->
            capturedNodes += c1
            capturedNodes += c2
        }
        assertTrue(capturedNodes.isEmpty())
    }

    @Test
    fun `whenChildrenAttached is invoked multiple times for each pair of children`() {
        val child11 = createChild1()
        val child12 = createChild1()
        val child21 = createChild2()
        val child22 = createChild2()
        val capturedNodes = ArrayList<Pair<Rib, Rib>>()
        registry.whenChildrenAttached<Child1, Child2>(lifecycle) { _, child1, child2 ->
            capturedNodes += child1 to child2
        }
        assertEquals(
            listOf(child11 to child21, child11 to child22, child12 to child21, child12 to child22),
            capturedNodes
        )
    }

    @Test
    fun `whenChildrenAttached is invoked properly for same class connections`() {
        val child1 = createChild1()
        val child2 = createChild1()
        val child3 = createChild1()
        val capturedNodes = ArrayList<Pair<Rib, Rib>>()
        registry.whenChildrenAttached<Child1, Child1>(lifecycle) { _, c1, c2 ->
            capturedNodes += c1 to c2
        }
        assertEquals(
            listOf(child1 to child2, child1 to child3, child2 to child3),
            capturedNodes
        )
    }

    // endregion

    // region Callback lifecycle checks

    @Test
    fun `ignores registration when parent lifecycle is destroyed`() {
        val testLifecycle = TestLifecycle(Lifecycle.State.DESTROYED)
        createChild1()
        var capturedNode: Rib? = null
        registry.whenChildAttached<Child1>(testLifecycle.lifecycle) { _, child ->
            capturedNode = child
        }
        assertNull(capturedNode)
    }

    @Test
    fun `removes registration after parent lifecycle is destroyed`() {
        val testLifecycle = TestLifecycle(Lifecycle.State.CREATED)
        createChild1()
        var capturedNode: Rib?
        registry.whenChildAttached<Child1>(testLifecycle.lifecycle) { _, child ->
            capturedNode = child
        }
        testLifecycle.state = Lifecycle.State.DESTROYED
        capturedNode = null
        createChild1()
        assertNull(capturedNode)
    }

    // endregion

    // region Setup

    private fun createChild1(attach: Boolean = true) =
        createViaBuilder(attach = attach) { Child1Node(it) }

    private fun createChild2(attach: Boolean = true) =
        createViaBuilder(attach = attach) { Child2Node(it) }

    private fun createChild3(attach: Boolean = true) =
        createViaBuilder(attach = attach) { Child3Node(it) }

    private fun <T : Node<*>> createViaBuilder(
        isRoot: Boolean = false,
        attach: Boolean = true,
        factory: (BuildContext) -> T
    ): T =
        object : Builder<Nothing?, T>() {
            override fun build(buildParams: BuildParams<Nothing?>): T =
                factory(buildParams.buildContext)
        }.build(
            buildContext = BuildContext(
                ancestryInfo = if (isRoot) {
                    AncestryInfo.Root
                } else {
                    AncestryInfo.Child(rootNode, Routing(Configuration))
                },
                savedInstanceState = null,
                customisations = RibCustomisationDirectoryImpl(),
            ),
            payload = null
        ).also {
            if (!isRoot && attach) {
                rootNode.attachChildNode(it)
            }
        }

    @Parcelize
    object Configuration : Parcelable

    interface Child1 : Rib
    class Child1Node(
        buildContext: BuildContext,
    ) : Node<RibView>(
        buildParams = BuildParams(payload = null, buildContext = buildContext),
        viewFactory = null,
    ), Child1 {
        override fun toString(): String = "${this::class.simpleName}@${hashCode()}"
    }

    interface Child2 : Rib
    class Child2Node(
        buildContext: BuildContext,
    ) : Node<RibView>(
        buildParams = BuildParams(payload = null, buildContext = buildContext),
        viewFactory = null,
    ), Child2 {
        override fun toString(): String = "${this::class.simpleName}@${hashCode()}"
    }

    interface Child3 : Rib
    class Child3Node(
        buildContext: BuildContext,
    ) : Node<RibView>(
        buildParams = BuildParams(payload = null, buildContext = buildContext),
        viewFactory = null,
    ), Child3 {
        override fun toString(): String = "${this::class.simpleName}@${hashCode()}"
    }


    // endregion

}
