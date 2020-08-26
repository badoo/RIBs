package com.badoo.ribs.portal

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildContext.Companion.root
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.source.impl.Empty
import com.badoo.ribs.portal.PortalRouter.Configuration.Content.Default
import com.badoo.ribs.portal.PortalRouter.Configuration.Content.Portal
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PortalRouterTest {

    lateinit var router: PortalRouter
    lateinit var node1: Node<*>
    lateinit var node2: Node<*>
    lateinit var node3: Node<*>


    @Before
    fun setUp() {
        node3 = TestNode()
        node2 = TestNode(router = TestRouter(routingActionForC3 = child { node3 }))
        node1 = TestNode(router = TestRouter(routingActionForC2 = child { node2 }))

        router = PortalRouter(
            buildParams = BuildParams.Empty(),
            routingSource = Empty(),
            defaultRoutingAction = child { node1 }
        )
    }

    @Test
    fun `Resolving Portal configuration builds expected remote Node`() {
        val remoteRoutingAction = router.resolve(
            Routing(
                Portal(
                    listOf(
                        Routing(Default),
                        Routing(TestRouter.Configuration.C2),
                        Routing(TestRouter.Configuration.C3)
                    )
                )
            )

        )

        val builtNodes = remoteRoutingAction.buildNodes(listOf(root(null)))
        assertEquals(listOf(node3), builtNodes)
    }
}
