package com.badoo.ribs.core

import android.os.Bundle
import com.badoo.ribs.core.routing.portal.AncestryInfo

class BuildContext internal constructor(
    val ancestryInfo: AncestryInfo,
    val viewAttachMode: AttachMode = AttachMode.PARENT,
    val savedInstanceState: Bundle?
) {
    companion object {
        fun root(savedInstanceState: Bundle?) =
            BuildContext(
                ancestryInfo = AncestryInfo.Root,
                savedInstanceState = savedInstanceState
            )
    }
}
