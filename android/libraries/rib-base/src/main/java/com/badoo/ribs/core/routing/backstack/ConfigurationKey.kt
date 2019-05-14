package com.badoo.ribs.core.routing.backstack

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class ConfigurationKey : Parcelable {

    @Parcelize
    data class Content(val index: Int) : ConfigurationKey()

    @Parcelize
    data class Overlay(val index: Int) : ConfigurationKey()

    @Parcelize
    data class Permanent(val index: Int) : ConfigurationKey()
}
