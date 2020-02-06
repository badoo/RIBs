package com.badoo.ribs.core.builder

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.Rib.Identifier.Companion.KEY_UUID
import java.util.UUID


data class BuildParams<T>(
    val data: T? = null,
    val buildContext: BuildContext,
    val identifier: Rib.Identifier = Rib.Identifier(
        rib = object : Rib {},
        uuid = buildContext.savedInstanceState?.getSerializable(KEY_UUID) as? UUID
            ?: UUID.randomUUID()
    )
) {
}
