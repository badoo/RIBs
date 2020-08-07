package com.badoo.ribs.core.state

fun <T> single(producer: () -> T): SingleSource<T> =
    SingleSource(just(producer))

fun <T, R> SingleSource<T>.takeUntil(terminationSignal: Source<R>): SingleSource<T> =
    SingleSource(
        TakeUntilSource(this, terminationSignal)
    )

fun <T> Source<T>.first(): SingleSource<T> =
    SingleSource(this)

class SingleSource<T>(private val upstream: Source<T>) : Source<T> {
    override fun observe(callback: (T) -> Unit): Cancellable {
        var cancellable: Cancellable? = null
        cancellable = upstream.observe {
            callback(it)
            cancellable?.cancel()
        }
        return cancellable
    }
}
