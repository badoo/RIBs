package com.badoo.ribs.test

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.badoo.ribs.core.Rib

interface RibTestHelper<out R : Rib> : LifecycleOwner {

    /**
     * Moves [Rib] state to [state].
     *
     * Manually invoke `moveTo(State.DESTROYED)` after each test to release resources properly.
     */
    fun moveTo(state: Lifecycle.State)

    /**
     * Moves [Rib] to [state], invokes [block] and destroys [Rib].
     *
     * Supports nested calls.
     * ```kotlin
     * moveToStateAndCheck(State.CREATED) {
     *   assert(state == State.CREATED)
     *   moveToStateAndCheck(State.STARTED) {
     *     assert(state == State.STARTED)
     *   }
     *   assert(state == State.CREATED)
     * }
     * assert(state == State.DESTROYED)
     * ```
     */
    fun moveToStateAndCheck(state: Lifecycle.State, block: (R) -> Unit)

}
