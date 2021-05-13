package com.badoo.ribs.rx2.clienthelper.conector

import com.badoo.ribs.rx2.clienthelper.conector.helpers.Rx2ConnectableTestRib.Input
import com.badoo.ribs.rx2.clienthelper.conector.helpers.Rx2ConnectableTestRib.Output
import com.badoo.ribs.rx2.clienthelper.conector.helpers.Rx2ConnectableTestRib.Output.Output1
import com.badoo.ribs.rx2.clienthelper.conector.helpers.Rx2ConnectableTestRib.Output.Output2
import com.badoo.ribs.rx2.clienthelper.conector.helpers.Rx2ConnectableTestRib.Output.Output3
import com.badoo.ribs.rx2.clienthelper.connector.NodeConnector
import com.badoo.ribs.test.BaseNodeConnectorTest
import io.reactivex.observers.TestObserver
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class Rx2NodeConnectorTest : BaseNodeConnectorTest() {

    private val firstTestObserver = TestObserver<Output>()
    private val secondTestObserver = TestObserver<Output>()

    @AfterEach
    fun tearDown() {
        firstTestObserver.dispose()
        secondTestObserver.dispose()
    }

    @Test
    override fun GIVEN_nodeConnector_onAttached_is_not_called_WHEN_output_is_accepted_THEN_accepted_output_do_not_reach_observer() {
        val nodeConnector = NodeConnector<Input, Output>()
        nodeConnector.output.subscribe { firstTestObserver.onNext(it) }

        nodeConnector.output.accept(Output1)

        firstTestObserver.assertValueCount(0)
    }

    @Test
    override fun GIVEN_and_output_is_accepted_before_onAttached_WHEN_nodeConnector_onAttached_is_called_THEN_accepted_output_reach_the_observer() {
        val nodeConnector = NodeConnector<Input, Output>()
        nodeConnector.output.subscribe { firstTestObserver.onNext(it) }

        nodeConnector.output.accept(Output1)
        nodeConnector.onAttached()

        firstTestObserver.assertValue(Output1)
        firstTestObserver.assertValueCount(1)
    }

    @Test
    override fun GIVEN_nodeConnector_is_attached_WHEN_output_is_accepted_THEN_every_accepted_output_reach_the_observer() {
        val nodeConnector = NodeConnector<Input, Output>()
        nodeConnector.output.subscribe { firstTestObserver.onNext(it) }

        nodeConnector.onAttached()
        nodeConnector.output.accept(Output1)

        firstTestObserver.assertValue(Output1)
        firstTestObserver.assertValueCount(1)
    }

    @Test
    override fun GIVEN_outputs_accepted_before_and_after_onAttached_WHEN_node_is_attached_THEN_every_accepted_output_reach_the_observer() {
        val nodeConnector = NodeConnector<Input, Output>()
        nodeConnector.output.subscribe { firstTestObserver.onNext(it) }

        nodeConnector.output.accept(Output1)
        nodeConnector.onAttached()
        nodeConnector.output.accept(Output2)
        nodeConnector.output.accept(Output3)

        firstTestObserver.assertValueAt(0, Output1)
        firstTestObserver.assertValueAt(1, Output2)
        firstTestObserver.assertValueAt(2, Output3)
        firstTestObserver.assertValueCount(3)
    }

    @Test
    override fun WHEN_nodeConnector_onAttached_is_called_twice_THEN_error_is_raised() {
        val nodeConnector = NodeConnector<Input, Output>()

        nodeConnector.onAttached()
        assertThrows(IllegalStateException::class.java) {
            nodeConnector.onAttached()
        }
    }

    @Test
    override fun GIVEN_multiple_observers_and_output_is_accepted_before_OnAttached_WHEN_nodeConnector_onAttached_is_called_THEN_every_accepted_output_reach_the_observers() {
        val nodeConnector = NodeConnector<Input, Output>()
        nodeConnector.output.subscribe { firstTestObserver.onNext(it) }
        nodeConnector.output.subscribe { secondTestObserver.onNext(it) }

        nodeConnector.output.accept(Output1)
        nodeConnector.onAttached()

        firstTestObserver.assertValue(Output1)
        firstTestObserver.assertValueCount(1)
        secondTestObserver.assertValue(Output1)
        secondTestObserver.assertValueCount(1)
    }

    @Test
    override fun GIVEN_multiple_observers_and_nodeConnector_is_attached_WHEN_output_is_accepted_THEN_every_accepted_output_reach_the_observer() {
        val nodeConnector = NodeConnector<Input, Output>()
        nodeConnector.output.subscribe { firstTestObserver.onNext(it) }
        nodeConnector.output.subscribe { secondTestObserver.onNext(it) }

        nodeConnector.onAttached()
        nodeConnector.output.accept(Output1)

        firstTestObserver.assertValue(Output1)
        firstTestObserver.assertValueCount(1)
        secondTestObserver.assertValue(Output1)
        secondTestObserver.assertValueCount(1)
    }

    @Test
    override fun GIVEN_multiple_observers_that_subscribe_before_and_after_onAttached__and_outputs_accepted_before_and_after_onAttached_WHEN_node_is_attached_THEN_every_accepted_output_reach_the_observer() {
        val nodeConnector = NodeConnector<Input, Output>()
        //First subscriber subscribe BEFORE onAttached
        nodeConnector.output.subscribe { firstTestObserver.onNext(it) }

        //Output accepted BEFORE onAttached
        nodeConnector.output.accept(Output1)
        nodeConnector.onAttached()

        //Second subscriber subscribe AFTER onAttached
        nodeConnector.output.subscribe { secondTestObserver.onNext(it) }

        //Outputs accepted AFTER onAttached
        nodeConnector.output.accept(Output2)
        nodeConnector.output.accept(Output3)

        firstTestObserver.assertValueAt(0, Output1)
        firstTestObserver.assertValueAt(1, Output2)
        firstTestObserver.assertValueAt(2, Output3)
        firstTestObserver.assertValueCount(3)

        secondTestObserver.assertValueAt(0, Output2)
        secondTestObserver.assertValueAt(1, Output3)
        secondTestObserver.assertValueCount(2)
    }


    @Test
    override fun WHEN_multiple_output_are_accepted_from_multiple_threads_THEN_output_is_correctly_received_when_onAttached_is_called() {
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

        nodeConnector.output.subscribe { firstTestObserver.onNext(it) }
        nodeConnector.onAttached()

        firstTestObserver.assertValueCount(threadNumber * iterations * 3)
    }


    /**
     * Why is this test repeated 1000 times?
     * This test exist to ensure that there is no race condition issues under the threading scenario described by the test.
     * To do so, a high amount of repetitions must be executed as when the race condition is happening, it could only produced
     * an unexpected result in some % of the case.
     * E.g: Race condition between Thread1 and Thread2
     * When Thread1 is executed first -> Desired Scenario
     * When Thread2 is executed first -> Undesired Scenario
     * Under this example if both threads get the lock with the same priority, the % of each case would be 50%.
     *
     * Why the test logic is duplicated?
     * Seems that CyclicBarrier is a bit biased and is giving preference to the first thread awaiting to be executed first.
     * So to balance this weighed thread execution priority, we test both cases in the same test to increase the % of failure
     * when race condition issue is present.
     */
    @RepeatedTest(1000)
    override fun WHEN_accept_and_onAttached_are_called_by_different_thread_at_the_same_time_THEN_output_is_the_expected() {
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
                nodeConnector1.onAttached()
            }
        }
        val executor2 = Executors.newFixedThreadPool(threadNumber).apply {
            //Emitter thread
            submit {
                barrier2.await()
                nodeConnector2.output.accept(Output1)
            }
            //Attacher thread
            submit {
                barrier2.await()
                nodeConnector2.onAttached()
            }
        }

        //subscribe nodes
        nodeConnector1.output.subscribe { firstTestObserver.onNext(it) }
        nodeConnector2.output.subscribe { secondTestObserver.onNext(it) }

        //Unlock threads(trip barrier) and wait for them to complete
        barrier1.await()
        barrier2.await()
        executor.shutdown()
        executor2.shutdown()
        executor.awaitTermination(3, TimeUnit.SECONDS)
        executor2.awaitTermination(3, TimeUnit.SECONDS)

        firstTestObserver.assertValue(Output1)
        firstTestObserver.assertValueCount(1)
        secondTestObserver.assertValue(Output1)
        secondTestObserver.assertValueCount(1)
    }
}
