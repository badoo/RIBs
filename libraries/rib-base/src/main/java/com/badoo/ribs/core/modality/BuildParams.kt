package com.badoo.ribs.core.modality

import android.os.Bundle
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.Rib.Identifier.Companion.KEY_UUID
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.core.customisation.RibCustomisationDirectoryImpl
import java.util.UUID
import kotlin.reflect.KClass


/**
 * Represents information passed to components.
 *
 * @param payload holds data only available build-time
 * @param buildContext see [BuildContext]
 * @param identifier unique identifier (not supposed to be human-readable)
 */
class BuildParams<T>(
    val payload: T,
    val buildContext: BuildContext,
    val identifier: Rib.Identifier = Rib.Identifier(
        uuid = buildContext.savedInstanceState?.getSerializable(KEY_UUID) as? UUID
            ?: UUID.randomUUID()
    )
) {
    val savedInstanceState: Bundle?
        get() = buildContext.savedInstanceState

    companion object {
        /**
         * Only for testing purposes. Don't use this in production code, otherwise all your Nodes
         * will be considered Roots.
         */
        fun Empty() = BuildParams(
            payload = null,
            buildContext = BuildContext.root(
                savedInstanceState = null,
                customisations = RibCustomisationDirectoryImpl()
            )
        )
    }

    fun <T : RibCustomisation> get(key: KClass<T>): T? =
        buildContext.customisations.getRecursively(key)

    fun <T : RibCustomisation> getOrDefault(defaultCustomisation: T): T =
        buildContext.customisations.getRecursivelyOrDefault(defaultCustomisation)

}
