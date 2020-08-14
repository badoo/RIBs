package com.badoo.ribs.clienthelper.interactor

import com.badoo.ribs.android.requestcode.RequestCodeClient
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.BackPressHandler
import com.badoo.ribs.core.plugin.NodeLifecycleAware
import com.badoo.ribs.core.plugin.RibAware
import com.badoo.ribs.core.plugin.RibAwareImpl
import com.badoo.ribs.core.plugin.SavesInstanceState
import com.badoo.ribs.core.plugin.SubtreeChangeAware
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.core.view.RibView

abstract class Interactor<R : Rib, V : RibView>(
    private val buildParams: BuildParams<*>,
    private val ribAware: RibAware<R> = RibAwareImpl()
) : RibAware<R> by ribAware,
    ViewAware<V>,
    NodeLifecycleAware,
    SubtreeChangeAware,
    BackPressHandler,
    SavesInstanceState,
    RequestCodeClient {

    /**
     * This will be used to make your locally unique request codes unique to the whole app
     *
     * The idea is that you can use Android request codes starting from 1, 2, 3, ... instead of "random"
     * numbers, and they only need to be unique globally in combination with the value of this field.
     */
    override val requestCodeClientId: String
        get() = buildParams.identifier.toString()

    val node: Node<*>
       get() = rib.node
}
