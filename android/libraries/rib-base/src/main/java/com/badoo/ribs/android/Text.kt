package com.badoo.ribs.android

import android.content.Context

interface Text {

    fun resolve(context: Context): String

    class Plain(private val string: String): Text {
        override fun resolve(context: Context): String =
            string
    }

    class ResId(private val resId: Int, private vararg val formatArgs: Any) : Text {
        override fun resolve(context: Context): String =
            context.resources.getString(resId, formatArgs)
    }
}
