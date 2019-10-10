package com.badoo.ribs.core

import android.os.Bundle
import com.badoo.ribs.core.AttachMode.PARENT
import com.badoo.ribs.core.routing.portal.AncestryInfo

sealed class BuildContext {
    abstract val savedInstanceState: Bundle?
    abstract val ancestryInfo: AncestryInfo
    abstract val viewAttachMode: AttachMode

    // TODO better name
    data class Params(
        override val ancestryInfo: AncestryInfo,
        override val viewAttachMode: AttachMode = PARENT,
        override val savedInstanceState: Bundle?
    ) : BuildContext()

    // TODO better name
    data class ParamsWithData<T : Any?>(
        override val ancestryInfo: AncestryInfo,
        override val viewAttachMode: AttachMode,
        override val savedInstanceState: Bundle?,
        val tag: Any = Unit,
        val data: T? = null
    ) : BuildContext()

    data class Resolved<T : Any?>(
        override val ancestryInfo: AncestryInfo,
        override val viewAttachMode: AttachMode,
        override val savedInstanceState: Bundle?,
        val identifier: Rib.Identifier,
        val data: T? = null
    ) : BuildContext()
}
