package com.badoo.ribs.core

import android.os.Bundle
import com.badoo.ribs.core.AttachMode.PARENT
import com.badoo.ribs.core.Rib.Identifier.Companion.KEY_UUID
import com.badoo.ribs.core.routing.portal.AncestryInfo
import java.util.UUID


data class BuildParams<T>(
    val data: T? = null,
    val systemInfo: SystemInfo,
    val identifier: Rib.Identifier = Rib.Identifier(
        rib = object : Rib {},
        uuid = systemInfo.savedInstanceState?.getSerializable(KEY_UUID) as? UUID ?: UUID.randomUUID()
    )
) {
    class SystemInfo internal constructor(
        val ancestryInfo: AncestryInfo,
        val viewAttachMode: AttachMode = PARENT,
        val savedInstanceState: Bundle?
    ) {
        companion object {
            fun root(savedInstanceState: Bundle?) = SystemInfo(
                ancestryInfo = AncestryInfo.Root,
                savedInstanceState = savedInstanceState
            )
        }
    }
}
