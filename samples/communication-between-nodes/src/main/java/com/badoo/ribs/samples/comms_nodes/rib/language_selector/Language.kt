package com.badoo.ribs.samples.comms_nodes.rib.language_selector

import android.os.Parcelable
import com.badoo.ribs.android.text.Text
import kotlinx.android.parcel.Parcelize

sealed class Language : Parcelable {
    abstract fun displayText(): Text

    @Parcelize
    object English : Language() {
        override fun displayText(): Text = Text.Plain("English")
    }

    @Parcelize object German : Language() {
        override fun displayText(): Text = Text.Plain("German")
    }

    @Parcelize object French : Language() {
        override fun displayText(): Text = Text.Plain("French")
    }
}
