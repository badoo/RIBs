package com.badoo.ribs.minimal.state

import io.reactivex.observers.TestObserver
import org.junit.Test

class StoreTest {
    @Test
    fun `store emits initial state on observe`() {
        val store = object : Store<Int>(0) { }

        val testObserver = TestObserver<Int>()
        store.observe { testObserver.onNext(it) }

        testObserver.assertValue(0)
    }

    @Test
    fun `store emits latest state on observe`() {
        val store = object : Store<Int>(0) {
            fun next() {
                emit(state + 1)
            }
        }

        val testObserver = TestObserver<Int>()
        store.next()
        store.observe { testObserver.onNext(it) }

        testObserver.assertValue(1)
    }

    @Test
    fun `observer receives state updates`() {
        val store = object : Store<Int>(0) {
            fun next() {
                emit(state + 1)
            }
        }

        val testObserver = TestObserver<Int>()
        store.observe { testObserver.onNext(it) }
        store.next()

        testObserver.assertValues(0, 1)
    }

    @Test
    fun `observer does not receive values after cancel`() {
        val store = object : Store<Int>(0) {
            fun next() {
                emit(state + 1)
            }
        }

        val testObserver = TestObserver<Int>()
        val cancellable = store.observe { testObserver.onNext(it) }
        cancellable.cancel()

        store.next()
        testObserver.assertValues(0)
    }

    @Test
    fun `async store can emit multiple values through events`() {
        val store = object : AsyncStore<Int, String>("") {
            override fun reduceEvent(event: Int, state: String): String =
                state + event

            fun trigger() {
                emitEvent(1)
                emitEvent(2)
            }
        }

        val testObserver = TestObserver<String>()
        store.observe { testObserver.onNext(it) }

        store.trigger()
        testObserver.assertValues("", "1", "12")
    }
}
