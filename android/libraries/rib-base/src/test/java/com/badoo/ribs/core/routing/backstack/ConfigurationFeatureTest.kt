package com.badoo.ribs.core.routing.backstack

import android.os.Bundle
import android.os.Parcelable
import com.badoo.mvicore.element.TimeCapsule
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Node.Descriptor
import com.badoo.ribs.core.Node.ViewAttachMode
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.MultiConfigurationCommand.Sleep
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.MultiConfigurationCommand.WakeUp
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.SingleConfigurationCommand.Activate
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.SingleConfigurationCommand.Add
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.SingleConfigurationCommand.Deactivate
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.SingleConfigurationCommand.Remove
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.INACTIVE
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.SLEEPING
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.Unresolved
import com.badoo.ribs.core.routing.backstack.ConfigurationFeatureTest.Configuration.ContentExternal1
import com.badoo.ribs.core.routing.backstack.ConfigurationFeatureTest.Configuration.ContentExternal2
import com.badoo.ribs.core.routing.backstack.ConfigurationFeatureTest.Configuration.ContentViewParented1
import com.badoo.ribs.core.routing.backstack.ConfigurationFeatureTest.Configuration.ContentViewParented2
import com.badoo.ribs.core.routing.backstack.ConfigurationFeatureTest.Configuration.ContentViewParented3
import com.badoo.ribs.core.routing.backstack.ConfigurationFeatureTest.Configuration.Permanent1
import com.badoo.ribs.core.routing.backstack.ConfigurationFeatureTest.Configuration.Permanent2
import com.badoo.ribs.core.routing.backstack.ConfigurationKey.Content
import com.badoo.ribs.core.routing.backstack.ConfigurationKey.Permanent
import com.badoo.ribs.core.routing.backstack.feature.ConfigurationFeature
import com.badoo.ribs.core.routing.backstack.feature.SavedState
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.clearInvocations
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.android.parcel.Parcelize
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ConfigurationFeatureTest {

    sealed class Configuration : Parcelable {
        @Parcelize object Permanent1 : Configuration()
        @Parcelize object Permanent2 : Configuration()
        @Parcelize object ContentViewParented1 : Configuration()
        @Parcelize object ContentViewParented2 : Configuration()
        @Parcelize object ContentViewParented3 : Configuration()
        @Parcelize object ContentExternal1 : Configuration()
        @Parcelize object ContentExternal2 : Configuration()
    }

    private lateinit var emptyTimeCapsule: TimeCapsule<SavedState<Configuration>>
    private lateinit var restoredTimeCapsule: TimeCapsule<SavedState<Configuration>>
    private lateinit var poolInTimeCapsule: Map<ConfigurationKey, Unresolved<Configuration>>

    private lateinit var feature: ConfigurationFeature<Configuration>
    private lateinit var resolver: (Configuration) -> RoutingAction<*>
    private lateinit var parentNode: Node<*>

    private lateinit var helperPermanent1: ConfigurationTestHelper
    private lateinit var helperPermanent2: ConfigurationTestHelper
    private lateinit var helperContentViewParented1: ConfigurationTestHelper
    private lateinit var helperContentViewParented2: ConfigurationTestHelper
    private lateinit var helperContentViewParented3: ConfigurationTestHelper
    private lateinit var helperContentExternal1: ConfigurationTestHelper
    private lateinit var helperContentExternal2: ConfigurationTestHelper

    data class ConfigurationTestHelper(
        val configuration: Configuration,
        val nodes: List<Descriptor>,
        val bundles: List<Bundle>,
        val nodeFactories: List<() -> Descriptor>,
        val routingAction: RoutingAction<*>
    ) {
        companion object {
            fun create(configuration: Configuration, nbNodes: Int, viewAttachMode: ViewAttachMode): ConfigurationTestHelper {
                val nodes = MutableList(nbNodes) { i ->
                    Descriptor(
                        mock { on { toString() } doReturn "Node #$i of $configuration" },
                        viewAttachMode
                    )
                }
                val bundles = MutableList(nbNodes) { mock<Bundle>() }
                val factories = nodes.toFactory()
                val routingAction: RoutingAction<*> = factories.toRoutingAction()

                return ConfigurationTestHelper(
                    configuration = configuration,
                    nodes = nodes,
                    bundles = bundles,
                    nodeFactories = factories,
                    routingAction = routingAction
                )
            }

            private fun List<Descriptor>.toFactory(): List<() -> Descriptor> =
                map { descriptor ->
                    mock<() -> Descriptor> {
                        on { invoke() } doReturn descriptor
                    }
                }

            private fun List<() -> Descriptor>.toRoutingAction(): RoutingAction<*> =
                mock {
                    on { buildNodes() } doAnswer {
                        this@toRoutingAction.map {
                            factory -> factory.invoke()
                        }
                    }
                }
        }
    }

    @Before
    fun setUp() {
        helperPermanent1 =
            ConfigurationTestHelper.create(Permanent1,2, ViewAttachMode.PARENT)

        helperPermanent2 =
            ConfigurationTestHelper.create(Permanent2,3, ViewAttachMode.PARENT)

        helperContentViewParented1 =
            ConfigurationTestHelper.create(ContentViewParented1,2, ViewAttachMode.PARENT)

        helperContentViewParented2 =
            ConfigurationTestHelper.create(ContentViewParented2,3, ViewAttachMode.PARENT)

        helperContentViewParented3 =
            ConfigurationTestHelper.create(ContentViewParented3,2, ViewAttachMode.PARENT)

        helperContentExternal1 =
            ConfigurationTestHelper.create(ContentExternal1,2, ViewAttachMode.EXTERNAL)

        helperContentExternal2 =
            ConfigurationTestHelper.create(ContentExternal2,3, ViewAttachMode.EXTERNAL)

        val helpers = listOf(
            helperPermanent1,
            helperPermanent2,
            helperContentViewParented1,
            helperContentViewParented2,
            helperContentViewParented3,
            helperContentExternal1,
            helperContentExternal2
        )

        resolver = mock {
            helpers.forEach { helper ->
                on { invoke(helper.configuration) } doReturn helper.routingAction
            }
        }

        poolInTimeCapsule = mapOf(
            Permanent(0) to Unresolved<Configuration>(SLEEPING, Permanent1, helperPermanent1.bundles),
            Permanent(1) to Unresolved<Configuration>(SLEEPING, Permanent2, helperPermanent2.bundles),

            Content(0) to Unresolved<Configuration>(SLEEPING, ContentViewParented1, helperContentViewParented1.bundles),
            Content(1) to Unresolved<Configuration>(SLEEPING, ContentViewParented2, helperContentViewParented2.bundles),
            Content(2) to Unresolved<Configuration>(INACTIVE, ContentViewParented3, helperContentViewParented3.bundles),

            Content(3) to Unresolved<Configuration>(SLEEPING, ContentExternal1, helperContentExternal1.bundles),
            Content(4) to Unresolved<Configuration>(INACTIVE, ContentExternal2, helperContentExternal2.bundles)
        )
        emptyTimeCapsule = mock()
        restoredTimeCapsule = mock {
            on { get<SavedState<Configuration>>(ConfigurationFeature::class.java.name) } doReturn SavedState(
                pool = poolInTimeCapsule
            )
        }
        parentNode = mock()
    }

    private val permanentParts = listOf(
        Permanent1,
        Permanent2
    )

    private fun createFeature(timeCapsule: TimeCapsule<SavedState<Configuration>>): ConfigurationFeature<Configuration> {
        return ConfigurationFeature(
            initialConfigurations = permanentParts,
            timeCapsule = timeCapsule,
            resolver = resolver,
            parentNode = parentNode
        )
    }

    private fun empty() {
        feature = createFeature(emptyTimeCapsule)
    }

    private fun restored() {
        feature = createFeature(restoredTimeCapsule)
    }

    // region Init

    @Test
    fun `On init, ALL initial configuration are added - associated RoutingActions are resolved on demand`() {
        empty()
        verify(resolver).invoke(Permanent1)
        verify(resolver).invoke(Permanent2)
    }

    @Test
    fun `On init, ALL initial configuration are added - Node factories are invoked`() {
        empty()
        helperPermanent1.nodeFactories.forEach { verify(it).invoke() }
        helperPermanent2.nodeFactories.forEach { verify(it).invoke() }
    }

    @Test
    fun `On init, ALL initial configuration are added - Nodes that are created are attached with empty Bundles`() {
        empty()
        helperPermanent1.nodes.forEach { verify(parentNode).attachChildNode(it.node, null) }
        helperPermanent2.nodes.forEach { verify(parentNode).attachChildNode(it.node, null) }
    }

    @Test
    fun `On first WakeUp after init, ALL initial configuration are activated - associated RoutingActions are executed`() {
        empty()
        feature.accept(WakeUp())
        verify(helperPermanent1.routingAction).execute()
        verify(helperPermanent2.routingAction).execute()
    }

    @Test
    fun `On first WakeUp after init, ALL initial configuration are activated - Nodes that are created are attached to the view`() {
        empty()
        feature.accept(WakeUp())
        helperPermanent1.nodes.forEach { verify(parentNode).attachChildView(it.node) }
        helperPermanent2.nodes.forEach { verify(parentNode).attachChildView(it.node) }
    }

    // endregion

    // region Init from SavedState

    @Test
    fun `On WakeUp after init from TimeCapsule, ALL previously ACTIVE configurations are added - associated RoutingActions are resolved on demand`() {
        restored()
        feature.accept(WakeUp())
        verify(resolver).invoke(Permanent1)
        verify(resolver).invoke(Permanent2)
        verify(resolver).invoke(ContentViewParented1)
        verify(resolver).invoke(ContentViewParented2)
        verify(resolver).invoke(ContentExternal1)
    }

    @Test
    fun `On WakeUp after init from TimeCapsule, ALL previously ACTIVE configurations are added - Node factories are invoked`() {
        restored()
        feature.accept(WakeUp())
        helperPermanent1.nodeFactories.forEach { verify(it).invoke() }
        helperPermanent2.nodeFactories.forEach { verify(it).invoke() }
        helperContentViewParented1.nodeFactories.forEach { verify(it).invoke() }
        helperContentViewParented2.nodeFactories.forEach { verify(it).invoke() }
        helperContentExternal1.nodeFactories.forEach { verify(it).invoke() }
    }

    @Test
    fun `On WakeUp after init from TimeCapsule, ALL previously ACTIVE configurations are added - Nodes that are created are attached with their correct Bundles`() {
        restored()
        feature.accept(WakeUp())

        helperPermanent1.nodes.forEachIndexed{ idx, item ->
            verify(parentNode).attachChildNode(item.node, helperPermanent1.bundles[idx])
        }
        helperPermanent2.nodes.forEachIndexed{ idx, item ->
            verify(parentNode).attachChildNode(item.node, helperPermanent2.bundles[idx])
        }
        helperContentViewParented1.nodes.forEachIndexed{ idx, item ->
            verify(parentNode).attachChildNode(item.node, helperContentViewParented1.bundles[idx])
        }
        helperContentViewParented2.nodes.forEachIndexed{ idx, item ->
            verify(parentNode).attachChildNode(item.node, helperContentViewParented2.bundles[idx])
        }
        helperContentExternal1.nodes.forEachIndexed{ idx, item ->
            verify(parentNode).attachChildNode(item.node, helperContentExternal1.bundles[idx])
        }
    }

    @Test
    fun `On WakeUp after init from TimeCapsule, ALL previously ACTIVE configurations are activated - associated RoutingActions are executed`() {
        restored()
        feature.accept(WakeUp())
        verify(helperPermanent1.routingAction).execute()
        verify(helperPermanent2.routingAction).execute()
        verify(helperContentViewParented1.routingAction).execute()
        verify(helperContentViewParented2.routingAction).execute()
        verify(helperContentExternal1.routingAction).execute()
    }

    @Test
    fun `On WakeUp after init from TimeCapsule, ALL previously ACTIVE configurations are activated - Nodes that are created are attached to the view with respect to their ViewAttachMode`() {
        restored()
        feature.accept(WakeUp())
        helperPermanent1.nodes.forEach { verify(parentNode).attachChildView(it.node) }
        helperPermanent2.nodes.forEach { verify(parentNode).attachChildView(it.node) }
        helperContentViewParented1.nodes.forEach { verify(parentNode).attachChildView(it.node) }
        helperContentViewParented2.nodes.forEach { verify(parentNode).attachChildView(it.node) }
        // External should not be attached:
        helperContentExternal1.nodes.forEach { verify(parentNode, never()).attachChildView(it.node) }
    }

    @Test
    fun `On WakeUp after init from TimeCapsule, previously INACTIVE Content parts NOT are added - associated RoutingActions are NOT resolved`() {
        restored()
        feature.accept(WakeUp())
        verify(resolver, never()).invoke(ContentViewParented3)
        verify(resolver, never()).invoke(ContentExternal2)
    }

    @Test
    fun `On WakeUp after init from TimeCapsule, previously INACTIVE Content parts are NOT added - Node factories are NOT invoked`() {
        restored()
        feature.accept(WakeUp())
        helperContentViewParented3.nodeFactories.forEach { verify(it, never()).invoke() }
        helperContentExternal2.nodeFactories.forEach { verify(it, never()).invoke() }
    }

    @Test
    fun `On WakeUp after init from TimeCapsule, previously INACTIVE Content parts are NOT added - Nodes that are created are NOT ttached`() {
        restored()
        feature.accept(WakeUp())
        helperContentViewParented3.nodes.forEach { verify(parentNode, never()).attachChildNode(eq(it.node), anyOrNull()) }
        helperContentExternal2.nodes.forEach { verify(parentNode, never()).attachChildNode(eq(it.node), anyOrNull()) }
    }

    @Test
    fun `On WakeUp after init from TimeCapsule, ALL previously INACTIVE configurations are NOT activated - associated RoutingActions are NOT executed`() {
        restored()
        feature.accept(WakeUp())
        verify(helperContentViewParented3.routingAction, never()).execute()
        verify(helperContentExternal2.routingAction, never()).execute()
    }

    @Test
    fun `On WakeUp after init from TimeCapsule, ALL previously INACTIVE configurations are NOT activated - Nodes that are created are NOT attached to the view`() {
        restored()
        feature.accept(WakeUp())
        helperContentViewParented3.nodes.forEach { verify(parentNode, never()).attachChildView(it.node) }
        helperContentExternal2.nodes.forEach { verify(parentNode, never()).attachChildView(it.node) }
    }

    // endregion

    // region Add
    @Test
    fun `On Add, Node factories are invoked`() {
        empty()
        feature.accept(Add(Content(0), ContentViewParented1))
        helperContentViewParented1.nodeFactories.forEach {
            verify(it).invoke()
        }
    }

    @Test
    fun `On Add, Nodes that are created are attached with empty Bundles`() {
        empty()
        feature.accept(Add(Content(0), ContentViewParented1))
        helperContentViewParented1.nodes.forEach {
            verify(parentNode).attachChildNode(it.node, null)
        }
    }

    @Test
    fun `On Add, associated RoutingAction is resolved on demand`() {
        empty()
        feature.accept(Add(Content(0), ContentViewParented1))
        verify(resolver).invoke(ContentViewParented1)
    }

    @Test
    fun `On Add, associated RoutingAction is not yet executed`() {
        empty()
        feature.accept(Add(Content(0), ContentViewParented1))
        verify(helperContentViewParented1.routingAction, never()).execute()
    }
    // endregion

    // region Activate
    @Test
    fun `On Activate BEFORE WakeUp, associated RoutingAction is NOT yet executed`() {
        empty()
        feature.accept(Add(Content(0), ContentViewParented1))
        feature.accept(Activate(Content(0)))
        verify(helperContentViewParented1.routingAction, never()).execute()
    }

    @Test
    fun `On Activate BEFORE WakeUp, attachChildView() is NOT yet called on associated Nodes that are view-parented`() {
        empty()
        feature.accept(Add(Content(0), ContentViewParented1))
        feature.accept(Activate(Content(0)))
        helperContentViewParented1.nodes.forEach {
            verify(parentNode, never()).attachChildView(it.node)
        }
    }

    @Test
    fun `On Activate BEFORE WakeUp, associated RoutingAction is executed AUTOMATICALLY AFTER next WakeUp`() {
        empty()
        feature.accept(Add(Content(0), ContentViewParented1))
        feature.accept(Activate(Content(0)))
        feature.accept(WakeUp())
        verify(helperContentViewParented1.routingAction).execute()
    }

    @Test
    fun `On Activate BEFORE WakeUp, attachChildView() is called AUTOMATICALLY AFTER next WakeUp on associated Nodes that are view-parented`() {
        empty()
        feature.accept(Add(Content(0), ContentViewParented1))
        feature.accept(Activate(Content(0)))
        feature.accept(WakeUp())
        helperContentViewParented1.nodes.forEach {
            verify(parentNode).attachChildView(it.node)
        }
    }

    @Test
    fun `On Activate AFTER WakeUp, associated RoutingAction is executed`() {
        empty()
        feature.accept(WakeUp())
        feature.accept(Add(Content(0), ContentViewParented1))
        feature.accept(Activate(Content(0)))
        verify(helperContentViewParented1.routingAction).execute()
    }

    @Test
    fun `On Activate AFTER WakeUp, attachChildView() is called on associated Nodes that are view-parented`() {
        empty()
        feature.accept(WakeUp())
        feature.accept(Add(Content(0), ContentViewParented1))
        feature.accept(Activate(Content(0)))
        helperContentViewParented1.nodes.forEach {
            verify(parentNode).attachChildView(it.node)
        }
    }

    @Test
    fun `On Activate AFTER WakeUp, attachChildView() is NOT called on associated Nodes that are NOT view-parented`() {
        empty()
        feature.accept(WakeUp())
        feature.accept(Add(Content(0), ContentExternal1))
        feature.accept(Activate(Content(0)))
        helperContentViewParented1.nodes.forEach {
            verify(parentNode, never()).attachChildView(it.node)
        }
    }

    @Test
    fun `On Activate on ALREADY ACTIVE configuration, associated RoutingAction is NOT executed again`() {
        empty()
        feature.accept(WakeUp())
        feature.accept(Add(Content(0), ContentViewParented1))
        feature.accept(Activate(Content(0)))
        feature.accept(Activate(Content(0)))
        verify(helperContentViewParented1.routingAction, times(1)).execute()
    }

    @Test
    fun `On Activate on ALREADY ACTIVE configuration, attachChildView() is NOT called again`() {
        empty()
        feature.accept(WakeUp())
        feature.accept(Add(Content(0), ContentViewParented1))
        feature.accept(Activate(Content(0)))
        feature.accept(Activate(Content(0)))
        helperContentViewParented1.nodes.forEach {
            verify(parentNode, times(1)).attachChildView(it.node)
        }
    }
    // endregion

    // region Deactivate
    @Test
    fun `On Deactivate, cleanup() is called on associated RoutingAction`() {
        empty()
        feature.accept(Add(Content(0), ContentViewParented1))
        feature.accept(Deactivate(Content(0)))
        verify(helperContentViewParented1.routingAction).cleanup()
    }

    @Test
    fun `On Deactivate, saveViewState() is called on associated Nodes`() {
        empty()
        feature.accept(Add(Content(0), ContentViewParented1))
        feature.accept(Deactivate(Content(0)))
        helperContentViewParented1.nodes.forEach {
            verify(it.node).saveViewState()
        }
    }

    @Test
    fun `On Deactivate, detachChildView() is called on associated Nodes that are view-parented`() {
        empty()
        feature.accept(Add(Content(0), ContentViewParented1))
        feature.accept(Deactivate(Content(0)))
        helperContentViewParented1.nodes.forEach {
            verify(parentNode).detachChildView(it.node)
        }
    }

    @Test
    fun `On Deactivate, detachChildView() is NOT called on associated Nodes that are NOT view-parented`() {
        empty()
        feature.accept(Add(Content(0), ContentExternal1))
        feature.accept(Deactivate(Content(0)))
        helperContentViewParented1.nodes.forEach {
            verify(parentNode, never()).detachChildView(it.node)
        }
    }

    @Test
    fun `On Deactivate, Node references are kept`() {
        empty()
        feature.accept(Add(Content(0), ContentViewParented1))
        feature.accept(Deactivate(Content(0)))
        val configurationContext = feature.state.pool[Content(0)]
        assertEquals(true, configurationContext is Resolved)
        assertEquals(helperContentViewParented1.nodes, (configurationContext as? Resolved)?.nodes)
    }
    // endregion

    // region Remove
    @Test
    fun `On Remove, all of its Nodes are detached regardless of view-parenting mode`() {
        empty()
        feature.accept(Add(Content(0), ContentViewParented1))
        feature.accept(Add(Content(1), ContentExternal1))
        feature.accept(Remove(Content(1)))
        feature.accept(Remove(Content(0)))

        helperContentExternal1.nodes.forEach {
            inOrder(parentNode) {
                verify(parentNode).detachChildView(it.node)
                verify(parentNode).detachChildNode(it.node)
            }
        }
        helperContentViewParented1.nodes.forEach {
            inOrder(parentNode) {
                verify(parentNode).detachChildView(it.node)
                verify(parentNode).detachChildNode(it.node)
            }
        }
    }

    @Test
    fun `On Remove all added elements, only permanent parts are left in the pool`() {
        empty()
        feature.accept(Add(Content(0), ContentViewParented1))
        feature.accept(Add(Content(1), ContentExternal1))
        feature.accept(Remove(Content(1)))
        feature.accept(Remove(Content(0)))
        val configurationsLeftInPool = feature.state.pool.map {
            it.key to it.value.configuration
        }
        assertEquals(permanentParts, configurationsLeftInPool)
    }
    // endregion

    // region Sleep
    @Test
    fun `On Sleep after WakeUp, cleanup() is called on associated RoutingAction`() {
        empty()
        feature.accept(WakeUp())
        feature.accept(Add(Content(0), ContentViewParented1))
        feature.accept(Add(Content(1), ContentExternal1))
        feature.accept(Activate(Content(0)))
        clearInvocations(parentNode)
        feature.accept(Sleep())

        verify(helperContentViewParented1.routingAction).cleanup()
    }

    @Test
    fun `On Sleep after WakeUp, saveViewState() is called on every ACTIVE node`() {
        empty()
        feature.accept(WakeUp())
        feature.accept(Add(Content(0), ContentViewParented1))
        feature.accept(Add(Content(1), ContentExternal1))
        feature.accept(Activate(Content(0)))
        clearInvocations(parentNode)
        feature.accept(Sleep())

        helperContentViewParented1.nodes.forEach {
            verify(it.node).saveViewState()
        }
    }

    @Test
    fun `On Sleep after WakeUp, detachChildView() is called on every ACTIVE node that are view-parented`() {
        empty()
        feature.accept(WakeUp())
        feature.accept(Add(Content(0), ContentViewParented1))
        feature.accept(Add(Content(1), ContentExternal1))
        feature.accept(Activate(Content(0)))
        clearInvocations(parentNode)
        feature.accept(Sleep())

        helperContentViewParented1.nodes.forEach {
            verify(parentNode).detachChildView(it.node)
        }
    }
    // endregion

    // region WakeUp
    @Test
    fun `On WakeUp after Sleep, execute() is called on associated RoutingAction`() {
        empty()
        feature.accept(WakeUp())
        feature.accept(Add(Content(0), ContentViewParented1))
        feature.accept(Add(Content(1), ContentExternal1))
        feature.accept(Activate(Content(0)))
        feature.accept(Sleep())
        clearInvocations(helperContentViewParented1.routingAction)
        feature.accept(WakeUp())

        verify(helperContentViewParented1.routingAction).execute()
    }

    @Test
    fun `On WakeUp after Sleep, attachChildView() is called on every ACTIVE node that are view-parented`() {
        empty()
        feature.accept(WakeUp())
        feature.accept(Add(Content(0), ContentViewParented1))
        feature.accept(Add(Content(1), ContentExternal1))
        feature.accept(Activate(Content(0)))
        feature.accept(Sleep())
        clearInvocations(parentNode)
        feature.accept(WakeUp())

        helperContentViewParented1.nodes.forEach {
            verify(parentNode).attachChildView(it.node)
        }
    }
    // endregion
}
