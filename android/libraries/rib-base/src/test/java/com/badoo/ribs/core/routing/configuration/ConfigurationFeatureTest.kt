package com.badoo.ribs.core.routing.configuration

import android.os.Bundle
import android.os.Parcelable
import com.badoo.mvicore.element.TimeCapsule
import com.badoo.ribs.core.AttachMode
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.builder.BuildContext
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Activate
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Add
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Deactivate
import com.badoo.ribs.core.routing.configuration.ConfigurationCommand.Remove
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.INACTIVE
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.ActivationState.SLEEPING
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Unresolved
import com.badoo.ribs.core.routing.configuration.ConfigurationFeatureTest.Configuration.ContentExternal1
import com.badoo.ribs.core.routing.configuration.ConfigurationFeatureTest.Configuration.ContentExternal2
import com.badoo.ribs.core.routing.configuration.ConfigurationFeatureTest.Configuration.ContentViewParented1
import com.badoo.ribs.core.routing.configuration.ConfigurationFeatureTest.Configuration.ContentViewParented2
import com.badoo.ribs.core.routing.configuration.ConfigurationFeatureTest.Configuration.ContentViewParented3
import com.badoo.ribs.core.routing.configuration.ConfigurationFeatureTest.Configuration.Permanent1
import com.badoo.ribs.core.routing.configuration.ConfigurationFeatureTest.Configuration.Permanent2
import com.badoo.ribs.core.routing.configuration.ConfigurationKey.Content
import com.badoo.ribs.core.routing.configuration.ConfigurationKey.Permanent
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
import com.nhaarman.mockitokotlin2.inOrder
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
    private lateinit var resolver: ConfigurationResolver<Configuration>
    private lateinit var parentNode: Node<Nothing>

    private lateinit var helperPermanent1: ConfigurationTestHelper
    private lateinit var helperPermanent2: ConfigurationTestHelper
    private lateinit var helperContentViewParented1: ConfigurationTestHelper
    private lateinit var helperContentViewParented2: ConfigurationTestHelper
    private lateinit var helperContentViewParented3: ConfigurationTestHelper
    private lateinit var helperContentExternal1: ConfigurationTestHelper
    private lateinit var helperContentExternal2: ConfigurationTestHelper

    data class ConfigurationTestHelper(
        val configuration: Configuration,
        val nodes: List<Node<Nothing>>,
        val bundles: List<Bundle>,
        val nodeFactories: List<() -> Rib>,
        val routingAction: RoutingAction
    ) {
        companion object {
            fun create(configuration: Configuration, nbNodes: Int, viewAttachMode: AttachMode): ConfigurationTestHelper {
                val nodes = MutableList(nbNodes) { i ->
                    mock<Node<Nothing>> {
                        on { this.buildContext } doReturn BuildContext.root(null)
                        on { this.attachMode } doReturn viewAttachMode
                        on { toString() } doReturn "Node #$i of $configuration"
                    }
                }
                val bundles = MutableList(nbNodes) { mock<Bundle>() }
                val factories = nodes.toFactory()
                val routingAction: RoutingAction = factories.toRoutingAction(nbNodes)

                return ConfigurationTestHelper(
                    configuration = configuration,
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
        helperPermanent1 =
            ConfigurationTestHelper.create(Permanent1,2, AttachMode.PARENT)

        helperPermanent2 =
            ConfigurationTestHelper.create(Permanent2,3, AttachMode.PARENT)

        helperContentViewParented1 =
            ConfigurationTestHelper.create(ContentViewParented1,2, AttachMode.PARENT)

        helperContentViewParented2 =
            ConfigurationTestHelper.create(ContentViewParented2,3, AttachMode.PARENT)

        helperContentViewParented3 =
            ConfigurationTestHelper.create(ContentViewParented3,2, AttachMode.PARENT)

        helperContentExternal1 =
            ConfigurationTestHelper.create(ContentExternal1,2, AttachMode.EXTERNAL)

        helperContentExternal2 =
            ConfigurationTestHelper.create(ContentExternal2,3, AttachMode.EXTERNAL)

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
                on { resolve(Routing(helper.configuration)) } doReturn helper.routingAction
            }
        }

        val routingsforTimeCapsule = listOf(
            Routing(identifier = Identifier("Permanent 0"), configuration = Permanent1 as Configuration),
            Routing(identifier = Identifier("Permanent 1"), configuration = Permanent2 as Configuration),

            Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1 as Configuration),
            Routing(identifier = Identifier("Content 1"), configuration = ContentViewParented2 as Configuration),
            Routing(identifier = Identifier("Content 2"), configuration = ContentViewParented3 as Configuration),

            Routing(identifier = Identifier("Content 3"), configuration = ContentExternal1 as Configuration),
            Routing(identifier = Identifier("Content 4"), configuration = ContentExternal2 as Configuration)
        )

        val poolInTimeCapsule = mapOf(
            routingsforTimeCapsule[0] to Unresolved(SLEEPING, routingsforTimeCapsule[0], helperPermanent1.bundles),
            routingsforTimeCapsule[1] to Unresolved(SLEEPING, routingsforTimeCapsule[2], helperPermanent2.bundles),

            routingsforTimeCapsule[2] to Unresolved(SLEEPING, routingsforTimeCapsule[3], helperContentViewParented1.bundles),
            routingsforTimeCapsule[3] to Unresolved(SLEEPING, routingsforTimeCapsule[4], helperContentViewParented2.bundles),
            routingsforTimeCapsule[4] to Unresolved(INACTIVE, routingsforTimeCapsule[5], helperContentViewParented3.bundles),

            routingsforTimeCapsule[5] to Unresolved(SLEEPING, routingsforTimeCapsule[6], helperContentExternal1.bundles),
            routingsforTimeCapsule[6] to Unresolved(INACTIVE, routingsforTimeCapsule[7], helperContentExternal2.bundles)
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

    private fun createFeature(timeCapsule: TimeCapsule<SavedState<Configuration>>): ConfigurationFeature<Configuration> {
        return ConfigurationFeature(
            timeCapsule = timeCapsule,
            resolver = resolver,
            parentNode = parentNode,
            transitionHandler = null,
            activator = mock()
        )
    }

    private fun createEmptyFeature() {
        feature = createFeature(emptyTimeCapsule)
    }

    private fun createRestoredFeature() {
        feature = createFeature(restoredTimeCapsule)
    }

    // region Init

    @Test
    fun `On init, ALL initial configuration are added - associated RoutingActions are resolved on demand`() {
        createEmptyFeature()
        verify(resolver).resolve(Routing(Permanent1))
        verify(resolver).resolve(Routing(Permanent2))
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
        helperPermanent1.nodes.forEach { verify(parentNode).attachChildView(it) }
        helperPermanent2.nodes.forEach { verify(parentNode).attachChildView(it) }
    }

    // endregion

    // region Init from SavedState

    @Test
    fun `On WakeUp after init from TimeCapsule, ALL previously ACTIVE configurations are added - associated RoutingActions are resolved on demand`() {
        createRestoredFeature()
        feature.accept(WakeUp())
        verify(resolver).resolve(Routing(Permanent1))
        verify(resolver).resolve(Routing(Permanent2))
        verify(resolver).resolve(Routing(ContentViewParented1))
        verify(resolver).resolve(Routing(ContentViewParented2))
        verify(resolver).resolve(Routing(ContentExternal1))
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

        helperPermanent1.nodes.forEachIndexed{ idx, item ->
            verify(parentNode).attachChildNode(item)
        }
        helperPermanent2.nodes.forEachIndexed{ idx, item ->
            verify(parentNode).attachChildNode(item)
        }
        helperContentViewParented1.nodes.forEachIndexed{ idx, item ->
            verify(parentNode).attachChildNode(item)
        }
        helperContentViewParented2.nodes.forEachIndexed{ idx, item ->
            verify(parentNode).attachChildNode(item)
        }
        helperContentExternal1.nodes.forEachIndexed{ idx, item ->
            verify(parentNode).attachChildNode(item)
        }
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
        helperPermanent1.nodes.forEach { verify(parentNode).attachChildView(it) }
        helperPermanent2.nodes.forEach { verify(parentNode).attachChildView(it) }
        helperContentViewParented1.nodes.forEach { verify(parentNode).attachChildView(it) }
        helperContentViewParented2.nodes.forEach { verify(parentNode).attachChildView(it) }
        helperContentExternal1.nodes.forEach { verify(parentNode).attachChildView(it) }

        // As these were INACTIVE and shouldn't be reactivated after WakeUp
        helperContentViewParented3.nodes.forEach { verify(parentNode, never()).attachChildView(it) }
        helperContentExternal2.nodes.forEach { verify(parentNode, never()).attachChildView(it) }
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
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1))
        ))
        helperContentViewParented1.nodeFactories.forEach {
            verify(it).invoke()
        }
    }

    @Test
    fun `On Add TWICE, Node factories are NOT invoked again`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1)),
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1))
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
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1))
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
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1)),
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1))
        ))
        helperContentViewParented1.nodes.forEach {
            verify(parentNode, times(1)).attachChildNode(it)
        }
    }

    @Test
    fun `On Add, associated RoutingAction is resolved on demand`() {
        createEmptyFeature()
        feature.accept(Transaction.from(Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1))))
        verify(resolver).resolve(Routing(ContentViewParented1))
    }

    @Test
    fun `On Add, associated RoutingAction is not yet executed`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1))
        ))
        verify(helperContentViewParented1.routingAction, never()).execute()
    }
    // endregion

    // region Activate
    @Test
    fun `On Activate BEFORE WakeUp, associated RoutingAction is NOT yet executed`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1)),
            Activate(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1))
        ))
        verify(helperContentViewParented1.routingAction, never()).execute()
    }

    @Test
    fun `On Activate BEFORE WakeUp, attachChildView() is NOT yet called on associated Nodes that are view-parented`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1)),
            Activate(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1))
        ))
        helperContentViewParented1.nodes.forEach {
            verify(parentNode, never()).attachChildView(it)
        }
    }

    @Test
    fun `On Activate BEFORE WakeUp, associated RoutingAction is executed AUTOMATICALLY AFTER next WakeUp`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1)),
            Activate(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1))
        ))
        feature.accept(WakeUp())
        verify(helperContentViewParented1.routingAction).execute()
    }

    @Test
    fun `On Activate BEFORE WakeUp, attachChildView() is called AUTOMATICALLY AFTER next WakeUp on associated Nodes that are view-parented`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1)),
            Activate(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1))
        ))
        feature.accept(WakeUp())
        helperContentViewParented1.nodes.forEach {
            verify(parentNode).attachChildView(it)
        }
    }

    @Test
    fun `On Activate AFTER WakeUp, associated RoutingAction is executed`() {
        createEmptyFeature()
        feature.accept(WakeUp())
        feature.accept(Transaction.from(
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1)),
            Activate(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1))
        ))
        verify(helperContentViewParented1.routingAction).execute()
    }

    @Test
    fun `On Activate AFTER WakeUp, attachChildView() is called on associated Nodes that are view-parented`() {
        createEmptyFeature()
        feature.accept(WakeUp())
        feature.accept(Transaction.from(
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1)),
            Activate(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1))
        ))
        helperContentViewParented1.nodes.forEach {
            verify(parentNode).attachChildView(it)
        }
    }

    @Test
    fun `On Activate AFTER WakeUp, attachChildView() is NOT called on associated Nodes that are NOT view-parented`() {
        createEmptyFeature()
        feature.accept(WakeUp())
        feature.accept(Transaction.from(
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentExternal1)),
            Activate(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1))
        ))
        helperContentViewParented1.nodes.forEach {
            verify(parentNode, never()).attachChildView(it)
        }
    }

    @Test
    fun `On Activate on ALREADY ACTIVE configuration, associated RoutingAction is NOT executed again`() {
        createEmptyFeature()
        feature.accept(WakeUp())
        feature.accept(Transaction.from(
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1)),
            Activate(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1))
        ))
        feature.accept(Transaction.from(Activate(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1))))
        verify(helperContentViewParented1.routingAction, times(1)).execute()
    }

    @Test
    fun `On Activate on ALREADY ACTIVE configuration, attachChildView() is NOT called again`() {
        createEmptyFeature()
        feature.accept(WakeUp())
        feature.accept(Transaction.from(
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1)),
            Activate(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1))
        ))
        feature.accept(Transaction.from(Activate(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1))))
        helperContentViewParented1.nodes.forEach {
            verify(parentNode, times(1)).attachChildView(it)
        }
    }
    // endregion

    // region Deactivate
    @Test
    fun `On Deactivate, cleanup() is called on associated RoutingAction`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1)),
            Deactivate(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1))
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
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1)),
            Deactivate(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1))
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
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1)),
            Deactivate(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1))
        ))
        helperContentViewParented1.nodes.forEach {
            verify(parentNode).detachChildView(it)
        }
    }

    @Test
    fun `On Deactivate, detachChildView() is NOT called on associated Nodes that are NOT view-parented`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentExternal1)),
            Deactivate(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1))
        ))
        helperContentViewParented1.nodes.forEach {
            verify(parentNode, never()).detachChildView(it)
        }
    }

    @Test
    fun `On Deactivate, Node references are kept`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1)),
            Deactivate(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1))
        ))
        val configurationContext = feature.state.pool[Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1 as Configuration)]
        assertEquals(true, configurationContext is Resolved)
        assertEquals(helperContentViewParented1.nodes, (configurationContext as? Resolved)?.nodes)
    }
    // endregion

    // region Remove
    @Test
    fun `On Remove, all of its Nodes are detached regardless of view-parenting mode`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1 as Configuration)),
            Add(Routing(identifier = Identifier("Content 1"), configuration = ContentExternal1 as Configuration)),
            Remove(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1 as Configuration)),
            Remove(Routing(identifier = Identifier("Content 1"), configuration = ContentExternal1 as Configuration))
        ))

        helperContentExternal1.nodes.forEach {
            inOrder(parentNode) {
                verify(parentNode).detachChildView(it)
                verify(parentNode).detachChildNode(it)
            }
        }
        helperContentViewParented1.nodes.forEach {
            inOrder(parentNode) {
                verify(parentNode).detachChildView(it)
                verify(parentNode).detachChildNode(it)
            }
        }
    }

    @Test
    fun `On Remove all added elements, only permanent parts are left in the pool`() {
        createEmptyFeature()
        feature.accept(Transaction.from(
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1 as Configuration)),
            Add(Routing(identifier = Identifier("Content 1"), configuration = ContentExternal1)),
            Remove(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1)),
            Remove(Routing(identifier = Identifier("Content 1"), configuration = ContentExternal1))
        ))
        val configurationsLeftInPool = feature.state.pool.map {
            it.value.routing
        }
        assertEquals(permanentParts, configurationsLeftInPool)
    }
    // endregion

    // region Sleep
    @Test
    fun `On Sleep after WakeUp, cleanup() is called on associated RoutingAction`() {
        createEmptyFeature()
        feature.accept(WakeUp())
        feature.accept(Transaction.from(
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1 as Configuration)),
            Add(Routing(identifier = Identifier("Content 1"), configuration = ContentExternal1)),
            Activate(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1 as Configuration))
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
    @Ignore
    fun `On Sleep after WakeUp, saveViewState() is called on every ACTIVE node`() {
        createEmptyFeature()
        feature.accept(WakeUp())
        feature.accept(Transaction.from(
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1 as Configuration)),
            Add(Routing(identifier = Identifier("Content 1"), configuration = ContentExternal1)),
            Activate(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1 as Configuration))
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
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1 as Configuration)),
            Add(Routing(identifier = Identifier("Content 1"), configuration = ContentExternal1)),
            Activate(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1 as Configuration))
        ))
        clearInvocations(parentNode)
        feature.accept(Sleep())

        helperContentViewParented1.nodes.forEach {
            verify(parentNode).detachChildView(it)
        }
    }
    // endregion

    // region WakeUp
    @Test
    fun `On WakeUp after Sleep, execute() is called on associated RoutingAction`() {
        createEmptyFeature()
        feature.accept(WakeUp())
        feature.accept(Transaction.from(
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1 as Configuration)),
            Add(Routing(identifier = Identifier("Content 1"), configuration = ContentExternal1)),
            Activate(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1 as Configuration))
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
            Add(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1 as Configuration)),
            Add(Routing(identifier = Identifier("Content 1"), configuration = ContentExternal1)),
            Activate(Routing(identifier = Identifier("Content 0"), configuration = ContentViewParented1 as Configuration))
        ))
        feature.accept(Sleep())
        clearInvocations(parentNode)
        feature.accept(WakeUp())

        helperContentViewParented1.nodes.forEach {
            verify(parentNode).attachChildView(it)
        }
    }
    // endregion
}
