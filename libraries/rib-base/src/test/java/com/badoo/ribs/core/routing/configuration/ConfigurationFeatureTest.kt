package com.badoo.ribs.core.routing.configuration

import android.os.Bundle
import android.os.Parcelable
import com.badoo.mvicore.element.TimeCapsule
import com.badoo.ribs.core.ActivationMode
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.builder.BuildContext
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.activator.RoutingActivator
import com.badoo.ribs.core.routing.configuration.RoutingCommand.Activate
import com.badoo.ribs.core.routing.configuration.RoutingCommand.Add
import com.badoo.ribs.core.routing.configuration.RoutingCommand.Deactivate
import com.badoo.ribs.core.routing.configuration.RoutingCommand.Remove
import com.badoo.ribs.core.routing.configuration.RoutingContext.ActivationState.INACTIVE
import com.badoo.ribs.core.routing.configuration.RoutingContext.ActivationState.SLEEPING
import com.badoo.ribs.core.routing.configuration.RoutingContext.Resolved
import com.badoo.ribs.core.routing.configuration.RoutingContext.Unresolved
import com.badoo.ribs.core.routing.configuration.ConfigurationFeatureTest.Configuration.ContentExternal1
import com.badoo.ribs.core.routing.configuration.ConfigurationFeatureTest.Configuration.ContentExternal2
import com.badoo.ribs.core.routing.configuration.ConfigurationFeatureTest.Configuration.ContentViewParented1
import com.badoo.ribs.core.routing.configuration.ConfigurationFeatureTest.Configuration.ContentViewParented2
import com.badoo.ribs.core.routing.configuration.ConfigurationFeatureTest.Configuration.ContentViewParented3
import com.badoo.ribs.core.routing.configuration.ConfigurationFeatureTest.Configuration.Permanent1
import com.badoo.ribs.core.routing.configuration.ConfigurationFeatureTest.Configuration.Permanent2
import com.badoo.ribs.core.routing.configuration.Transaction.MultiConfigurationCommand.Sleep
import com.badoo.ribs.core.routing.configuration.Transaction.MultiConfigurationCommand.WakeUp
import com.badoo.ribs.core.routing.configuration.feature.ConfigurationFeature
import com.badoo.ribs.core.routing.configuration.feature.SavedState
import com.badoo.ribs.core.routing.history.Routing
import com.badoo.ribs.core.routing.history.Routing.Identifier
import com.nhaarman.mockitokotlin2.clearInvocations
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.android.parcel.Parcelize
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList

// FIXME rework test suite -- many of the responsibilities has been moved out to other classes
//  TODO test only remaining responsibilities without assumptions on view detach / attach etc.
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
    private lateinit var poolInTimeCapsule: Map<Routing<Configuration>, Unresolved<Configuration>>

    private lateinit var feature: ConfigurationFeature<Configuration>
    private lateinit var resolver: RoutingResolver<Configuration>
    private lateinit var parentNode: Node<Nothing>

    private lateinit var helperPermanent1: ConfigurationTestHelper
    private lateinit var helperPermanent2: ConfigurationTestHelper
    private lateinit var helperContentViewParented1: ConfigurationTestHelper
    private lateinit var helperContentViewParented2: ConfigurationTestHelper
    private lateinit var helperContentViewParented3: ConfigurationTestHelper
    private lateinit var helperContentExternal1: ConfigurationTestHelper
    private lateinit var helperContentExternal2: ConfigurationTestHelper

    data class ConfigurationTestHelper(
        val routing: Routing<Configuration>,
        val nodes: List<Node<Nothing>>,
        val bundles: List<Bundle>,
        val nodeFactories: List<() -> Rib>,
        val routingAction: RoutingAction
    ) {
        companion object {
            fun create(routing: Routing<Configuration>, nbNodes: Int, viewActivationMode: ActivationMode): ConfigurationTestHelper {
                val nodes = MutableList(nbNodes) { i ->
                    mock<Node<Nothing>> {
                        on { this.buildContext } doReturn BuildContext.root(null)
                        on { this.activationMode } doReturn viewActivationMode
                        on { toString() } doReturn "Node #$i of ${routing.configuration}"
                    }
                }
                val bundles = MutableList(nbNodes) { mock<Bundle>() }
                val factories = nodes.toFactory()
                val routingAction: RoutingAction = factories.toRoutingAction(nbNodes)

                return ConfigurationTestHelper(
                    routing = routing,
                    nodes = nodes,
                    bundles = bundles,
                    nodeFactories = factories,
                    routingAction = routingAction
                )
            }

            private fun List<Node<Nothing>>.toFactory(): List<() -> Rib> =
                map { node ->
                    mock<() -> Rib> {
                        on { invoke() } doReturn object : Rib {
                            override val node = node
                        }
                    }
                }

            private fun List<() -> Rib>.toRoutingAction(nbNodes: Int): RoutingAction =
                mock {
                    on { nbNodesToBuild } doReturn nbNodes
                    on { buildNodes(anyList()) } doAnswer {
                        this@toRoutingAction.map {
                            factory -> factory.invoke()
                        }
                    }
                }
        }
    }

    @Before
    fun setUp() {
        val routingPermanent1 = Routing(identifier = Identifier("Permanent 0"), configuration = Permanent1 as Configuration)
        val routingPermanent2 = Routing(identifier = Identifier("Permanent 1"), configuration = Permanent2 as Configuration)
        val routingContentViewParented1 = Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1 as Configuration)
        val routingContentViewParented2 = Routing(identifier = Identifier("Content 1"), configuration = ContentViewParented2 as Configuration)
        val routingContentViewParented3 = Routing(identifier = Identifier("Content 2"), configuration = ContentViewParented3 as Configuration)
        val routingContentExternal1 = Routing(identifier = Identifier("Content 3"), configuration = ContentExternal1 as Configuration)
        val routingContentExternal2 = Routing(identifier = Identifier("Content 4"), configuration = ContentExternal2 as Configuration)

        helperPermanent1 =
            ConfigurationTestHelper.create(routingPermanent1,2, ActivationMode.ATTACH_TO_PARENT)

        helperPermanent2 =
            ConfigurationTestHelper.create(routingPermanent2,3, ActivationMode.ATTACH_TO_PARENT)

        helperContentViewParented1 =
            ConfigurationTestHelper.create(routingContentViewParented1,2, ActivationMode.ATTACH_TO_PARENT)

        helperContentViewParented2 =
            ConfigurationTestHelper.create(routingContentViewParented2,3, ActivationMode.ATTACH_TO_PARENT)

        helperContentViewParented3 =
            ConfigurationTestHelper.create(routingContentViewParented3,2, ActivationMode.ATTACH_TO_PARENT)

        helperContentExternal1 =
            ConfigurationTestHelper.create(routingContentExternal1,2, ActivationMode.CLIENT)

        helperContentExternal2 =
            ConfigurationTestHelper.create(routingContentExternal2,3, ActivationMode.CLIENT)

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
                on { resolve(helper.routing) } doReturn helper.routingAction
            }
        }

        val routingsforTimeCapsule = listOf(
            routingPermanent1,
            routingPermanent2,
            routingContentViewParented1,
            routingContentViewParented2,
            routingContentViewParented3,
            routingContentExternal1,
            routingContentExternal2
        )

        val poolInTimeCapsule = mapOf(
            routingsforTimeCapsule[0] to Unresolved(SLEEPING, routingsforTimeCapsule[0], helperPermanent1.bundles),
            routingsforTimeCapsule[1] to Unresolved(SLEEPING, routingsforTimeCapsule[1], helperPermanent2.bundles),

            routingsforTimeCapsule[2] to Unresolved(SLEEPING, routingsforTimeCapsule[2], helperContentViewParented1.bundles),
            routingsforTimeCapsule[3] to Unresolved(SLEEPING, routingsforTimeCapsule[3], helperContentViewParented2.bundles),
            routingsforTimeCapsule[4] to Unresolved(INACTIVE, routingsforTimeCapsule[4], helperContentViewParented3.bundles),

            routingsforTimeCapsule[5] to Unresolved(SLEEPING, routingsforTimeCapsule[5], helperContentExternal1.bundles),
            routingsforTimeCapsule[6] to Unresolved(INACTIVE, routingsforTimeCapsule[6], helperContentExternal2.bundles)
        )

        emptyTimeCapsule = mock()
        restoredTimeCapsule = mock {
            on { get<SavedState<Configuration>>(ConfigurationFeature::class.java.name) } doReturn SavedState(
                pool = poolInTimeCapsule
            )
        }
        parentNode = mock {
            on { this.buildContext } doReturn BuildContext.root(null)
        }
    }

    private val permanentParts = listOf(
        Routing(Permanent1),
        Routing(Permanent2)
    )

    private val routingActivator: RoutingActivator<Configuration> = mock()

    private fun createFeature(timeCapsule: TimeCapsule<SavedState<Configuration>>): ConfigurationFeature<Configuration> {
        return ConfigurationFeature(
            timeCapsule = timeCapsule,
            resolver = resolver,
            parentNode = parentNode,
            transitionHandler = null,
            activator = routingActivator
        )
    }

    private fun createEmptyFeature() {
        feature = createFeature(emptyTimeCapsule).apply {
            addPermanents()
        }
    }

    private fun createRestoredFeature() {
        feature = createFeature(restoredTimeCapsule)
    }

    // For backwards compatibility with legacy testing approaches until test suite is reworked -- still
    //  better than throwing them away
    private fun ConfigurationFeature<Configuration>.addPermanents() {
        accept(Transaction.from(
            Add(helperPermanent1.routing),
            Add(helperPermanent2.routing),
            Activate(helperPermanent1.routing),
            Activate(helperPermanent2.routing)
        ))
    }

    // region Init

    @Test
    fun `On init, ALL initial configuration are added - associated RoutingActions are resolved on demand`() {
        createEmptyFeature()
        verify(resolver).resolve(helperPermanent1.routing)
        verify(resolver).resolve(helperPermanent2.routing)
    }

    @Test
    fun `On init, ALL initial configuration are added - Node factories are invoked`() {
        createEmptyFeature()
        helperPermanent1.nodeFactories.forEach { verify(it).invoke() }
        helperPermanent2.nodeFactories.forEach { verify(it).invoke() }
    }

    /**
     * Test fails: `attachChildNode` is not called because of mock implementations.
     *
     * Instead of fixing this here, the whole test suite should be refactored, as now there are more
     * layers of abstraction than when it was first created.
     * This class should only test for Action execution.
     * Results of actions should be tested on the Actions themselves.
     */
    @Test
    @Ignore("The whole test suite should be refactored.")
    fun `On init, ALL initial configuration are added - Nodes that are created are attached with empty Bundles`() {
        createEmptyFeature()
        helperPermanent1.nodes.forEach { verify(parentNode).attachChildNode(it) }
        helperPermanent2.nodes.forEach { verify(parentNode).attachChildNode(it) }
    }

    @Test
    fun `On first WakeUp after init, ALL initial configuration are activated - associated RoutingActions are executed`() {
        createEmptyFeature()
        feature.accept(WakeUp())
        verify(helperPermanent1.routingAction).execute()
        verify(helperPermanent2.routingAction).execute()
    }

    @Test
    fun `On first WakeUp after init, ALL initial configuration are activated - Nodes that are created are attached to the view`() {
        createEmptyFeature()
        feature.accept(WakeUp())
        verify(routingActivator).activate(helperPermanent1.routing, helperPermanent1.nodes)
        verify(routingActivator).activate(helperPermanent2.routing, helperPermanent2.nodes)
    }

    // endregion

    // region Init from SavedState

    @Test
    fun `On WakeUp after init from TimeCapsule, ALL previously ACTIVE configurations are added - associated RoutingActions are resolved on demand`() {
        createRestoredFeature()
        feature.accept(WakeUp())
        verify(resolver).resolve(helperPermanent1.routing)
        verify(resolver).resolve(helperPermanent2.routing)
        verify(resolver).resolve(helperContentViewParented1.routing)
        verify(resolver).resolve(helperContentViewParented2.routing)
        verify(resolver).resolve(helperContentExternal1.routing)
    }

    @Test
    fun `On WakeUp after init from TimeCapsule, ALL previously ACTIVE configurations are added - Node factories are invoked`() {
        createRestoredFeature()
        feature.accept(WakeUp())
        helperPermanent1.nodeFactories.forEach { verify(it).invoke() }
        helperPermanent2.nodeFactories.forEach { verify(it).invoke() }
        helperContentViewParented1.nodeFactories.forEach { verify(it).invoke() }
        helperContentViewParented2.nodeFactories.forEach { verify(it).invoke() }
        helperContentExternal1.nodeFactories.forEach { verify(it).invoke() }
    }

    @Test
    fun `On WakeUp after init from TimeCapsule, ALL previously ACTIVE configurations are added - Nodes that are created are attached with their correct Bundles`() {
        createRestoredFeature()
        feature.accept(WakeUp())

        verify(routingActivator).add(helperPermanent1.routing, helperPermanent1.nodes)
        verify(routingActivator).add(helperPermanent2.routing, helperPermanent2.nodes)
        verify(routingActivator).add(helperContentViewParented1.routing, helperContentViewParented1.nodes)
        verify(routingActivator).add(helperContentViewParented2.routing, helperContentViewParented2.nodes)
        verify(routingActivator).add(helperContentExternal1.routing, helperContentExternal1.nodes)
    }

    @Test
    fun `On WakeUp after init from TimeCapsule, ALL previously ACTIVE configurations are activated - associated RoutingActions are executed`() {
        createRestoredFeature()
        feature.accept(WakeUp())
        verify(helperPermanent1.routingAction).execute()
        verify(helperPermanent2.routingAction).execute()
        verify(helperContentViewParented1.routingAction).execute()
        verify(helperContentViewParented2.routingAction).execute()
        verify(helperContentExternal1.routingAction).execute()
    }

    @Test
    fun `On WakeUp after init from TimeCapsule, ALL previously ACTIVE configurations are activated - Nodes that are created are attached to the view with respect to their ViewAttachMode`() {
        createRestoredFeature()
        feature.accept(WakeUp())
        verify(routingActivator).activate(helperPermanent1.routing, helperPermanent1.nodes)
        verify(routingActivator).activate(helperPermanent2.routing, helperPermanent2.nodes)
        verify(routingActivator).activate(helperContentViewParented1.routing, helperContentViewParented1.nodes)
        verify(routingActivator).activate(helperContentViewParented2.routing, helperContentViewParented2.nodes)
        verify(routingActivator).activate(helperContentExternal1.routing, helperContentExternal1.nodes)

        // As these were INACTIVE and shouldn't be reactivated after WakeUp
        verify(routingActivator, never()).activate(helperContentViewParented3.routing, helperContentViewParented3.nodes)
        verify(routingActivator, never()).activate(helperContentExternal2.routing, helperContentExternal2.nodes)
    }

    @Test
    fun `On WakeUp after init from TimeCapsule, previously INACTIVE Content parts NOT are added - associated RoutingActions are NOT resolved`() {
        createRestoredFeature()
        feature.accept(WakeUp())
        verify(resolver, never()).resolve(Routing(ContentViewParented3))
        verify(resolver, never()).resolve(Routing(ContentExternal2))
    }

    @Test
    fun `On WakeUp after init from TimeCapsule, previously INACTIVE Content parts are NOT added - Node factories are NOT invoked`() {
        createRestoredFeature()
        feature.accept(WakeUp())
        helperContentViewParented3.nodeFactories.forEach { verify(it, never()).invoke() }
        helperContentExternal2.nodeFactories.forEach { verify(it, never()).invoke() }
    }

    @Test
    fun `On WakeUp after init from TimeCapsule, previously INACTIVE Content parts are NOT added - Nodes that are created are NOT ttached`() {
        createRestoredFeature()
        feature.accept(WakeUp())
        helperContentViewParented3.nodes.forEach { verify(parentNode, never()).attachChildNode(eq(it)) }
        helperContentExternal2.nodes.forEach { verify(parentNode, never()).attachChildNode(eq(it)) }
    }

    @Test
    fun `On WakeUp after init from TimeCapsule, ALL previously INACTIVE configurations are NOT activated - associated RoutingActions are NOT executed`() {
        createRestoredFeature()
        feature.accept(WakeUp())
        verify(helperContentViewParented3.routingAction, never()).execute()
        verify(helperContentExternal2.routingAction, never()).execute()
    }

    @Test
    fun `On WakeUp after init from TimeCapsule, ALL previously INACTIVE configurations are NOT activated - Nodes that are created are NOT attached to the view`() {
        createRestoredFeature()
        feature.accept(WakeUp())
        helperContentViewParented3.nodes.forEach { verify(parentNode, never()).attachChildView(it) }
        helperContentExternal2.nodes.forEach { verify(parentNode, never()).attachChildView(it) }
    }

    // endregion

    // region Add
    @Test
    fun `On Add, Node factories are invoked`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(helperContentViewParented1.routing)
        ))
        helperContentViewParented1.nodeFactories.forEach {
            verify(it).invoke()
        }
    }

    @Test
    fun `On Add TWICE, Node factories are NOT invoked again`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(helperContentViewParented1.routing),
            Add(helperContentViewParented1.routing)
        ))
        helperContentViewParented1.nodeFactories.forEach {
            verify(it, times(1)).invoke()
        }
    }

    /**
     * Test fails: `attachChildNode` is not called because of mock implementations.
     *
     * Instead of fixing this here, the whole test suite should be refactored, as now there are more
     * layers of abstraction than when it was first created.
     * This class should only test for Action execution.
     * Results of actions should be tested on the Actions themselves.
     */
    @Test
    @Ignore("The whole test suite should be refactored.")
    fun `On Add, Nodes that are created are attached with empty Bundles`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(helperContentViewParented1.routing)
        ))
        helperContentViewParented1.nodes.forEach {
            verify(parentNode).attachChildNode(it)
        }
    }

    /**
     * Test fails: `attachChildNode` is not called because of mock implementations.
     *
     * Instead of fixing this here, the whole test suite should be refactored, as now there are more
     * layers of abstraction than when it was first created.
     * This class should only test for Action execution.
     * Results of actions should be tested on the Actions themselves.
     */
    @Test
    @Ignore("The whole test suite should be refactored.")
    fun `On Add TWICE, Nodes are NOT added again`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(helperContentViewParented1.routing),
            Add(helperContentViewParented1.routing)
        ))
        helperContentViewParented1.nodes.forEach {
            verify(parentNode, times(1)).attachChildNode(it)
        }
    }

    @Test
    fun `On Add, associated RoutingAction is resolved on demand`() {
        createEmptyFeature()
        feature.accept(Transaction.from(Add(helperContentViewParented1.routing)))
        verify(resolver).resolve(helperContentViewParented1.routing)
    }

    @Test
    fun `On Add, associated RoutingAction is not yet executed`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(helperContentViewParented1.routing)
        ))
        verify(helperContentViewParented1.routingAction, never()).execute()
    }
    // endregion

    // region Activate
    @Test
    fun `On Activate BEFORE WakeUp, associated RoutingAction is NOT yet executed`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(helperContentViewParented1.routing),
            Activate(helperContentViewParented1.routing)
        ))
        verify(helperContentViewParented1.routingAction, never()).execute()
    }

    @Test
    fun `On Activate BEFORE WakeUp, attachChildView() is NOT yet called on associated Nodes that are view-parented`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(helperContentViewParented1.routing),
            Activate(helperContentViewParented1.routing)
        ))

        verify(routingActivator, never()).activate(helperContentViewParented1.routing, helperContentViewParented1.nodes)
    }

    @Test
    fun `On Activate BEFORE WakeUp, associated RoutingAction is executed AUTOMATICALLY AFTER next WakeUp`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(helperContentViewParented1.routing),
            Activate(helperContentViewParented1.routing)
        ))
        feature.accept(WakeUp())
        verify(helperContentViewParented1.routingAction).execute()
    }

    @Test
    fun `On Activate BEFORE WakeUp, attachChildView() is called AUTOMATICALLY AFTER next WakeUp on associated Nodes that are view-parented`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(helperContentViewParented1.routing),
            Activate(helperContentViewParented1.routing)
        ))
        feature.accept(WakeUp())
        verify(routingActivator).activate(helperContentViewParented1.routing, helperContentViewParented1.nodes)
    }

    @Test
    fun `On Activate AFTER WakeUp, associated RoutingAction is executed`() {
        createEmptyFeature()
        feature.accept(WakeUp())
        feature.accept(Transaction.from(
            Add(helperContentViewParented1.routing),
            Activate(helperContentViewParented1.routing)
        ))
        verify(helperContentViewParented1.routingAction).execute()
    }

    @Test
    fun `On Activate AFTER WakeUp, attachChildView() is called on associated Nodes`() {
        createEmptyFeature()
        feature.accept(WakeUp())
        feature.accept(Transaction.from(
            Add(helperContentViewParented1.routing),
            Activate(helperContentViewParented1.routing)
        ))


        verify(routingActivator).activate(helperContentViewParented1.routing, helperContentViewParented1.nodes)
    }

    @Test
    fun `On Activate on ALREADY ACTIVE configuration, associated RoutingAction is NOT executed again`() {
        createEmptyFeature()
        feature.accept(WakeUp())
        feature.accept(Transaction.from(
            Add(helperContentViewParented1.routing),
            Activate(helperContentViewParented1.routing)
        ))
        feature.accept(Transaction.from(
            Activate(helperContentViewParented1.routing)
        ))
        verify(helperContentViewParented1.routingAction, times(1)).execute()
    }

    @Test
    fun `On Activate on ALREADY ACTIVE configuration, attachChildView() is NOT called again`() {
        createEmptyFeature()
        feature.accept(WakeUp())
        feature.accept(Transaction.from(
            Add(helperContentViewParented1.routing),
            Activate(helperContentViewParented1.routing)
        ))
        feature.accept(Transaction.from(
            Activate(helperContentViewParented1.routing)
        ))
        verify(routingActivator, times(1)).activate(helperContentViewParented1.routing, helperContentViewParented1.nodes)
    }
    // endregion

    // region Deactivate
    @Test
    fun `On Deactivate, cleanup() is called on associated RoutingAction`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(helperContentViewParented1.routing),
            Deactivate(helperContentViewParented1.routing)
        ))
        verify(helperContentViewParented1.routingAction).cleanup()
    }

    /**
     * Test fails: `saveViewState` is not called because of mock implementations.
     *
     * Instead of fixing this here, the whole test suite should be refactored, as now there are more
     * layers of abstraction than when it was first created.
     * This class should only test for Action execution.
     * Results of actions should be tested on the Actions themselves.
     */
    @Test
    @Ignore("The whole test suite should be refactored.")
    fun `On Deactivate, saveViewState() is called on associated Nodes`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(helperContentViewParented1.routing),
            Deactivate(helperContentViewParented1.routing)
        ))
        helperContentViewParented1.nodes.forEach {
            verify(it).saveViewState()
        }
    }

    /**
     * Test fails: `detachChildView` is not called because of mock implementations.
     *
     * Instead of fixing this here, the whole test suite should be refactored, as now there are more
     * layers of abstraction than when it was first created.
     * This class should only test for Action execution.
     * Results of actions should be tested on the Actions themselves.
     */
    @Test
    @Ignore("The whole test suite should be refactored.")
    fun `On Deactivate, detachChildView() is called on associated Nodes that are view-parented`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(helperContentViewParented1.routing),
            Deactivate(helperContentViewParented1.routing)
        ))

        verify(routingActivator).deactivate(helperContentViewParented1.routing, helperContentViewParented1.nodes)
    }

    @Test
    fun `On Deactivate, Node references are kept`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(helperContentViewParented1.routing),
            Deactivate(helperContentViewParented1.routing)
        ))
        val configurationContext = feature.state.pool[helperContentViewParented1.routing]
        assertEquals(true, configurationContext is Resolved)
        assertEquals(helperContentViewParented1.nodes, (configurationContext as? Resolved)?.nodes)
    }
    // endregion

    // region Remove
    @Test
    fun `On Remove, all of its Nodes are detached regardless of view-parenting mode`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(helperContentViewParented1.routing),
            Add(helperContentExternal1.routing),
            Remove(helperContentViewParented1.routing),
            Remove(helperContentExternal1.routing)
        ))

        verify(routingActivator).remove(helperContentViewParented1.routing, helperContentViewParented1.nodes)
        verify(routingActivator).remove(helperContentExternal1.routing, helperContentExternal1.nodes)
    }
    // endregion

    // region Sleep
    @Test
    fun `On Sleep after WakeUp, cleanup() is called on associated RoutingAction`() {
        createEmptyFeature()
        feature.accept(WakeUp())
        feature.accept(Transaction.from(
            Add(helperContentViewParented1.routing),
            Add(helperContentExternal1.routing),
            Activate(helperContentViewParented1.routing)
        ))
        clearInvocations(parentNode)
        feature.accept(Sleep())

        verify(helperContentViewParented1.routingAction).cleanup()
    }

    /**
     * Test fails: `saveViewState` is not called because of mock implementations.
     *
     * Instead of fixing this here, the whole test suite should be refactored, as now there are more
     * layers of abstraction than when it was first created.
     * This class should only test for Action execution.
     * Results of actions should be tested on the Actions themselves.
     */
    @Test
    @Ignore("This should be tested in RoutingActivator")
    fun `On Sleep after WakeUp, saveViewState() is called on every ACTIVE node`() {
        createEmptyFeature()
        feature.accept(WakeUp())
        feature.accept(Transaction.from(
            Add(helperContentViewParented1.routing),
            Add(helperContentExternal1.routing),
            Activate(helperContentViewParented1.routing)
        ))
        clearInvocations(parentNode)
        feature.accept(Sleep())

        helperContentViewParented1.nodes.forEach {
            verify(it).saveViewState()
        }
    }

    /**
     * Test fails: `detachChildView` is not called because of mock implementations.
     *
     * Instead of fixing this here, the whole test suite should be refactored, as now there are more
     * layers of abstraction than when it was first created.
     * This class should only test for Action execution.
     * Results of actions should be tested on the Actions themselves.
     */
    @Test
    @Ignore
    fun `On Sleep after WakeUp, detachChildView() is called on every ACTIVE node that are view-parented`() {
        createEmptyFeature()
        feature.accept(WakeUp())
        feature.accept(Transaction.from(
            Add(helperContentViewParented1.routing),
            Add(helperContentExternal1.routing),
            Activate(helperContentViewParented1.routing)
        ))
        clearInvocations(parentNode)
        feature.accept(Sleep())

        verify(routingActivator).deactivate(helperContentViewParented1.routing, helperContentViewParented1.nodes)
    }
    // endregion

    // region WakeUp
    @Test
    fun `On WakeUp after Sleep, execute() is called on associated RoutingAction`() {
        createEmptyFeature()
        feature.accept(WakeUp())
        feature.accept(Transaction.from(
            Add(helperContentViewParented1.routing),
            Add(helperContentExternal1.routing),
            Activate(helperContentViewParented1.routing)
        ))
        feature.accept(Sleep())
        clearInvocations(helperContentViewParented1.routingAction)
        feature.accept(WakeUp())

        verify(helperContentViewParented1.routingAction).execute()
    }

    @Test
    fun `On WakeUp after Sleep, attachChildView() is called on every ACTIVE node that are view-parented`() {
        createEmptyFeature()
        feature.accept(WakeUp())
        feature.accept(Transaction.from(
            Add(helperContentViewParented1.routing),
            Add(helperContentExternal1.routing),
            Activate(helperContentViewParented1.routing)
        ))
        feature.accept(Sleep())
        clearInvocations(routingActivator)
        feature.accept(WakeUp())

        verify(routingActivator).activate(helperContentViewParented1.routing, helperContentViewParented1.nodes)
    }
    // endregion
}
