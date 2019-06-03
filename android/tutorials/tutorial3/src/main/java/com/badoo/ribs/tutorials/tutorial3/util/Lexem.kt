package com.badoo.ribs.tutorials.tutorial3.util

import android.content.Context
import android.support.annotation.StringRes

interface Lexem {

    fun resolve(context: Context): String

    data class Text(val text: String) : Lexem {
        override fun resolve(context: Context): String =
            text
    }

    data class Resource(@StringRes val resId: Int) : Lexem {
        override fun resolve(context: Context): String =
            context.getString(resId)
    }
}
