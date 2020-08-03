package com.badoo.ribs.core.customisation

import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.view.RibView

fun <T> RibView.inflate(@LayoutRes layoutResourceId: Int): T =
    LayoutInflater
        .from(androidView.context)
        .inflate(
            layoutResourceId,
            androidView,
            false
        ) as T
