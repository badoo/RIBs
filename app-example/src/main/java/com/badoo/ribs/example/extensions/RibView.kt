package com.badoo.ribs.example.extensions

import android.view.View
import androidx.annotation.IdRes
import com.badoo.ribs.core.view.RibView

fun <T : View> RibView.findViewById(@IdRes id: Int): T = androidView.findViewById(id)
