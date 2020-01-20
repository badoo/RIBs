package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.Action
import com.badoo.ribs.core.routing.configuration.ActionFactory
import com.badoo.ribs.core.routing.configuration.ConfigurationContext
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams
import com.badoo.ribs.core.routing.transition.TransitionElement

internal class NoOpAction<C : Parcelable>(
    item: Resolved<C>
) : Action<C> {

    override fun onPreExecute() {}
    override fun execute() {}
    override fun onPostExecute() {}
    override fun finally() {}
    override val result: Resolved<C> =
        item

    override val transitionElements: List<TransitionElement<C>> =
        emptyList()

    object Factory : ActionFactory {
        override fun <C : Parcelable> create(
            key: ConfigurationKey,
            params: ActionExecutionParams<C>
        ): Action<C> =
            NoOpAction(params.resolver.invoke(key))
    }
}
