package com.badoo.ribs.clienthelper.childaware

import androidx.lifecycle.Lifecycle

internal val Lifecycle.isDestroyed
    get() = currentState == Lifecycle.State.DESTROYED
