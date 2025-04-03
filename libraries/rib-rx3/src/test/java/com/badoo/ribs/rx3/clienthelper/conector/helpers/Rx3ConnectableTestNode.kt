package com.badoo.ribs.rx3.clienthelper.conector.helpers

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.AncestryInfo
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.rx3.clienthelper.conector.helpers.Rx3ConnectableTestRib.Input
import com.badoo.ribs.rx3.clienthelper.conector.helpers.Rx3ConnectableTestRib.Output
import com.badoo.ribs.rx3.clienthelper.connector.Connectable
import com.badoo.ribs.rx3.clienthelper.connector.NodeConnector

class Rx3ConnectableTestNode(
    buildParams: BuildParams<*>? = null,
    parent: Node<*>? = null,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : Node<Nothing>(
    buildParams = buildParams ?: getDefaultBuildParams(parent),
    viewFactory = null,
    plugins = plugins
), Rx3ConnectableTestRib, Connectable<Input, Output> by connector {

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
