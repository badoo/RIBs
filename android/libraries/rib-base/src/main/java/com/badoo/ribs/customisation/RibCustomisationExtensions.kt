package com.badoo.ribs.customisation

import androidx.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.ViewGroup
import com.badoo.ribs.core.view.ViewFactoryOld

@Deprecated("Use new template from RIBs 0.3+ that has proper DI for views")
fun <View> inflateOnDemand(@LayoutRes layoutResourceId: Int): ViewFactoryOld<View> =
    object : ViewFactoryOld<View> {
        override fun invoke(p1: Nothing?): (ViewGroup) -> View =
            object : (ViewGroup) -> View {
                override fun invoke(parentViewGroup: ViewGroup): View =
                    inflate(parentViewGroup, layoutResourceId)
            }
    }

fun <T> inflate(parentViewGroup: ViewGroup, @LayoutRes layoutResourceId: Int): T =
    LayoutInflater.from(parentViewGroup.context).inflate(layoutResourceId, parentViewGroup, false) as T
