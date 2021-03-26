package com.badoo.ribs.samples.comms_nodes.app

import com.badoo.ribs.android.text.Text

sealed class Language {
    abstract fun displayText(): Text

    object English : Language() {
        override fun displayText(): Text = Text.Plain("English")
    }

    object German : Language() {
        override fun displayText(): Text = Text.Plain("German")
    }

    object French : Language() {
        override fun displayText(): Text = Text.Plain("French")
    }
}