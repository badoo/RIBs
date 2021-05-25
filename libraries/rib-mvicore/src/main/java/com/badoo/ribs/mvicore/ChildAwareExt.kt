package com.badoo.ribs.mvicore

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.resumePause
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.mvicore.binder.Binder
import com.badoo.ribs.clienthelper.childaware.ChildAwareDSL
import com.badoo.ribs.core.Rib

inline fun <reified T : Rib> ChildAwareDSL.createDestroy(
    lifecycle: Lifecycle = this.lifecycle,
    noinline f: Binder.(T) -> Unit
) {
    whenChildAttached(lifecycle, T::class) { commonLifecycle, child ->
        commonLifecycle.createDestroy {
            f(this, child)
        }
    }
}

inline fun <reified T1 : Rib, reified T2 : Rib> ChildAwareDSL.createDestroy(
    lifecycle: Lifecycle = this.lifecycle,
    noinline f: Binder.(T1, T2) -> Unit
) {
    whenChildrenAttached(lifecycle, T1::class, T2::class) { commonLifecycle, child1, child2 ->
        commonLifecycle.createDestroy {
            f(this, child1, child2)
        }
    }
}

inline fun <reified T : Rib> ChildAwareDSL.startStop(
    lifecycle: Lifecycle = this.lifecycle,
    noinline f: Binder.(T) -> Unit
) {
    whenChildAttached(lifecycle, T::class) { commonLifecycle, child ->
        commonLifecycle.startStop {
            f(this, child)
        }
    }
}

inline fun <reified T1 : Rib, reified T2 : Rib> ChildAwareDSL.startStop(
    lifecycle: Lifecycle = this.lifecycle,
    noinline f: Binder.(T1, T2) -> Unit
) {
    whenChildrenAttached(lifecycle, T1::class, T2::class) { commonLifecycle, child1, child2 ->
        commonLifecycle.startStop {
            f(this, child1, child2)
        }
    }
}

inline fun <reified T : Rib> ChildAwareDSL.resumePause(
    lifecycle: Lifecycle = this.lifecycle,
    noinline f: Binder.(T) -> Unit
) {
    whenChildAttached(lifecycle, T::class) { commonLifecycle, child ->
        commonLifecycle.resumePause {
            f(this, child)
        }
    }
}

inline fun <reified T1 : Rib, reified T2 : Rib> ChildAwareDSL.resumePause(
    lifecycle: Lifecycle = this.lifecycle,
    noinline f: Binder.(T1, T2) -> Unit
) {
    whenChildrenAttached(lifecycle, T1::class, T2::class) { commonLifecycle, child1, child2 ->
        commonLifecycle.resumePause {
            f(this, child1, child2)
        }
    }
}

