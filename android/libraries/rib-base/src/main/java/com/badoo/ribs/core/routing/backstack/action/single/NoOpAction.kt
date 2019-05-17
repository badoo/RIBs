package com.badoo.ribs.core.routing.backstack.action.single

import android.os.Parcelable
import com.badoo.ribs.core.routing.backstack.ConfigurationContext.Resolved
import com.badoo.ribs.core.routing.backstack.action.ActionExecutionParams

internal object NoOpAction : ResolvedSingleConfigurationAction() {

    override fun <C : Parcelable> execute(item: Resolved<C>, params: ActionExecutionParams<C>): Resolved<C> =
        // no-op
        item
}
