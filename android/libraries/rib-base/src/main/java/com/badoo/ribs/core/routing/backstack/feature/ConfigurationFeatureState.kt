package com.badoo.ribs.core.routing.backstack.feature

import android.os.Parcelable
import com.badoo.ribs.core.routing.backstack.ConfigurationContext
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.ActivationState.SLEEPING
import com.badoo.ribs.core.routing.backstack.ConfigurationKey
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class SavedState<C : Parcelable>(
    val pool: Map<ConfigurationKey, ConfigurationContext.Unresolved<C>>
) : Parcelable {

    fun toWorkingState(): WorkingState<C> =
        WorkingState(
            SLEEPING,
            pool
        )
}

internal data class WorkingState<C : Parcelable>(
    val activationLevel: ActivationState = SLEEPING,
    val pool: Map<ConfigurationKey, ConfigurationContext<C>> = mapOf()
) {
    fun toSavedState(): SavedState<C> =
        SavedState(
            pool.map {
                it.key to when (val entry = it.value) {
                    is ConfigurationContext.Unresolved -> entry
                    is ConfigurationContext.Resolved -> entry.shrink()
                }.copy(
                    activationState = SLEEPING
                )
            }.toMap()
        )
}
