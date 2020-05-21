package com.badoo.ribs.core.routing.configuration

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeature
import com.badoo.ribs.core.routing.configuration.feature.BackStackFeatureState
import com.badoo.ribs.core.routing.configuration.feature.TransitionDescriptor
import com.badoo.ribs.core.routing.history.RoutingHistory
import io.reactivex.Observable
import io.reactivex.ObservableSource

/**
 * Takes the state emissions from [BackStackFeature], and translates them to a stream of
 * [ConfigurationCommand]s.
 *
 * @see [ConfigurationCommandCreator.diff]
 */
internal fun <C : Parcelable> ObservableSource<RoutingHistory<C>>.toCommands(): Observable<Transaction<C>> {
    return Observable.wrap(this)
        // FIXME this was the original and working one, but signature change means initialState is not accessible
//        .startWith(initialState)

        // FIXME temp solution, but this won't be good when restoring from bundle
        .startWith(BackStackFeatureState()) // TODO reconsider // Bootstrapper can overwrite it by the time we receive the first state emission here

        .buffer(2, 1)
        .flatMap { (previous, current) ->
            val commands =
                ConfigurationCommandCreator.diff(
                    //  Note: Next PR will add new differ algorithm and this legacy list-based approach will be removed
                    previous.toList(),
                    current.toList()
                )
            when {
                commands.isNotEmpty() -> Observable.just(
                    Transaction.ListOfCommands(
                        descriptor = TransitionDescriptor(
                            from = previous,
                            to = current
                        ),
                        commands = commands
                    )
                )
                else -> Observable.empty()
            }
        }
}
