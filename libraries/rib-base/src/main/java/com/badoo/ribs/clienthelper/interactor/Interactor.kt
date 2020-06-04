package com.badoo.ribs.clienthelper.interactor

import com.badoo.ribs.core.RequestCodeClient
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.plugin.BackPressHandler
import com.badoo.ribs.core.plugin.NodeLifecycleAware
import com.badoo.ribs.core.plugin.RibAware
import com.badoo.ribs.core.plugin.RibAwareImpl
import com.badoo.ribs.core.plugin.SavesInstanceState
import com.badoo.ribs.core.plugin.SubtreeChangeAware
import com.badoo.ribs.core.plugin.ViewAware
import com.badoo.ribs.core.view.RibView
import io.reactivex.disposables.Disposable

abstract class Interactor<R : Rib, V : RibView>(
    buildParams: BuildParams<*>,
    private val disposables: Disposable? = null,
    private val ribAware: RibAware<R> = RibAwareImpl()
) : RequestCodeClient by buildParams.identifier,
    RibAware<R> by ribAware,
    BackPressHandler,
    NodeLifecycleAware,
    SubtreeChangeAware,
    SavesInstanceState,
    ViewAware<V> {

    override fun onDetach() {
        disposables?.dispose()
    }

    val node: Node<*>
       get() = rib.node
}
