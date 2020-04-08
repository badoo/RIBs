package com.badoo.ribs.core.routing.configuration

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.feature.ConfigurationFeature
import com.badoo.ribs.core.routing.configuration.feature.WorkingState
import kotlinx.android.parcel.Parcelize

/**
 * TODO reword to reflect new changes that configuration moved from ConfigurationCommand to here
 *
 * Represents a complex key to refer to a configuration, so that elements with different types
 * do not overwrite each other with the same index in the pool.
 *
 * Unique payloads are required to make a difference between elements that are changing in the
 * same Transaction, in the same position, but with different reflected Configuration.
 *
 * E.g. after a replace operation on the back stack, the following could happen:
 *
 * Transaction(Remove(Content(123)), Add(Content(123))
 *
 * Where even though they have the same position, they refer to different configurations. If executed
 * in the same Transaction, this could mean immediately removing the newly added Configuration.
 *
 * Payload in itself is allowed to repeat multiple times (same child RIB in back stack more
 * than once), but position + payload is a completely unique compound key per parent.
 *
 * Permanent keys don't need unique payload. Being permanent implies they won't ever get removed
 * or replaced.
 *
 * @see WorkingState.pool
 * @see ConfigurationFeature.ActorImpl.resolve
 *
 */
sealed class ConfigurationKey<C : Parcelable> : Parcelable {

    abstract val configuration: C

    @Parcelize
    data class Permanent<C : Parcelable>(val index: Int, override val configuration: C) : ConfigurationKey<C>()

    @Parcelize
    data class Content<C : Parcelable>(val index: Int, override val configuration: C) : ConfigurationKey<C>()

    @Parcelize
    data class Overlay<C : Parcelable>(val key: Key<C>) : ConfigurationKey<C>() {

        override val configuration: C
            get() = key.configuration

        @Parcelize
        data class Key<C : Parcelable>(val contentKey: Content<C>, val index: Int, val configuration: C) : Parcelable
    }


}
