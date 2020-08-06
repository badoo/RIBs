package com.badoo.ribs.core.state

fun <A : Any, B : Any, C> combineLatest(source1: Source<A>, source2: Source<B>, combination: (A, B) -> C): Source<C> =
    object : Source<C> {
        override fun observe(callback: (C) -> Unit): Cancellable {
            var firstValue: A? = null
            var secondValue: B? = null

            fun emitIfComplete() {
                if (firstValue != null && secondValue != null) {
                    callback(combination(firstValue!!, secondValue!!))
                }
            }

            val cancellable1 = source1.observe {
                firstValue = it
                emitIfComplete()
            }
            val cancellable2 = source2.observe {
                secondValue = it
                emitIfComplete()
            }

            return Cancellable.cancellableOf {
                cancellable1.cancel()
                cancellable2.cancel()
            }
        }
    }
