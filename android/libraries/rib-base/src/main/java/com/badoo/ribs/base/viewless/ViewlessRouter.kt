package com.badoo.ribs.base.viewless

import android.os.Parcelable
import com.badoo.ribs.core.Router

abstract class ViewlessRouter<C: Parcelable>(
    initialConfiguration: C
): Router<C, Nothing>(initialConfiguration)
