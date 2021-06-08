package com.badoo.ribs.rx2.clienthelper.conector.helpers

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.AncestryInfo
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.rx2.clienthelper.conector.helpers.Rx2ConnectableTestRib.Input
import com.badoo.ribs.rx2.clienthelper.conector.helpers.Rx2ConnectableTestRib.Output
import com.badoo.ribs.rx2.clienthelper.connector.Connectable
import com.badoo.ribs.rx2.clienthelper.connector.NodeConnector

class Rx2ConnectableTestNode(
    buildParams: BuildParams<*>? = null,
    parent: Node<*>? = null,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : Node<Nothing>(
    buildParams = buildParams ?: getDefaultBuildParams(parent),
    viewFactory = null,
    plugins = plugins
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
