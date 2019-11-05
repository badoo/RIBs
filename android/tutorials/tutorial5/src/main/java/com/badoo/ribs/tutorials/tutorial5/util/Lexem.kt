package com.badoo.ribs.tutorials.tutorial5.util

import android.content.Context
import androidx.annotation.StringRes

interface Lexem {

    fun resolve(context: Context): String

    data class Text(val text: String) : Lexem {
        override fun resolve(context: Context): String =
            text
    }

    class Resource(@StringRes val resId: Int, private vararg val formatArgs: Any) : Lexem {
        override fun resolve(context: Context): String =
            context.getString(resId, *formatArgs)
    }
}



