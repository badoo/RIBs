

package com.badoo.ribs.core.modality

import android.os.Bundle
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.plugin.Plugin
import com.bumble.appyx.utils.customisations.NodeCustomisationDirectory
import com.bumble.appyx.utils.customisations.NodeCustomisationDirectoryImpl

/**
 * Holds information passed by the framework to affect modality of the same Node depending on context.
 */
data class BuildContext(
    val ancestryInfo: AncestryInfo,
    val activationMode: ActivationMode = ActivationMode.ATTACH_TO_PARENT,
    val savedInstanceState: Bundle?,
    val customisations: NodeCustomisationDirectory,
    val defaultPlugins: (Node<*>) -> List<Plugin> = { emptyList() }
) {
    companion object {
        /**
         * Only use this for actual roots at integration point and for testing in isolation.
         */
        fun root(
            savedInstanceState: Bundle?,
            customisations: NodeCustomisationDirectory = NodeCustomisationDirectoryImpl(),
            defaultPlugins: (Node<*>) -> List<Plugin> = { emptyList() }
        ): BuildContext =
            BuildContext(
                ancestryInfo = AncestryInfo.Root,
                savedInstanceState = savedInstanceState,
                customisations = customisations,
                defaultPlugins = defaultPlugins
            )
    }
}
