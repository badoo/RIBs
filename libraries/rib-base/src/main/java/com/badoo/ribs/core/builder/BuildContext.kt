package com.badoo.ribs.core.builder

import android.os.Bundle
import com.badoo.ribs.core.ActivationMode
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.routing.portal.AncestryInfo
import com.badoo.ribs.customisation.RibCustomisationDirectory
import com.badoo.ribs.customisation.RibCustomisationDirectoryImpl

data class BuildContext internal constructor(
    val ancestryInfo: AncestryInfo,
    val activationMode: ActivationMode = ActivationMode.ATTACH_TO_PARENT,
    val savedInstanceState: Bundle?,
    val customisations: RibCustomisationDirectory,
    val plugins: List<Plugin> = emptyList()
) {
    companion object {
        /**
         * Only use this for actual roots at integration point and for testing in isolation.
         */
        fun root(
            savedInstanceState: Bundle?,
            customisations: RibCustomisationDirectory = RibCustomisationDirectoryImpl(),
            rootPlugins: List<Plugin> = emptyList()
        ) =
            BuildContext(
                ancestryInfo = AncestryInfo.Root,
                savedInstanceState = savedInstanceState,
                customisations = customisations,
                plugins = rootPlugins
            )
    }
}
