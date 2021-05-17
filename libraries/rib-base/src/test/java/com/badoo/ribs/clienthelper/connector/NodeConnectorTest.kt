package com.badoo.ribs.clienthelper.connector

import com.badoo.ribs.core.helper.connectable.ConnectableTestRib.Input
import com.badoo.ribs.core.helper.connectable.ConnectableTestRib.Output
import com.badoo.ribs.core.helper.connectable.ConnectableTestRib.Output.Output1
import com.badoo.ribs.core.helper.connectable.ConnectableTestRib.Output.Output2
import com.badoo.ribs.core.helper.connectable.ConnectableTestRib.Output.Output3
import com.badoo.ribs.test.BaseNodeConnectorTest
import io.reactivex.observers.TestObserver
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class NodeConnectorTest : BaseNodeConnectorTest() {

    private val firstTestObserver = TestObserver<Output>()
    private val secondTestObserver = TestObserver<Output>()

    @AfterEach
    fun tearDown() {
        firstTestObserver.dispose()
        secondTestObserver.dispose()
    }

    @Test
    override fun `GIVEN nodeConnector onAttached is not called WHEN output is accepted THEN accepted output do not reach observer`() {
        val nodeConnector = NodeConnector<Input, Output>()
        nodeConnector.output.observe { firstTestObserver.onNext(it) }

        nodeConnector.output.accept(Output1)

        firstTestObserver.assertValueCount(0)
    }

    @Test
    override fun `GIVEN and output is accepted before onAttached WHEN nodeConnector onAttached is called THEN accepted output reach the observer`() {
        val nodeConnector = NodeConnector<Input, Output>()
        nodeConnector.output.observe { firstTestObserver.onNext(it) }

        nodeConnector.output.accept(Output1)
        nodeConnector.onAttach()

        firstTestObserver.assertValues(Output1)
    }

    @Test
    override fun `GIVEN nodeConnector is attached WHEN output is accepted THEN every accepted output reach the observer`() {
        val nodeConnector = NodeConnector<Input, Output>()
        nodeConnector.output.observe { firstTestObserver.onNext(it) }

        nodeConnector.onAttach()
        nodeConnector.output.accept(Output1)

        firstTestObserver.assertValues(Output1)
    }

    @Test
    override fun `GIVEN outputs accepted before and after onAttached WHEN node is attached THEN every accepted output reach the observer`() {
        val nodeConnector = NodeConnector<Input, Output>()
        nodeConnector.output.observe { firstTestObserver.onNext(it) }

        nodeConnector.output.accept(Output1)
        nodeConnector.onAttach()
        nodeConnector.output.accept(Output2)
        nodeConnector.output.accept(Output3)

        firstTestObserver.assertValues(Output1, Output2, Output3)
    }

    @Test
    override fun `WHEN nodeConnector onAttached is called twice THEN error is raised`() {
        val nodeConnector = NodeConnector<Input, Output>()

        nodeConnector.onAttach()
        assertThrows(IllegalStateException::class.java) {
            nodeConnector.onAttach()
        }
    }

    @Test
    override fun `GIVEN multiple observers and output is accepted before OnAttached WHEN nodeConnector onAttached is called THEN every accepted output reach the observers`() {
        val nodeConnector = NodeConnector<Input, Output>()
        nodeConnector.output.observe { firstTestObserver.onNext(it) }
        nodeConnector.output.observe { secondTestObserver.onNext(it) }

        nodeConnector.output.accept(Output1)
        nodeConnector.onAttach()

        firstTestObserver.assertValues(Output1)
        secondTestObserver.assertValues(Output1)
    }

    @Test
    override fun `GIVEN multiple observers and nodeConnector is attached WHEN output is accepted THEN every accepted output reach the observer`() {
        val nodeConnector = NodeConnector<Input, Output>()
        nodeConnector.output.observe { firstTestObserver.onNext(it) }
        nodeConnector.output.observe { secondTestObserver.onNext(it) }

        nodeConnector.onAttach()
        nodeConnector.output.accept(Output1)

        firstTestObserver.assertValues(Output1)
        secondTestObserver.assertValues(Output1)
    }

    @Test
    override fun `GIVEN multiple observers that subscribe before and after onAttached  and outputs accepted before and after onAttached WHEN node is attached THEN every accepted output reach the observer`() {
        val nodeConnector = NodeConnector<Input, Output>()
        //First observer observe BEFORE onAttached
        nodeConnector.output.observe { firstTestObserver.onNext(it) }

        //Output accepted BEFORE onAttached
        nodeConnector.output.accept(Output1)
        nodeConnector.onAttach()

        //Second observer observe AFTER onAttached
        nodeConnector.output.observe { secondTestObserver.onNext(it) }

        //Outputs accepted AFTER onAttached
        nodeConnector.output.accept(Output2)
        nodeConnector.output.accept(Output3)

        firstTestObserver.assertValues(Output1, Output2, Output3)
        secondTestObserver.assertValues(Output2, Output3)
    }


    @Test
    override fun `WHEN multiple output are accepted from multiple threads THEN output is correctly received when onAttached is called`() {
        val nodeConnector = NodeConnector<Input, Output>()
        val threadNumber = 100
        val iterations = 10000
        val barrier = CyclicBarrier(threadNumber + 1)
        val executor = Executors.newFixedThreadPool(threadNumber).apply {
            repeat(threadNumber) {
                submit {
                    barrier.awaitWithTimeOut()
                    repeat(iterations) {
                        nodeConnector.output.accept(Output1)
                        nodeConnector.output.accept(Output2)
                        nodeConnector.output.accept(Output3)

                    }
                }
            }
        }

        //Unlock threads(trip barrier) and wait for them to complete
        barrier.awaitWithTimeOut()
        executor.shutdown()
        executor.awaitWithTimeOut()

        nodeConnector.output.observe { firstTestObserver.onNext(it) }
        nodeConnector.onAttach()

        firstTestObserver.assertValueCount(threadNumber * iterations * 3)
    }


    /**
     * Why is this test repeated 1000 times?
     * This test exist to ensure that there is no race condition issues under the threading scenario described by the test.
     * To do so, a high amount of repetitions must be executed as when the race condition is happening, it could only produce
     * an unexpected result in some % of the case.
     * E.g: Race condition between Thread1 and Thread2
     * When Thread1 is executed first -> Desired Scenario
     * When Thread2 is executed first -> Undesired Scenario
     * Under this example if both threads get the lock with the same priority, the % of each case would be 50%.
     *
     * Why is the test logic duplicated?
     * Seems that CyclicBarrier is a bit biased and is giving preference to the first thread awaiting to be executed first.
     * So to balance this weighed thread execution priority, we switch order and test both in the same test to increase the
     * % of failure when race condition issue is present.
     */
    @RepeatedTest(1000)
    override fun `WHEN accept and onAttached are called by different thread at the same time THEN output is the expected`() {
        val nodeConnector1 = NodeConnector<Input, Output>()
        val nodeConnector2 = NodeConnector<Input, Output>()
        val threadNumber = 2
        val barrier1 = CyclicBarrier(threadNumber + 1)
        val barrier2 = CyclicBarrier(threadNumber + 1)

        val executor = Executors.newFixedThreadPool(threadNumber).apply {
            //Emitter thread
            submit {
                barrier1.await()
                nodeConnector1.output.accept(Output1)
            }
            //Attacher thread
            submit {
                barrier1.await()
                nodeConnector1.onAttach()
            }
        }
        val executor2 = Executors.newFixedThreadPool(threadNumber).apply {
            //Attacher thread
            submit {
                barrier2.awaitWithTimeOut()
                nodeConnector2.onAttach()
            }
            //Emitter thread
            submit {
                barrier2.awaitWithTimeOut()
                nodeConnector2.output.accept(Output1)
            }
        }

        //Observe nodes
        nodeConnector1.output.observe { firstTestObserver.onNext(it) }
        nodeConnector2.output.observe { secondTestObserver.onNext(it) }

        //Unlock threads(trip barrier) and wait for them to complete
        barrier1.awaitWithTimeOut()
        barrier2.awaitWithTimeOut()
        executor.shutdown()
        executor2.shutdown()
        executor.awaitWithTimeOut()
        executor2.awaitWithTimeOut()

        firstTestObserver.assertValues(Output1)
        secondTestObserver.assertValues(Output1)
    }

    private fun CyclicBarrier.awaitWithTimeOut() {
        await(TIME_OUT_S, TimeUnit.SECONDS)
    }

    private fun ExecutorService.awaitWithTimeOut() {
        awaitTermination(TIME_OUT_S, TimeUnit.SECONDS)
    }

    companion object {
        private const val TIME_OUT_S = 30L
    }
}

