package com.badoo.ribs.core.state

import io.reactivex.observers.TestObserver
import org.junit.Assert.assertEquals
import org.junit.Test

class TakeUntilTest {
    @Test
    fun `take until passes value before signal emits`() {
        val relay = Relay<Int>()
        val signal = Relay<Unit>()

        val testObserver = TestObserver<Int>()
        relay.first().takeUntil(signal).observe {
            testObserver.onNext(it)
        }

        relay.emit(0)
        signal.emit(Unit)

        testObserver.assertValue(0)
    }

    @Test
    fun `take until interrupts subscription when signal emits`() {
        val relay = Relay<Int>()
        val signal = Relay<Unit>()

        val testObserver = TestObserver<Int>()
        relay.first().takeUntil(signal).observe {
            testObserver.onNext(it)
        }

        signal.emit(Unit)
        relay.emit(0)

        testObserver.assertNoValues()
    }

    @Test
    fun `take until does not trigger subscription if signal is emitted straight away`() {
        var executed = false
        val source = just { executed = true }
        val signal = Relay.Behavior<Unit>()

        signal.emit(Unit)
        source.first().takeUntil(signal).observe { }

        assertEquals(false, executed)
    }
}
