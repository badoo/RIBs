package com.badoo.ribs.rx2.clienthelper.conector.helpers

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.AncestryInfo
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.rx2.clienthelper.conector.helpers.Rx2ConnectableTestRib.Input
import com.badoo.ribs.rx2.clienthelper.conector.helpers.Rx2ConnectableTestRib.Output
import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.rx2.clienthelper.connector.NodeConnector
import com.badoo.ribs.test.helper.AnyConfiguration
import com.badoo.ribs.test.helper.TestView
import com.badoo.ribs.test.helper.testBuildParams
import com.nhaarman.mockitokotlin2.mock

class Rx2ConnectableTestNode(
    buildParams: BuildParams<*>? = null,
    parent: Node<*>? = null,
    viewFactory: ViewFactory<TestView>? = null,
    router: Router<*> = mock(),
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : Node<TestView>(
    buildParams = buildParams ?: getDefaultBuildParams(parent),
    viewFactory = viewFactory,
    plugins = plugins + listOf(router)
), Rx2ConnectableTestRib, Connectable<Input, Output> by connector {

    companion object {
        private fun getDefaultBuildParams(parent: Node<*>?): BuildParams<*> {
            return if (parent == null) {
                testBuildParams()
            } else {
                testBuildParams(
                    ancestryInfo = AncestryInfo.Child(parent, Routing(AnyConfiguration))
                )
            }
        }
    }
}
