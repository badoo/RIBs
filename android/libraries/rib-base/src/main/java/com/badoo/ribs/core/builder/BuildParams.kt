package com.badoo.ribs.core.builder

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.Rib.Identifier.Companion.KEY_UUID
import java.util.UUID


class BuildParams<T> internal constructor(
    val data: T,
    val buildContext: BuildContext,
    val identifier: Rib.Identifier = Rib.Identifier(
        rib = object : Rib {},
        uuid = buildContext.savedInstanceState?.getSerializable(KEY_UUID) as? UUID
            ?: UUID.randomUUID()
    )
)
