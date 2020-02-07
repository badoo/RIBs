package com.badoo.ribs.core.builder

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.Rib.Identifier.Companion.KEY_UUID
import java.util.UUID


class BuildParams<T>(
    val data: T,
    val buildContext: BuildContext,
    val identifier: Rib.Identifier = Rib.Identifier(
        rib = object : Rib {},
        uuid = buildContext.savedInstanceState?.getSerializable(KEY_UUID) as? UUID
            ?: UUID.randomUUID()
    )
) {
    companion object {
        /**
         * Only for testing purposes. Don't use this in production code, otherwise all your Nodes
         * will be considered Roots.
         */
        fun Empty() = BuildParams(
            data = null,
            buildContext = BuildContext.root(null)
        )
    }
}
