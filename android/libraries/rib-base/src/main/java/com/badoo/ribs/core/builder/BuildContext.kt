package com.badoo.ribs.core.builder

import android.os.Bundle
import com.badoo.ribs.core.AttachMode
import com.badoo.ribs.core.routing.portal.AncestryInfo
import com.badoo.ribs.customisation.RibCustomisationDirectory
import com.badoo.ribs.customisation.RibCustomisationDirectoryImpl

data class BuildContext internal constructor(
    val ancestryInfo: AncestryInfo,
    val attachMode: AttachMode = AttachMode.PARENT,
    val savedInstanceState: Bundle?,
    val customisations: RibCustomisationDirectory
) {
    companion object {
        /**
         * Only use this for actual roots at integration point and for testing in isolation.
         */
        fun root(
            savedInstanceState: Bundle?,
            customisations: RibCustomisationDirectory = RibCustomisationDirectoryImpl()
        ) =
            BuildContext(
                ancestryInfo = AncestryInfo.Root,
                savedInstanceState = savedInstanceState,
                customisations = customisations
            )
    }
}
