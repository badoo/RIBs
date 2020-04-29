package com.badoo.ribs.core.builder

import android.os.Bundle
import com.badoo.ribs.core.AttachMode
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.Rib.Identifier.Companion.KEY_UUID
import com.badoo.ribs.core.routing.portal.AncestryInfo
import java.util.UUID


class BuildParams<T>(
    val payload: T,
    val buildContext: BuildContext,
    val identifier: Rib.Identifier = Rib.Identifier(
        uuid = buildContext.savedInstanceState?.getSerializable(KEY_UUID) as? UUID
            ?: UUID.randomUUID()
    )
) {
    val savedInstanceState: Bundle?
        get() = buildContext.savedInstanceState

    companion object {
        /**
         * Only for testing purposes. Don't use this in production code, otherwise all your Nodes
         * will be considered Roots.
         */
        fun Empty() = BuildParams(
            payload = null,
            buildContext = BuildContext.root(null)
        )

        /**
         * Only for testing purposes. Don't use this in production code, otherwise all your Nodes
         * will be considered children.
         */
        fun EmptyChild(buildContext: BuildContext) = BuildParams(
            payload = null,
            buildContext = buildContext
        )
    }
}
