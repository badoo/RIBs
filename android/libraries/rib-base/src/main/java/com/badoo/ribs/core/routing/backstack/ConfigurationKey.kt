package com.badoo.ribs.core.routing.backstack

import android.os.Parcelable
import com.badoo.ribs.core.routing.backstack.feature.ConfigurationFeature
import com.badoo.ribs.core.routing.backstack.feature.WorkingState
import kotlinx.android.parcel.Parcelize

/**
 * Represents a complex key to refer to a configuration, so that elements with different types
 * do not overwrite each other with the same index in the pool.
 *
 * @see WorkingState.pool
 * @see ConfigurationFeature.ActorImpl.resolve
 *
 */
sealed class ConfigurationKey : Parcelable {

    @Parcelize
    data class Permanent(val index: Int) : ConfigurationKey()

    @Parcelize
    data class Content(val index: Int) : ConfigurationKey()

    @Parcelize
    data class Overlay(val key: Key) : ConfigurationKey() {

        @Parcelize
        data class Key(val contentKey: Content, val index: Int) : Parcelable
    }


}
