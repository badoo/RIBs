package com.badoo.ribs.core.state

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
