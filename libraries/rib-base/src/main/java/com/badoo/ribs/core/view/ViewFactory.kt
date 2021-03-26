package com.badoo.ribs.core.view

import androidx.lifecycle.Lifecycle

interface ViewFactory<View> : (ViewFactory.Context) -> View {
    class Context(
        val parent: RibView,
        val lifecycle: Lifecycle,
    )
}
