package com.badoo.ribs.core

import android.os.Bundle

sealed class BuildContext {
    abstract val savedInstanceState: Bundle?

    // TODO better name
    data class Params(
        // TODO move AncestryInfo here too
        override val savedInstanceState: Bundle?
    ) : BuildContext()

    // TODO better name
    data class ParamsWithData<T : Any?>(
        override val savedInstanceState: Bundle?,
        val tag: Any = Unit,
        val data: T? = null
    ) : BuildContext()

    data class Resolved<T : Any?>(
        override val savedInstanceState: Bundle?,
        val identifier: Rib.Identifier,
        val data: T? = null
    ) : BuildContext()
}
