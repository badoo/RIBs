package com.badoo.ribs.android.text

import android.content.Context

/**
 * An abstraction over text, so that you can provide it from different sources.
 *
 * In case the default implementations are not good enough, feel free to implement your own.
 */
interface Text {

    fun resolve(context: Context): CharSequence

    class Plain(private val string: String): Text {
        override fun resolve(context: Context): CharSequence =
            string
    }

    class Resource(private val resId: Int, private vararg val formatArgs: Any) :
        Text {
        override fun resolve(context: Context): CharSequence =
            context.resources.getString(resId, *formatArgs)
    }
}
