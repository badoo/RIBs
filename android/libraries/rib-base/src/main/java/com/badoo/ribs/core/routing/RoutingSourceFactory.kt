package com.badoo.ribs.core.routing

import android.os.Parcelable
import com.badoo.ribs.core.builder.BuildParams

interface RoutingSourceFactory<C : Parcelable, RS : RoutingSource<C>>: (BuildParams<*>) -> RS
