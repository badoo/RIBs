package com.badoo.ribs.core.routing.configuration.action.single

import android.os.Parcelable
import com.badoo.ribs.core.routing.configuration.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.configuration.action.ActionExecutionParams

internal object NoOpAction : ResolvedSingleConfigurationAction() {

    override fun <C : Parcelable> execute(item: Resolved<C>, params: ActionExecutionParams<C>): Resolved<C> =
        // no-op
        item
}
