package com.badoo.ribs.routing.resolution

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildContext

open class ChildResolution(
    private val ribFactory: RibFactory
) : Resolution {

    override val numberOfNodes: Int = 1

    override fun buildNodes(buildContexts: List<BuildContext>): List<Rib> =
        listOf(
            ribFactory.invoke(
                buildContexts.first()
            )
        )

    companion object {
        fun attach(ribFactory: RibFactory): Resolution =
            ChildResolution(ribFactory)
    }
}
