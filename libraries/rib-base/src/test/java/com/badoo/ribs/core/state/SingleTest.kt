package com.badoo.ribs.core.state

import io.reactivex.observers.TestObserver
import org.junit.Test

class SingleTest {
    @Test
    fun `first takes only one first value emitted`() {
        val relay = Relay<Int>()

        val testObserver = TestObserver<Int>()
        relay.first().observe { testObserver.onNext(it) }

        relay.emit(0)
        relay.emit(1)

        testObserver.assertValue(0)
    }


    @Test
    fun `first does not keep values before subscription`() {
        val relay = Relay<Int>()
        val source = relay.first()

        relay.emit(0)

        val testObserver = TestObserver<Int>()
        source.observe { testObserver.onNext(it) }

        relay.emit(1)

        testObserver.assertValue(1)
    }
}
