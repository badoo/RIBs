package com.badoo.ribs.clienthelper.childawareness

import android.os.Parcelable
import com.badoo.ribs.builder.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisationDirectoryImpl
import com.badoo.ribs.core.modality.AncestryInfo
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.routing.Routing
import kotlinx.android.parcel.Parcelize
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ChildAwareCallbackInfoTest {

    private val registry = ChildAwareRegistryImpl()
    private val rootNode = createViaBuilder(isRoot = true) { buildContext ->
        Node<RibView>(
            buildParams = BuildParams(payload = null, buildContext = buildContext),
            viewFactory = null,
            plugins = listOf(registry),
        )
    }.apply {
        onCreate()
    }

    // region Single

    @Test
    fun `whenChildBuilt is invoked if registered before onChildBuilt`() {
        var capturedNode: Rib? = null
        registry.whenChildBuilt<Child1>(rootNode.lifecycle) { _, child -> capturedNode = child }
        val child1 = createChild1(attach = false)
        assertEquals(child1, capturedNode)
    }

    @Test
    fun `whenChildBuilt is invoked if registered after onChildBuilt`() {
        val child1 = createChild1()
        var capturedNode: Rib? = null
        registry.whenChildBuilt<Child1>(rootNode.lifecycle) { _, child -> capturedNode = child }
        assertEquals(child1, capturedNode)
    }

    @Test
    fun `whenChildAttached is invoked if registered before onChildAttached`() {
        var capturedNode: Rib? = null
        registry.whenChildAttached<Child1>(rootNode.lifecycle) { _, child -> capturedNode = child }
        val child1 = createChild1()
        assertEquals(child1, capturedNode)
    }

    @Test
    fun `whenChildAttached is invoked if registered after onChildAttached`() {
        val child1 = createChild1()
        var capturedNode: Rib? = null
        registry.whenChildAttached<Child1>(rootNode.lifecycle) { _, child -> capturedNode = child }
        assertEquals(child1, capturedNode)
    }

    @Test
    fun `every whenChildBuilt is invoked`() {
        var capturedNode1: Rib? = null
        var capturedNode2: Rib? = null
        registry.whenChildBuilt<Child1>(rootNode.lifecycle) { _, child -> capturedNode1 = child }
        registry.whenChildBuilt<Child1>(rootNode.lifecycle) { _, child -> capturedNode2 = child }
        val child1 = createChild1(attach = false)
        assertEquals(child1, capturedNode1)
        assertEquals(child1, capturedNode2)
    }

    @Test
    fun `whenChildBuilt is invoked multiple times for each instance`() {
        val child1 = createChild1()
        val child2 = createChild1()
        val capturedNodes = ArrayList<Rib>()
        registry.whenChildBuilt<Child1>(rootNode.lifecycle) { _, child -> capturedNodes += child }
        assertEquals(listOf(child1, child2), capturedNodes)
    }

    @Test
    fun `whenChildAttached is not invoked for unrelated child`() {
        createChild1()
        var capturedNode: Rib? = null
        registry.whenChildAttached<Child2>(rootNode.lifecycle) { _, child -> capturedNode = child }
        assertNull(capturedNode)
    }

    // endregion

    // region Double

    @Test(expected = IllegalArgumentException::class)
    fun `whenChildrenBuilt can't register same class callback`() {
        registry.whenChildrenBuilt<Child1, Child1>(rootNode.lifecycle) { _, _, _ -> }
    }

    @Test
    fun `whenChildrenBuilt is invoked if registered before onChildBuilt`() {
        val capturedNodes = ArrayList<Rib>()
        registry.whenChildrenBuilt<Child1, Child2>(rootNode.lifecycle) { _, c1, c2 ->
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
        registry.whenChildrenBuilt<Child1, Child2>(rootNode.lifecycle) { _, c1, c2 ->
            capturedNodes += c1
            capturedNodes += c2
        }
        assertEquals(listOf(child1, child2), capturedNodes)
    }

    @Test
    fun `whenChildrenAttached is invoked if registered before onChildAttached`() {
        val capturedNodes = ArrayList<Rib>()
        registry.whenChildrenAttached<Child1, Child2>(rootNode.lifecycle) { _, c1, c2 ->
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
        registry.whenChildrenAttached<Child1, Child2>(rootNode.lifecycle) { _, c1, c2 ->
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
        registry.whenChildrenAttached<Child1, Child2>(rootNode.lifecycle) { _, c1, c2 ->
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
        val capturedNodes = ArrayList<Rib>()
        registry.whenChildrenAttached<Child1, Child2>(rootNode.lifecycle) { _, child1, child2 ->
            capturedNodes += child1
            capturedNodes += child2
        }
        assertEquals(
            listOf(child11, child21, child11, child22, child12, child21, child12, child22),
            capturedNodes
        )
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
