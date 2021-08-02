package com.badoo.ribs.minimal.reactive

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DistinctUntilChangedTest {

    @Test
    fun `emits first value`() {
        val relay = Relay<Int>()
        var result: Int? = null
        relay.distinctUntilChanged().observe { result = it }
        relay.accept(0)
        assertEquals(0, result)
    }

    @Test
    fun `skips duplicate`() {
        val relay = Relay<Int>()
        val results = ArrayList<Int>()
        relay.distinctUntilChanged().observe { results += it }
        relay.accept(0)
        relay.accept(0)
        assertEquals(listOf(0), results)
    }

    @Test
    fun `emits distinct value after duplicate`() {
        val relay = Relay<Int>()
        val results = ArrayList<Int>()
        relay.distinctUntilChanged().observe { results += it }
        relay.accept(0)
        relay.accept(0)
        relay.accept(1)
        assertEquals(listOf(0, 1), results)
    }

    @Test
    fun `multiple subscribers properly receives updates`() {
        val relay = Relay<Int>()
        val distinctUntilChanged = relay.distinctUntilChanged()
        val results = listOf(ArrayList<Int>(), ArrayList())
        for (i in results.indices) {
            distinctUntilChanged.observe { results[i] += it }
        }
        relay.accept(0)
        relay.accept(0)
        relay.accept(1)
        assertEquals(listOf(listOf(0, 1), listOf(0, 1)), results)
    }

}
