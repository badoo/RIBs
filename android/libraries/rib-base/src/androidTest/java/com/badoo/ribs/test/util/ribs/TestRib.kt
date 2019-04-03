package com.badoo.ribs.test.util.ribs

import android.arch.lifecycle.Lifecycle
import com.badoo.ribs.core.Rib
import io.reactivex.observers.TestObserver

interface TestRib : Rib {

    interface Dependency {
        fun viewLifecycleObserver(): TestObserver<Lifecycle.Event>
        fun nodeLifecycleObserver(): TestObserver<Lifecycle.Event>
    }
}
