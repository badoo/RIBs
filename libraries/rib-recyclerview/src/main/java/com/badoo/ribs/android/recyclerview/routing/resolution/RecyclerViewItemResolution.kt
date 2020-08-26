package com.badoo.ribs.android.recyclerview.routing.resolution

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.ActivationMode
import com.badoo.ribs.core.modality.BuildContext
import com.badoo.ribs.routing.resolution.RibFactory
import com.badoo.ribs.routing.resolution.Resolution

open class RecyclerViewItemResolution(
    private val ribFactory: RibFactory
) : Resolution {

    override val numberOfNodes: Int = 1

    override fun buildNodes(buildContexts: List<BuildContext>): List<Rib> =
        listOf(
            ribFactory.invoke(
                buildContexts.first().copy(
                    activationMode = ActivationMode.CLIENT
                )
            )
        )

    companion object {
        fun recyclerView(ribFactory: RibFactory): Resolution =
            RecyclerViewItemResolution(
                ribFactory
            )
    }
}
