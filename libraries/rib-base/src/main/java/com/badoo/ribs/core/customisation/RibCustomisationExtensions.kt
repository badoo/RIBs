package com.badoo.ribs.core.customisation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.bumble.appyx.utils.customisations.NodeCustomisationDirectoryImpl

fun customisations(block: NodeCustomisationDirectoryImpl.() -> Unit): NodeCustomisationDirectoryImpl =
    NodeCustomisationDirectoryImpl().apply(block)

fun <T> ViewFactory.Context.inflate(@LayoutRes layoutResourceId: Int): T =
    parent.inflate(layoutResourceId)

fun <T> RibView.inflate(@LayoutRes layoutResourceId: Int): T =
    androidView.inflate(layoutResourceId)

fun <T> ViewGroup.inflate(@LayoutRes layoutResourceId: Int): T =
    LayoutInflater
        .from(context)
        .inflate(
            layoutResourceId,
            this,
            false
        ) as T
