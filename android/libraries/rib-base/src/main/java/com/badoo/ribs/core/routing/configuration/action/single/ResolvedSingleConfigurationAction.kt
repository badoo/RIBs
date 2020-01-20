package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.ConfigurationContext
import com.badoo.ribs.core.routing.configuration.ConfigurationKey
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams
import com.badoo.ribs.core.routing.transition.TransitionElement

/**
 * A convenience class to automatically call resolution on the key.
 * Child classes can act directly on the [ConfigurationContext.Resolved] element.
 */
internal abstract class ResolvedSingleConfigurationAction : SingleConfigurationAction {

    override fun <C : Parcelable> execute(
        key: ConfigurationKey,
        params: ActionExecutionParams<C>
    ): ConfigurationContext.Resolved<C> {
        val resolved = params.resolver.invoke(key)

        return execute(
            resolved,
            params
        )
    }

    abstract fun <C : Parcelable> execute(
        item: ConfigurationContext.Resolved<C>,
        params: ActionExecutionParams<C>
    ): ConfigurationContext.Resolved<C>


    override fun <C : Parcelable> transitionElements(
        key: ConfigurationKey,
        params: ActionExecutionParams<C>
    ): List<TransitionElement<C>> {
        val resolved = params.resolver.invoke(key)

        return transitionElements(
            resolved,
            params
        )
    }

    open fun <C : Parcelable> transitionElements(
        item: ConfigurationContext.Resolved<C>,
        params: ActionExecutionParams<C>
    ): List<TransitionElement<C>> =
        emptyList()
}
