package com.badoo.ribs.minimal.reactive

interface Emitter<in T> {
    fun emit(value: T)
}
