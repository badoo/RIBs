package com.badoo.ribs.minimal.reactive

interface Source<out T> {
    fun observe(callback: (T) -> Unit): Cancellable
}
