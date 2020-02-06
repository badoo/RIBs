package com.badoo.ribs.core

import android.os.Bundle
import com.badoo.ribs.core.AttachMode.PARENT
import com.badoo.ribs.core.Rib.Identifier.Companion.KEY_UUID
import com.badoo.ribs.core.routing.portal.AncestryInfo
import java.util.UUID


data class BuildParams<T>(
    val data: T? = null,
    val buildContext: BuildContext,
    val identifier: Rib.Identifier = Rib.Identifier(
        rib = object : Rib {},
        uuid = buildContext.savedInstanceState?.getSerializable(KEY_UUID) as? UUID ?: UUID.randomUUID()
    )
) {
    class BuildContext internal constructor(
        val ancestryInfo: AncestryInfo,
        val viewAttachMode: AttachMode = PARENT,
        val savedInstanceState: Bundle?
    ) {
        companion object {
            fun root(savedInstanceState: Bundle?) = BuildContext(
                ancestryInfo = AncestryInfo.Root,
                savedInstanceState = savedInstanceState
            )
        }
    }
}
