package com.badoo.ribs.core.routing.backstack

import android.os.Parcelable
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Node.*
import com.badoo.ribs.core.routing.action.RoutingAction
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.MultiConfigurationCommand.Sleep
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.MultiConfigurationCommand.WakeUp
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.SingleConfigurationCommand.Activate
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.SingleConfigurationCommand.Add
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.SingleConfigurationCommand.Deactivate
import com.badoo.ribs.core.routing.backstack.ConfigurationCommand.SingleConfigurationCommand.Remove
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.backstack.ConfigurationKey.Content
import com.badoo.ribs.core.routing.backstack.feature.BackStackFeature.State
import com.badoo.ribs.core.routing.backstack.feature.ConfigurationFeature
import com.jakewharton.rxrelay2.PublishRelay
import com.nhaarman.mockitokotlin2.clearInvocations
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import kotlinx.android.parcel.Parcelize
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ConfigurationFeatureTest {

    sealed class Configuration : Parcelable {
        @Parcelize object P1 : Configuration()
        @Parcelize object P2 : Configuration()
        @Parcelize object C1 : Configuration()
        @Parcelize object C2 : Configuration()
    }

    private lateinit var feature: ConfigurationFeature<Configuration>
    private lateinit var resolver: (Configuration) -> RoutingAction<*>
    private lateinit var parentNode: Node<*>
    private lateinit var backStackStateSubject: PublishRelay<State<Configuration>>

    private lateinit var helperP1: ConfigurationTestHelper
    private lateinit var helperP2: ConfigurationTestHelper
    private lateinit var helperC1: ConfigurationTestHelper
    private lateinit var helperC2: ConfigurationTestHelper

    data class ConfigurationTestHelper(
        val configuration: Configuration,
        val nodes: List<Descriptor>,
        val nodeFactories: List<() -> Descriptor>,
        val routingAction: RoutingAction<*>
    ) {
        companion object {
            fun create(configuration: Configuration, nbNodes: Int, viewAttachMode: ViewAttachMode): ConfigurationTestHelper {
                val nodes = MutableList(nbNodes) { Descriptor(mock(), viewAttachMode) }
                val factories = nodes.toFactory()
                val routingAction: RoutingAction<*> = factories.toRoutingAction()

                return ConfigurationTestHelper(
                    configuration = configuration,
                    nodes = nodes,
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
        helperP1 = ConfigurationTestHelper.create(Configuration.P1, 2, ViewAttachMode.PARENT)
        helperP2 = ConfigurationTestHelper.create(Configuration.P2,3, ViewAttachMode.PARENT)
        helperC1 = ConfigurationTestHelper.create(Configuration.C1,2, ViewAttachMode.PARENT)
        helperC2 = ConfigurationTestHelper.create(Configuration.C2,3, ViewAttachMode.EXTERNAL)
        val helpers = listOf(helperP1, helperP2, helperC1, helperC2)

        resolver = mock {
            helpers.forEach { helper ->
                on { invoke(helper.configuration) } doReturn helper.routingAction
            }
        }

        backStackStateSubject = PublishRelay.create<State<Configuration>>()
        parentNode = mock()
        feature = ConfigurationFeature(
            permanentParts = listOf(
                Configuration.P1,
                Configuration.P2
            ),
            timeCapsule = mock(),
            resolver = resolver,
            parentNode = parentNode
        )
    }
    // region Init

    @Test
    fun `On init, Permanent parts are added - associated RoutingActions are resolved on demand`() {
        verify(resolver).invoke(Configuration.P1)
        verify(resolver).invoke(Configuration.P2)
    }

    @Test
    fun `On init, Permanent parts are added - Node factories of the associated RoutingActions are invoked`() {
        (helperP1.nodeFactories + helperP2.nodeFactories).forEach {
            verify(it).invoke()
        }
    }

    @Test
    fun `On init, Permanent parts are added - Nodes that are created are attached`() {
        (helperP1.nodes + helperP2.nodes).forEach {
            verify(parentNode).attachChildNode(it.node, null)
        }
    }

    @Test
    fun `On init, Permanent parts are activated - associated RoutingActions are executed`() {
        verify(helperP1.routingAction).execute()
        verify(helperP2.routingAction).execute()
    }

    @Test
    fun `On init, Permanent parts are activated - Nodes that are created are attached to the view`() {
        (helperP1.nodes + helperP2.nodes).forEach {
            verify(parentNode).attachChildView(it.node)
        }
    }

    // endregion

    // region Add
    @Test
    fun `On Add, Node factories of the associated RoutingAction are invoked`() {
        feature.accept(Add(Content(0), Configuration.C1))
        helperC1.nodeFactories.forEach {
            verify(it).invoke()
        }
    }

    @Test
    fun `On Add, Nodes that are created are attached`() {
        feature.accept(Add(Content(0), Configuration.C1))
        helperC1.nodes.forEach {
            verify(parentNode).attachChildNode(it.node, null)
        }
    }

    @Test
    fun `On Add, associated RoutingAction is resolved on demand`() {
        feature.accept(Add(Content(0), Configuration.C1))
        verify(resolver).invoke(Configuration.C1)
    }

    @Test
    fun `On Add, associated RoutingAction is not yet executed`() {
        feature.accept(Add(Content(0), Configuration.C1))
        verify(helperC1.routingAction, never()).execute()
    }
    // endregion

    // region Activate
    @Test
    fun `On Activate, associated RoutingAction is executed`() {
        feature.accept(Add(Content(0), Configuration.C1))
        feature.accept(Activate(Content(0)))
        verify(helperC1.routingAction).execute()
    }

    @Test
    fun `On Activate, attachChildView() is called on associated Nodes that are view-parented`() {
        feature.accept(Add(Content(0), Configuration.C1)) // This configuration resolves to view-parented Nodes
        feature.accept(Activate(Content(0)))
        helperC1.nodes.forEach {
            verify(parentNode).attachChildView(it.node)
        }
    }

    @Test
    fun `On Activate, attachChildView() is NOT called on associated Nodes that are NOT view-parented`() {
        feature.accept(Add(Content(0), Configuration.C2)) // This configuration resolves to external Nodes
        feature.accept(Activate(Content(0)))
        helperC1.nodes.forEach {
            verify(parentNode, never()).attachChildView(it.node)
        }
    }
    // endregion

    // region Deactivate
    @Test
    fun `On Deactivate, cleanup() is called on associated RoutingAction`() {
        feature.accept(Add(Content(0), Configuration.C1)) // This configuration resolves to view-parented Nodes
        feature.accept(Deactivate(Content(0)))
        verify(helperC1.routingAction).cleanup()
    }

    @Test
    fun `On Deactivate, saveViewState() is called on associated Nodes`() {
        feature.accept(Add(Content(0), Configuration.C1)) // This configuration resolves to view-parented Nodes
        feature.accept(Deactivate(Content(0)))
        helperC1.nodes.forEach {
            verify(it.node).saveViewState()
        }
    }

    @Test
    fun `On Deactivate, detachChildView() is called on associated Nodes that are view-parented`() {
        feature.accept(Add(Content(0), Configuration.C1)) // This configuration resolves to view-parented Nodes
        feature.accept(Deactivate(Content(0)))
        helperC1.nodes.forEach {
            verify(parentNode).detachChildView(it.node)
        }
    }

    @Test
    fun `On Deactivate, detachChildView() is NOT called on associated Nodes that are NOT view-parented`() {
        feature.accept(Add(Content(0), Configuration.C2)) // This configuration resolves to external Nodes
        feature.accept(Deactivate(Content(0)))
        helperC1.nodes.forEach {
            verify(parentNode, never()).detachChildView(it.node)
        }
    }

    @Test
    fun `On Deactivate, Node references are kept`() {
        feature.accept(Add(Content(0), Configuration.C1)) // This configuration resolves to view-parented Nodes
        feature.accept(Deactivate(Content(0)))
        val configurationContext = feature.state.pool[Content(0)]
        assertEquals(true, configurationContext is Resolved)
        assertEquals(helperC1.nodes, (configurationContext as? Resolved)?.nodes)
    }
    // endregion

    // region Remove
    @Test
    fun `On Remove, all of its Nodes are detached regardless of view-parenting mode`() {
        feature.accept(Add(Content(0), Configuration.C1)) // This configuration resolves to view-parented Nodes
        feature.accept(Add(Content(1), Configuration.C2)) // This configuration resolves to external Nodes
        feature.accept(Remove(Content(1)))
        feature.accept(Remove(Content(0)))

        (helperC1.nodes + helperC2.nodes).forEach {
            inOrder(parentNode) {
                verify(parentNode).detachChildView(it.node)
                verify(parentNode).detachChildNode(it.node)
            }
        }
    }

    @Test
    fun `On Remove, all Node references are cleared`() {
        feature.accept(Add(Content(0), Configuration.C1)) // This configuration resolves to view-parented Nodes
        feature.accept(Add(Content(1), Configuration.C2)) // This configuration resolves to external Nodes
        feature.accept(Remove(Content(1)))
        feature.accept(Remove(Content(0)))

        feature.state.pool.forEach { (key, value) ->
            // Only Permanent parts should remain, all Content should be cleared a this point
            assertEquals(true, key is ConfigurationKey.Permanent)
        }
    }
    // endregion

    // region Sleep
    @Test
    fun `On Sleep after WakeUp, cleanup() is called on associated RoutingAction`() {
        feature.accept(WakeUp())
        feature.accept(Add(Content(0), Configuration.C1)) // This configuration resolves to view-parented Nodes
        feature.accept(Add(Content(1), Configuration.C2)) // This configuration resolves to external Nodes
        feature.accept(Activate(Content(0)))
        clearInvocations(parentNode)
        feature.accept(Sleep())

        verify(helperC1.routingAction).cleanup()
    }

    @Test
    // FIXME more configurations to test "every ACTIVE node" part
    fun `On Sleep after WakeUp, saveViewState() is called on every ACTIVE node`() {
        feature.accept(WakeUp())
        feature.accept(Add(Content(0), Configuration.C1)) // This configuration resolves to view-parented Nodes
        feature.accept(Add(Content(1), Configuration.C2)) // This configuration resolves to external Nodes
        feature.accept(Activate(Content(0)))
        clearInvocations(parentNode)
        feature.accept(Sleep())

        helperC1.nodes.forEach {
            verify(it.node).saveViewState()
        }
    }

    @Test
    // FIXME more configurations to test "every ACTIVE node" part
    fun `On Sleep after WakeUp, detachChildView() is called on every ACTIVE node that are view-parented`() {
        feature.accept(WakeUp())
        feature.accept(Add(Content(0), Configuration.C1)) // This configuration resolves to view-parented Nodes
        feature.accept(Add(Content(1), Configuration.C2)) // This configuration resolves to external Nodes
        feature.accept(Activate(Content(0)))
        clearInvocations(parentNode)
        feature.accept(Sleep())

        helperC1.nodes.forEach {
            verify(parentNode).detachChildView(it.node)
        }
    }
    // endregion

    // region WakeUp
    @Test
    fun `On WakeUp after Sleep, execute() is called on associated RoutingAction`() {
        feature.accept(WakeUp())
        feature.accept(Add(Content(0), Configuration.C1)) // This configuration resolves to view-parented Nodes
        feature.accept(Add(Content(1), Configuration.C2)) // This configuration resolves to external Nodes
        feature.accept(Activate(Content(0)))
        feature.accept(Sleep())
        clearInvocations(helperC1.routingAction)
        feature.accept(WakeUp())

        verify(helperC1.routingAction).execute()
    }

    @Test
    // FIXME more configurations to test "every ACTIVE node" part
    fun `On WakeUp after Sleep, attachChildView() is called on every ACTIVE node that are view-parented`() {
        feature.accept(WakeUp())
        feature.accept(Add(Content(0), Configuration.C1)) // This configuration resolves to view-parented Nodes
        feature.accept(Add(Content(1), Configuration.C2)) // This configuration resolves to external Nodes
        feature.accept(Activate(Content(0)))
        feature.accept(Sleep())
        clearInvocations(parentNode)
        feature.accept(WakeUp())

        helperC1.nodes.forEach {
            verify(parentNode).attachChildView(it.node)
        }
    }
    // endregion
}
