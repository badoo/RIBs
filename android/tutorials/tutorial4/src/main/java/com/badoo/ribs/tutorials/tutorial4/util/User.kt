package com.badoo.ribs.tutorials.tutorial4.util

/**
 * A very dumb interface for now.
 */
interface User {

    fun name(): String

    companion object {
        val DUMMY = object : User {
            override fun name(): String =
                "Tutorial User"
        }
    }
}
