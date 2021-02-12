package com.badoo.ribs.minimal.reactive

fun <T> just(producer: () -> T): Source<T> =
    object : Source<T> {
        override fun observe(callback: (T) -> Unit): Cancellable {
            callback(producer())
            return Cancellable.Empty
        }
    }

fun <A : Any, B : Any, C> combineLatest(source1: Source<A>, source2: Source<B>, combination: (A, B) -> C): Source<C> =
    object : Source<C> {
        /**
         * The internal state (first / second values) should be recreated for each new call to observer
         * Otherwise new subscriptions will not start from empty state but will share values, which is not desirable
         */
        override fun observe(callback: (C) -> Unit): Cancellable {
            var firstValue: A? = null
            var secondValue: B? = null

            fun emitCombined() {
                if (firstValue != null && secondValue != null) {
                    callback(combination(firstValue!!, secondValue!!))
                }
            }

            val cancellable1 = source1.observe {
                firstValue = it
                emitCombined()
            }
            val cancellable2 = source2.observe {
                secondValue = it
                emitCombined()
            }

            return Cancellable.cancellableOf {
                cancellable1.cancel()
                cancellable2.cancel()
            }
        }
    }

fun <T, R> Source<T>.map(transform: (T) -> R): Source<R> =
    object : Source<R> {
        override fun observe(callback: (R) -> Unit): Cancellable =
            this@map.observe { callback(transform(it)) }
    }

fun <T> Source<T>.filter(predicate: (T) -> Boolean): Source<T> =
    object : Source<T> {
        override fun observe(callback: (T) -> Unit): Cancellable =
            this@filter.observe {
                if (predicate(it)) callback(it)
            }
    }

fun <T> Source<T>.startWith(value: T): Source<T> =
    object : Source<T> {
        override fun observe(callback: (T) -> Unit): Cancellable {
            callback(value)
            return this@startWith.observe(callback)
        }
    }
