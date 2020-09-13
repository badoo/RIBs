package com.badoo.ribs.routing.resolution

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext

class CompositeResolution(
    private vararg val resolutions: Resolution
) : Resolution {

    override val numberOfNodes: Int = resolutions.sumBy { it.numberOfNodes }

    constructor(resolutions: List<Resolution>) : this(*resolutions.toTypedArray())

    override fun buildNodes(buildContexts: List<BuildContext>) : List<Rib> =
        resolutions.mapIndexed { index, routingAction ->
            routingAction.buildNodes(
                buildContexts = listOfNotNull(buildContexts.getOrNull(index))
            )
        }.flatten()

    override fun execute() {
        resolutions.forEach {
            it.execute()
        }
    }

    override fun cleanup() {
        resolutions.forEach {
            it.cleanup()
        }
    }

    companion object {
        fun composite(vararg resolutions: Resolution): Resolution =
            CompositeResolution(*resolutions)
    }
}
