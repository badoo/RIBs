package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams
import com.badoo.ribs.core.routing.transition.TransitionElement

internal class NoOpAction<C : Parcelable>(
    item: Resolved<C>
) : Action<C> {

    override fun onBeforeTransition() {}
    override fun onTransition() {}
    override fun onFinish() {}
    override fun reverse() {}
    override val result: Resolved<C> =
        item

    override val transitionElements: List<TransitionElement<C>> =
        emptyList()

    object Factory : ActionFactory {
        override fun <C : Parcelable> create(
            key: ConfigurationKey,
            params: ActionExecutionParams<C>,
            isBackStackOperation: Boolean
        ): Action<C> =
            NoOpAction(params.resolver.invoke(key).first)
    }
}
