package com.badoo.ribs.rx2.clienthelper.conector

import com.badoo.ribs.rx2.clienthelper.conector.helpers.Rx2ConnectableTestRib.Input
import com.badoo.ribs.rx2.clienthelper.conector.helpers.Rx2ConnectableTestRib.Output
import com.badoo.ribs.rx2.clienthelper.conector.helpers.Rx2ConnectableTestRib.Output.Output1
import com.badoo.ribs.rx2.clienthelper.conector.helpers.Rx2ConnectableTestRib.Output.Output2
import com.badoo.ribs.rx2.clienthelper.conector.helpers.Rx2ConnectableTestRib.Output.Output3
import com.badoo.ribs.rx2.clienthelper.connector.NodeConnector
import io.reactivex.observers.TestObserver
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class Rx2NodeConnectorTest {

    @get:Rule
    val exceptionRule: ExpectedException = ExpectedException.none()

    private val testObserver = TestObserver<Output>()

    @After
    fun tearDown() {
        testObserver.dispose()
    }

    @Test
    fun `WHEN nodeConnector onAttached is called THEN every emitted output event is emitted again`() {
        val nodeConnector = NodeConnector<Input, Output>()

        nodeConnector.output.subscribe { testObserver.onNext(it) }

        nodeConnector.output.accept(Output1)
        nodeConnector.onAttached()

        testObserver.assertValueAt(0, Output1)
        testObserver.assertValueAt(1, Output1)
    }

    @Test
    fun `WHEN nodeConnector onAttached is called THEN every new emitted output is just emitted once`() {

        val nodeConnector = NodeConnector<Input, Output>()
        nodeConnector.output.subscribe { testObserver.onNext(it) }

        nodeConnector.onAttached()
        nodeConnector.output.accept(Output1)

        testObserver.assertValue(Output1)
        testObserver.assertValueCount(1)
    }

    @Test
    fun `WHEN nodeConnector onAttached is called twice THEN error is raised`() {
        val nodeConnector = NodeConnector<Input, Output>()

        nodeConnector.onAttached()

        exceptionRule.expect(IllegalStateException::class.java)
        nodeConnector.onAttached()
    }

    @Test
    fun `WHEN multiple output are accepted from multiple threads THEN output is correctly received when onAttached is called`() {
        //val testObserver = TestObserver<Output>()
        val nodeConnector = NodeConnector<Input, Output>()
        val threadNumber = 100
        val iterations = 10000
        val barrier = CyclicBarrier(threadNumber + 1)
        val executor = Executors.newFixedThreadPool(threadNumber).apply {
            repeat(threadNumber) {
                submit {
                    barrier.await()
                    repeat(iterations) {
                        nodeConnector.output.accept(Output1)
                        nodeConnector.output.accept(Output2)
                        nodeConnector.output.accept(Output3)

                    }
                }
            }
        }

        //Unlock threads(trip barrier) and wait for them to complete
        barrier.await()
        executor.shutdown()
        executor.awaitTermination(3, TimeUnit.SECONDS)

        nodeConnector.output.subscribe { testObserver.onNext(it) }
        nodeConnector.onAttached()

        testObserver.assertValueCount(threadNumber * iterations * 3)
    }
}

