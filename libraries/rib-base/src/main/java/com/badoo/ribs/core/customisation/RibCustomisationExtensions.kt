package com.badoo.ribs.core.customisation

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.view.RibView

fun customisations(block: RibCustomisationDirectoryImpl.() -> Unit): RibCustomisationDirectoryImpl =
    RibCustomisationDirectoryImpl().apply(block)

@Deprecated(
    message = "Use ViewFactory.Context.inflate to properly support Jetpack intergations",
    level = DeprecationLevel.ERROR
)
fun <T> RibView.inflate(@LayoutRes layoutResourceId: Int): T =
    throw NotImplementedError()

@Deprecated(
    message = "Use ViewFactory.Context.inflate to properly support Jetpack intergations",
    level = DeprecationLevel.ERROR,
)
fun <T> ViewGroup.inflate(@LayoutRes layoutResourceId: Int): T =
    throw NotImplementedError()
