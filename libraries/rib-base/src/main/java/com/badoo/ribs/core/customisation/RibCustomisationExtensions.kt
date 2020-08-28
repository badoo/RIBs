package com.badoo.ribs.core.customisation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.view.RibView

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
