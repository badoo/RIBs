package com.badoo.ribs.clienthelper.interactor

import com.badoo.ribs.android.requestcode.RequestCodeClient
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.*
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

    override val requestCodeClientId: String
        get() = buildParams.identifier.toString()

    val node: Node<*>
       get() = rib.node
}
