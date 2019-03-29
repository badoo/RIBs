package com.badoo.ribs.base.viewless

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.uber.rib.util.RibRefWatcher

open class ViewlessNode(
    identifier: Rib,
    router: ViewlessRouter<*>?,
    interactor: ViewlessInteractor,
    ribRefWatcher: RibRefWatcher = RibRefWatcher.getInstance()
): Node<Nothing>(identifier, null, router, interactor, ribRefWatcher)
