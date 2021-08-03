package com.badoo.ribs.minimal.reactive

import io.reactivex.observers.TestObserver
import org.junit.jupiter.api.Test

class CombineLatestTest {

    @Test
    fun `combineLatest does not emit values when none emitted from sources`() {
        val relay1 = Relay<String>()
        val relay2 = Relay<String>()

        val source = combineLatest(relay1, relay2) { s1, s2 -> s1 + s2 }
        val observer = TestObserver<String>()
        source.observe { observer.onNext(it) }

        observer.assertNoValues()
    }

    @Test
    fun `combineLatest does not emit values when only the first value is received`() {
        val relay1 = Relay<String>()
        val relay2 = Relay<String>()

        relay1.emit("")

        val source = combineLatest(relay1, relay2) { s1, s2 -> s1 + s2 }
        val observer = TestObserver<String>()
        source.observe { observer.onNext(it) }

        observer.assertNoValues()
    }

    @Test
    fun `combineLatest does not emit values when only the second value is received`() {
        val relay1 = Relay<String>()
        val relay2 = Relay<String>()

        relay2.emit("")

        val source = combineLatest(relay1, relay2) { s1, s2 -> s1 + s2 }
        val observer = TestObserver<String>()
        source.observe { observer.onNext(it) }

        observer.assertNoValues()
    }

    @Test
    fun `combineLatest emit values when both values are received`() {
        val relay1 = Relay<String>()
        val relay2 = Relay<String>()

        val source = combineLatest(relay1, relay2) { s1, s2 -> s1 + s2 }
        val observer = TestObserver<String>()
        source.observe { observer.onNext(it) }

        relay1.emit("1")
        relay2.emit("2")

        observer.assertValue("12")
    }

    @Test
    fun `combineLatest does not emit values on resubscribe`() {
        val relay1 = Relay<String>()
        val relay2 = Relay<String>()

        val source = combineLatest(relay1, relay2) { s1, s2 -> s1 + s2 }
        source.observe { }

        relay1.emit("1")
        relay2.emit("2")

        val observer = TestObserver<String>()
        source.observe { observer.onNext(it) }
        observer.assertNoValues()
    }

    @Test
    fun `combineLatest combines latest values from two sources`() {
        val relay1 = Relay<String>()
        val relay2 = Relay<String>()

        val source = combineLatest(relay1, relay2) { s1, s2 -> s1 + s2 }
        val observer = TestObserver<String>()
        source.observe { observer.onNext(it) }

        relay1.emit("1")
        relay2.emit("2")
        relay1.emit("3")
        relay2.emit("4")
        observer.assertValues("12", "32", "34")
    }

    @Test
    fun `combineLatest distributes values to both instances`() {
        val relay1 = Relay<String>()
        val relay2 = Relay<String>()

        val source = combineLatest(relay1, relay2) { s1, s2 -> s1 + s2 }
        val observer1 = TestObserver<String>()
        val observer2 = TestObserver<String>()
        source.observe { observer1.onNext(it) }
        source.observe { observer2.onNext(it) }

        relay1.emit("1")
        relay2.emit("2")
        observer1.assertValues("12")
        observer2.assertValues("12")
    }

}
