package com.badoo.ribs.test

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.badoo.ribs.core.Rib

fun <R : Rib> RibTestHelper<R>.alignWith(ribTestHelper: RibTestHelper<*>) {
    ribTestHelper.lifecycle.addObserver(
        object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                moveTo(owner.lifecycle.currentState)
            }

            override fun onStart(owner: LifecycleOwner) {
                moveTo(owner.lifecycle.currentState)
            }

            override fun onResume(owner: LifecycleOwner) {
                moveTo(owner.lifecycle.currentState)
            }

            override fun onPause(owner: LifecycleOwner) {
                moveTo(owner.lifecycle.currentState)
            }

            override fun onStop(owner: LifecycleOwner) {
                moveTo(owner.lifecycle.currentState)
            }

            override fun onDestroy(owner: LifecycleOwner) {
                moveTo(owner.lifecycle.currentState)
            }
        }
    )
}
