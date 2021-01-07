package com.badoo.ribs.android.store

import androidx.lifecycle.ViewModel

/**
 * Used as marker for persisting [com.badoo.ribs.store.RetainedInstanceStore].
 *
 * When activity or fragment is recreated during configuration changes,
 * [onCleared] callback is not invoked,
 * which means that we should not clear RetainedInstanceStore.
 *
 * When activity or fragment is destroyed completely (because of navigation),
 * [onCleared] callback is invoked and RetainedInstanceStore should be cleared too.
 *
 * As an alternative solution different combination of flags (`isFinishing` and etc.)
 * can be used to check if activity or fragment will be recreated soon.
 * But this approach is not as reliable as [ViewModel] implementation supported by Google itself.
 * See `moxy-community/Moxy` and `sockeqwe/mosby` implementation and issues for additional information.
 */
internal class RetainedInstanceStoreViewModel : ViewModel() {

    var isCleared: Boolean = false
        private set

    override fun onCleared() {
        isCleared = true
    }

}
