package com.badoo.ribs.core.helper.connectable

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.clienthelper.connector.NodeConnector
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.helper.AnyConfiguration
import com.badoo.ribs.core.helper.connectable.ConnectableTestRib.Input
import com.badoo.ribs.core.helper.connectable.ConnectableTestRib.Output
import com.badoo.ribs.core.helper.testBuildParams
import com.badoo.ribs.core.modality.AncestryInfo
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.routing.Routing

class ConnectableTestNode(
    buildParams: BuildParams<*>? = null,
    parent: Node<*>? = null,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : Node<Nothing>(
    buildParams = buildParams ?: getDefaultBuildParams(parent),
    viewFactory = null,
    plugins = plugins
), ConnectableTestRib, Connectable<Input, Output> by connector {

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

