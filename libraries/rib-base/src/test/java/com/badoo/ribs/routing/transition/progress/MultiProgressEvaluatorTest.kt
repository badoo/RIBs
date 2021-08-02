package com.badoo.ribs.routing.transition.progress

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class MultiProgressEvaluatorTest {

    private val evaluator = MultiProgressEvaluator()

    @Test
    fun `GIVEN empty THEN progress is 0`() {
        assertEquals(0f, evaluator.progress)
    }

    @Test
    fun `GIVEN empty THEN isPending is false`() {
        assertEquals(false, evaluator.isPending)
    }

    @Test
    fun `GIVEN empty THEN isPendingSource returns false`() {
        var value: Any? = null
        evaluator.isPendingSource.observe { value = it }
        assertEquals(false, value)
    }

    @Test
    fun `GIVEN empty WHEN observed THEN does not allows to add more`() {
        evaluator.isPendingSource.observe { }
        assertThrows(IllegalStateException::class.java) {
            addTwoEvaluators()
        }
    }

    @Test
    fun `GIVEN empty WHEN observer created AND not observed THEN allows to add more`() {
        val source = evaluator.isPendingSource
        addTwoEvaluators()
        var value: Any? = null
        source.observe { value = it }
        assertEquals(true, value)
    }

    @Test
    fun `GIVEN 2 evaluators WHEN only one finished THEN isPending is true`() {
        val pair = addTwoEvaluators()
        pair.first.markFinished()
        assertEquals(true, evaluator.isPending)
    }

    @Test
    fun `GIVEN 2 evaluators WHEN both finished THEN isPending is false`() {
        val pair = addTwoEvaluators()
        pair.first.markFinished()
        pair.second.markFinished()
        assertEquals(false, evaluator.isPending)
    }

    @Test
    fun `GIVEN 2 evaluators WHEN both finished THEN notifies isPendingSource`() {
        var value: Any? = null
        val pair = addTwoEvaluators()
        evaluator.isPendingSource.observe { value = it }
        pair.first.markFinished()
        pair.second.markFinished()
        assertEquals(false, value)
    }

    private fun addTwoEvaluators(): Pair<SingleProgressEvaluator, SingleProgressEvaluator> {
        val first = SingleProgressEvaluator()
        val second = SingleProgressEvaluator()
        evaluator.add(first)
        evaluator.add(second)
        return first to second
    }

}
