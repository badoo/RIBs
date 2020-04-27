package com.badoo.ribs.core.builder

import android.os.Bundle
import com.badoo.ribs.core.AttachMode
import com.badoo.ribs.core.routing.portal.AncestryInfo
import com.badoo.ribs.customisation.RibCustomisationDirectory

data class BuildContext internal constructor(
    val ancestryInfo: AncestryInfo,
    val attachMode: AttachMode = AttachMode.PARENT,
    val savedInstanceState: Bundle?
) {
    companion object {
        /**
         * Only use this for actual roots at integration point and for testing in isolation.
         */
        fun root(savedInstanceState: Bundle?) =
            BuildContext(
                ancestryInfo = AncestryInfo.Root,
                savedInstanceState = savedInstanceState
            )
    }
}
