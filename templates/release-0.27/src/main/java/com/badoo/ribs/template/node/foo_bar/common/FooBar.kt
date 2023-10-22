package com.badoo.ribs.template.node.foo_bar.common

import io.reactivex.Single

interface FooBar {

    interface Dependency

    sealed class Input

    sealed class Output

    // Workflow
    // todo: rename rather than delete, and add more
    // todo: expose all meaningful operations
    fun businessLogicOperation(): Single<FooBar>

    // todo: expose all possible children (even permanent parts), or remove if there's none
    // fun attachChild1(): Single<Child>
}
