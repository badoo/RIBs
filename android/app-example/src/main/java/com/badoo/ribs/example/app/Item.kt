package com.badoo.ribs.example.app

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class Item : Parcelable {
    @Parcelize
    object LoremIpsumItem : Item()
    @Parcelize
    object FooBarItem : Item()
}
