package com.badoo.ribs.customisation

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.ViewGroup


fun <T> inflate(parentViewGroup: ViewGroup, @LayoutRes layoutResourceId: Int): T =
    LayoutInflater.from(parentViewGroup.context).inflate(layoutResourceId, parentViewGroup, false) as T
